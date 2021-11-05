package com.vv.personal.diurnal.interaction.controller;

import com.vv.personal.diurnal.artifactory.generated.OtpMailProto;
import com.vv.personal.diurnal.artifactory.generated.ResponsePrimitiveProto;
import com.vv.personal.diurnal.interaction.config.BeanStore;
import com.vv.personal.diurnal.interaction.config.MailOtpConfig;
import com.vv.personal.diurnal.interaction.config.OtpConfig;
import com.vv.personal.diurnal.interaction.util.DiurnalUtil;
import com.vv.personal.diurnal.interaction.util.TimerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import static com.vv.personal.diurnal.interaction.util.DiurnalUtil.generateOtpMail;

/**
 * @author Vivek
 * @since 07/03/21
 */
@Slf4j
@Secured("user")
@RequestMapping("/diurnal/mail")
@RestController("mail-controller")
//@SecurityScheme(securitySchemeName = "Basic Auth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class MailController {
    public static final String APPLICATION_X_PROTOBUF = "application/x-protobuf";
    private static final ResponsePrimitiveProto.ResponsePrimitive RESPOND_TRUE_BOOL = ResponsePrimitiveProto.ResponsePrimitive.newBuilder().setBoolResponse(true).build();
    private static final ResponsePrimitiveProto.ResponsePrimitive RESPOND_FALSE_BOOL = ResponsePrimitiveProto.ResponsePrimitive.newBuilder().setBoolResponse(false).build();

    @Inject
    BeanStore beanStore;
    @Inject
    OtpConfig otpConfig;
    @Inject
    MailOtpConfig mailOtpConfig;

    private static final ConcurrentHashMap<String, Integer> otpMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Timer> otpTimerMap = new ConcurrentHashMap<>();

    @PostMapping(value = "/generate/otp", produces = APPLICATION_X_PROTOBUF, consumes = APPLICATION_X_PROTOBUF)
    public ResponsePrimitiveProto.ResponsePrimitive generateOtp(@RequestBody OtpMailProto.OtpMail otpMail) {
        final String email = refineEmail(otpMail.getEmail());
        if (otpMap.containsKey(email)) {
            log.warn("OTP already sent for [{}]. Use that!", email);
            return RESPOND_TRUE_BOOL;
        }
        final Integer otp = DiurnalUtil.generateOtp(otpConfig.getOtpStartRangeForMail(), otpConfig.getOtpEndRangeForMail());
        Timer timer = beanStore.generateTimer();
        insertOtpAndTimerInMap(email, otp, timer);

        log.info("Initiating mail generation for OTP");
        String mailBody = String.format(mailOtpConfig.body(), mailOtpConfig.timeoutMinutes(), otp);
        if (beanStore.emailSender().sendOtpMessage(beanStore.diurnalMailMessage(), email, mailOtpConfig.title(), mailBody)) {
            TimerUtil.scheduleTimer(timer, TimerUtil.generateTimedTask(this::removeOtpAndTimerFromMap, email),
                    mailOtpConfig.timeoutMinutes() * 60L);
            return RESPOND_TRUE_BOOL;
        } else {
            log.warn("Failed to generate mail-otp for [{}]", otpMail);
            removeOtpAndTimerFromMap(email);
        }
        return RESPOND_FALSE_BOOL;
    }

    @Secured("user")
    @GetMapping("/generate/manual/otp")
    public Boolean generateOtpManually(@RequestParam String rxEmail) {
        return generateOtp(generateOtpMail(rxEmail))
                .getBoolResponse();
    }

    @PostMapping(value = "/verify/otp", produces = APPLICATION_X_PROTOBUF, consumes = APPLICATION_X_PROTOBUF)
    public ResponsePrimitiveProto.ResponsePrimitive verifyOtp(@RequestBody OtpMailProto.OtpMail otpMail) {
        final String email = refineEmail(otpMail.getEmail());
        if (otpMap.containsKey(email)) {
            if (otpMap.get(email).equals(otpMail.getOtp())) {
                log.info("User with [{}] verified!", email);
                removeOtpAndTimerFromMap(email);
                return RESPOND_TRUE_BOOL;
            } else {
                log.warn("Incorrect OTP entered by user");
            }
        } else {
            log.warn("Enquired mail's OTP is no longer in cache");
        }
        log.error("Denied verification!");
        return RESPOND_FALSE_BOOL;
    }

    @GetMapping("/verify/manual/otp")
    public Boolean verifyOtpManually(@RequestParam String email,
                                     @RequestParam Integer otp) {
        return verifyOtp(generateOtpMail(email, otp))
                .getBoolResponse();
    }

    @GetMapping("/map-otp")
    public String getOtpMap() {
        return otpMap.toString();
    }

    @GetMapping("/manual/clear/otp/all")
    public void clearAllOtp() {
        log.info("Initiating clear all otp effort");
        Set<String> emailsInEffect = new HashSet<>(otpMap.keySet());
        if (emailsInEffect.isEmpty()) return;
        emailsInEffect.forEach(this::removeOtpAndTimerFromMap);
        log.info("All otp cleared!");
    }

    @GetMapping("/manual/clear/otp/")
    public void clearOtp(@RequestParam String email) {
        log.info("Initiating clear otp for [{}]", email);
        removeOtpAndTimerFromMap(email);
    }

    private String refineEmail(String email) {
        return email.toLowerCase().trim();
    }

    private void insertOtpAndTimerInMap(String rxMail, Integer otp, Timer timer) {
        log.info("Generated OTP for [{}]: {}, and inserting in otp-map", rxMail, otp);
        otpMap.putIfAbsent(rxMail, otp);
        log.info("Generated Timer for [{}]: {} and inserting in otp-timer-map", rxMail, timer);
        otpTimerMap.putIfAbsent(rxMail, timer);
    }

    private Integer removeOtpAndTimerFromMap(String mailToRemove) {
        log.info("Removing [{}] from otp-timer-map", mailToRemove);
        Timer timer = otpTimerMap.get(mailToRemove);
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        otpTimerMap.remove(mailToRemove);
        log.info("Removing [{}] from otp-map", mailToRemove);
        return otpMap.remove(mailToRemove);
    }
}