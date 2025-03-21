package com.borntogeek.mfa_totp;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;

public class MFASecretGenerator {


    public static void main(String[] args) {
        String secretKey = generateSecretKey();
        System.out.println("Generated secret key: \n" + secretKey);
    }
    
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20]; // 20 bytes for a strong secret
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes).replace("=", "");
    }
}
