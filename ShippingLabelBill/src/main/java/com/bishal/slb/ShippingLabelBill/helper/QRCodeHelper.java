package com.bishal.slb.ShippingLabelBill.helper;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Component
public class QRCodeHelper {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeHelper.class);

    public byte[] getQRCodeImage(String text) {
        if (text == null || text.trim().isEmpty()) {
            logger.warn("QR code generation skipped: Empty input");
            return null;
        }

        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Failed to generate QR code for input '{}': {}", text, e.getMessage(), e);
            return null;
        }
    }
}


