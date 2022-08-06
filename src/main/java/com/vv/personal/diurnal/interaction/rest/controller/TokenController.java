package com.vv.personal.diurnal.interaction.rest.controller;

import com.vv.personal.diurnal.artifactory.generated.TokenProto;
import com.vv.personal.diurnal.interaction.service.UserMappingService;
import com.vv.personal.diurnal.interaction.service.config.BeanStore;
import com.vv.personal.diurnal.interaction.service.config.TokenConfig;
import com.vv.personal.diurnal.interaction.util.TimerUtil;
import com.vv.personal.diurnal.interaction.util.TokenUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vivek
 * @since 07/03/21
 */
@Slf4j
@AllArgsConstructor
@RequestMapping("/diurnal/token")
@RestController("token-controller")
public class TokenController {
    private static final String APPLICATION_X_PROTOBUF = "application/x-protobuf";
    private static final String TEXT_PLAIN = "text/plain";

    private static final TokenProto.TokenShell RESPOND_TOKEN_VERIFIED = TokenProto.TokenShell.newBuilder().setToken("0").build();
    private static final TokenProto.TokenShell RESPOND_TOKEN_INCORRECT = TokenProto.TokenShell.newBuilder().setToken("-1").build();
    private static final TokenProto.TokenShell RESPOND_TOKEN_EXPIRED = TokenProto.TokenShell.newBuilder().setToken("-11").build();
    private static final TokenProto.TokenShell RESPOND_TOKEN_ALREADY_GENERATED = TokenProto.TokenShell.newBuilder().setToken("-12").build();
    private static final TokenProto.TokenShell RESPOND_TOKEN_FOR_USER_NON_EXISTENT = TokenProto.TokenShell.newBuilder().setToken("-13").build();
    private static final ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Timer> tokenTimerMap = new ConcurrentHashMap<>();
    private final BeanStore beanStore;
    private final TokenConfig tokenConfig;
    private final UserMappingService userMappingService;

    @PostMapping(value = "/generate", produces = APPLICATION_X_PROTOBUF, consumes = APPLICATION_X_PROTOBUF)
    public TokenProto.TokenShell generateToken(@RequestBody TokenProto.TokenShell tokenShell) {
        final String email = refineEmail(tokenShell.getEmail());
        if (!userMappingService.isUserExistent(email)) {
            log.warn("No user record for {}, thus no token generation", email);
            return RESPOND_TOKEN_FOR_USER_NON_EXISTENT;
        }
        if (tokenMap.containsKey(email)) {
            log.warn("Token already in use for [{}]!", email);
            return RESPOND_TOKEN_ALREADY_GENERATED;
        }
        final String token = TokenUtil.generateToken(tokenConfig.length());
        Timer timer = beanStore.generateTimer();
        insertTokenAndTimerInMap(email, token, timer);
        TimerUtil.scheduleTimer(timer, TimerUtil.generateTimedTask(this::removeTokenAndTimerFromMap, email), tokenConfig.expiry());
        return TokenUtil.generateResponse(email, token);
    }

    @Secured("user")
    @GetMapping(value = "/generate/manual", produces = TEXT_PLAIN)
    public String generateTokenManually(@RequestParam String rxEmail) {
        return generateToken(TokenUtil.generateTokenShell(rxEmail)).getToken();
    }

    @PostMapping(value = "/verify", produces = APPLICATION_X_PROTOBUF, consumes = APPLICATION_X_PROTOBUF)
    public TokenProto.TokenShell verifyToken(@RequestBody TokenProto.TokenShell tokenShell) {
        final String email = refineEmail(tokenShell.getEmail());
        if (tokenMap.containsKey(email)) {
            if (tokenMap.get(email).equals(tokenShell.getToken())) {
                log.info("User with [{}] verified!", email);
                return RESPOND_TOKEN_VERIFIED;
            } else {
                log.warn("Incorrect Token sent for {}", email);
            }
        } else {
            log.warn("Token expired / non-existent for {}", email);
            return RESPOND_TOKEN_EXPIRED;
        }
        log.warn("Failed token verification for {}!", email);
        return RESPOND_TOKEN_INCORRECT;
    }

    @Secured("user")
    @GetMapping(value = "/verify/manual", produces = TEXT_PLAIN)
    public String verifyTokenManually(@RequestParam String email, @RequestParam String token) {
        return verifyToken(TokenUtil.generateTokenShell(email, token)).getToken();
    }

    @Secured("user")
    @GetMapping(value = "/map-token", produces = TEXT_PLAIN)
    public String getTokenMap() {
        return tokenMap.toString();
    }

    @Secured("user")
    @GetMapping("/manual/clear/token/all")
    public void clearAllToken() {
        log.info("Initiating clear all token effort");
        Set<String> emailsInEffect = new HashSet<>(tokenMap.keySet());
        if (emailsInEffect.isEmpty()) return;
        emailsInEffect.forEach(this::removeTokenAndTimerFromMap);
        log.info("All token cleared!");
    }

    @Secured("user")
    @GetMapping("/manual/clear/token/")
    public void clearToken(@RequestParam String email) {
        log.info("Initiating clear token for [{}]", email);
        removeTokenAndTimerFromMap(email);
    }

    private String refineEmail(String email) {
        return email.toLowerCase().trim();
    }

    private void insertTokenAndTimerInMap(String rxMail, String token, Timer timer) {
        log.info("Generated Token for [{}]: {}, and inserting in token-map", rxMail, token);
        tokenMap.putIfAbsent(rxMail, token);
        log.info("Generated Timer for [{}]: {} and inserting in token-timer-map", rxMail, timer);
        tokenTimerMap.putIfAbsent(rxMail, timer);
    }

    private String removeTokenAndTimerFromMap(String mailToRemove) {
        log.info("Removing [{}] from token-timer-map", mailToRemove);
        Timer timer = tokenTimerMap.get(mailToRemove);
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        tokenTimerMap.remove(mailToRemove);
        log.info("Removing [{}] from token-map", mailToRemove);
        return tokenMap.remove(mailToRemove);
    }
}