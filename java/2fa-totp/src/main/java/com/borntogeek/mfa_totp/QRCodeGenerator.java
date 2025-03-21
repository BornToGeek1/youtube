package com.borntogeek.mfa_totp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {
 
    public static void main(String[] args) throws WriterException, IOException {
        String secretKey = ""; // Replace with actual secret key
        String issuer = "MyApp"; // Your application name
        String accountName = "user@myapp.com"; // User's email or username

        String otpAuthUrl = String.format(
            "otpauth://totp/%s:%s?secret=%s&issuer=%s",
            issuer, accountName, secretKey, issuer
        );

        generateQRCode(otpAuthUrl, "qrcode.png", 300, 300);
        System.out.println("QR code succesfully generated");
    }
    
	public static void generateQRCode(String data, String filePath, int width, int height)
			throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
		Path path = new File(filePath).toPath();
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}
}
