package com.borntogeek.mfa_totp;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.time.Instant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

public class TOTPValidator {
	
    private static final String ALGORITHM = "HmacSHA1"; // Can be HmacSHA256 or HmacSHA512
    private static final int TOTP_INTERVAL = 30; // 30-second time step
    
    public static void main(String[] args) throws GeneralSecurityException {
        String secretKey = ""; // Replace with user's secret key
        String userInputOTP = ""; // Replace with actual user-entered OTP

        boolean isValid = validateOTP(secretKey, userInputOTP);
        System.out.println("Is OTP valid? " + isValid);
    }
    
    public static boolean validateOTP(String secretKey, String otp) throws GeneralSecurityException {
        long timeIndex = Instant.now().getEpochSecond() / TOTP_INTERVAL;
        return otp.equals(generateTOTP(secretKey, timeIndex));
    }

    public static String generateTOTP(String base32Secret, long timeIndex) throws GeneralSecurityException {
        Base32 base32 = new Base32();
        byte[] key = base32.decode(base32Secret);

        byte[] timeBytes = ByteBuffer.allocate(8).putLong(timeIndex).array();
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(key, ALGORITHM));
        byte[] hash = mac.doFinal(timeBytes);

        int offset = hash[hash.length - 1] & 0x0F;
        int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16) |
                     ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);

        return String.format("%06d", binary % 1_000_000);
    }
}
