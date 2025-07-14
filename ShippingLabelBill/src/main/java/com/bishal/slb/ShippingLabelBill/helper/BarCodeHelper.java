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
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Component
public class BarCodeHelper {

    private static final Logger logger = LoggerFactory.getLogger(BarCodeHelper.class);

    public byte[] getBarCodeImage(String text) {
        if (text == null || text.trim().isEmpty()) {
            logger.warn("Barcode generation skipped: Empty input");
            return null;
        }

        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            Code128Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, 300, 100);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Failed to generate barcode for input '{}': {}", text, e.getMessage(), e);
            return null;
        }
    }
}


