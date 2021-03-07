package com.vv.personal.diurnal.interaction.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Vivek
 * @since 07/03/21
 */
public class TimerUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(TimerUtil.class);

    public static void scheduleTimer(Timer timer, TimerTask timerTask, long seconds) {
        timer.schedule(timerTask, seconds * 1000);
        LOGGER.info("Scheduled timer task for otp clearing: {} for {} seconds", timer, seconds);
    }

    public static TimerTask generateTimedTask(Function<String, Integer> functionToRunPostTimeout, String input) {
        return new TimerTask() {
            @Override
            public void run() {
                functionToRunPostTimeout.apply(input);
            }
        };
    }
}
