package com.vv.personal.diurnal.interaction.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.ToIntFunction;

/**
 * @author Vivek
 * @since 07/03/21
 */
@Slf4j
public class TimerUtil {

    private TimerUtil() {
    }

    public static void scheduleTimer(Timer timer, TimerTask timerTask, long seconds) {
        timer.schedule(timerTask, seconds * 1000);
        log.info("Scheduled timer task for otp clearing: {} for {} seconds", timer, seconds);
    }

    public static TimerTask generateTimedTask(ToIntFunction<String> functionToRunPostTimeout, String input) {
        return new TimerTask() {
            @Override
            public void run() {
                functionToRunPostTimeout.applyAsInt(input);
            }
        };
    }
}