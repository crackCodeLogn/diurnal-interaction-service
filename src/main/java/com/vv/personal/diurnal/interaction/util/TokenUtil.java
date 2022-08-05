package com.vv.personal.diurnal.interaction.util;

import com.vv.personal.diurnal.artifactory.generated.TokenProto;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Vivek
 * @since 07/03/21
 */
public class TokenUtil {

    private TokenUtil() {
    }

    public static String generateToken(Integer tokenLength) {
        return RandomStringUtils.random(tokenLength, true, true);
    }

    public static TokenProto.TokenShell generateResponse(String email, String token) {
        return TokenProto.TokenShell.newBuilder()
                .setEmail(email)
                .setToken(token)
                .build();
    }

    public static TokenProto.TokenShell generateTokenShell(String rxEmail) {
        return TokenProto.TokenShell.newBuilder()
                .setEmail(rxEmail)
                .build();
    }

    public static TokenProto.TokenShell generateTokenShell(String rxEmail, String token) {
        return TokenProto.TokenShell.newBuilder()
                .setEmail(rxEmail)
                .setToken(token)
                .build();
    }
}