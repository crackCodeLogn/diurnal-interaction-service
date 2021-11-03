package com.vv.personal.diurnal.interaction.util;

import org.junit.jupiter.api.Test;

import static com.vv.personal.diurnal.interaction.util.DiurnalUtil.generateOtp;

/**
 * @author Vivek
 * @since 07/03/21
 */
class DiurnalUtilTest {

    @Test
    void testGenerateOtp() {
        System.out.println(generateOtp(10000000, 99999999));
    }

    @Test
    void test() {
        System.out.println((int) Math.pow(10, 7));
        System.out.println((int) Math.pow(10, 8) - 1);
    }
}