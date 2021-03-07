package com.vv.personal.diurnal.interaction.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.vv.personal.diurnal.interaction.util.DiurnalUtil.generateOtp;
import static org.junit.Assert.*;

/**
 * @author Vivek
 * @since 07/03/21
 */
@RunWith(JUnit4.class)
public class DiurnalUtilTest {

    @Test
    public void testGenerateOtp() {
        System.out.println(generateOtp(10000000, 99999999));
    }

    @Test
    public void test() {
        System.out.println((int) Math.pow(10, 7));
        System.out.println((int) Math.pow(10, 8) - 1);
    }
}