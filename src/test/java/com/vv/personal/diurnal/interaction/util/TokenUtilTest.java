package com.vv.personal.diurnal.interaction.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vivek
 * @since 06/08/22
 */
class TokenUtilTest {

    @Test
    void testGenerateToken() {
        String result = TokenUtil.generateToken(45);
        System.out.println(result);
        assertThat(result)
                .isNotBlank()
                .containsPattern("[0-9a-zA-Z]{45}");
    }
}