package com.bishal.slb.ShippingLabelBill.helper;

import org.springframework.stereotype.Component;

import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;

@Component
public class PdfHelper {

    public Cell getHeaderTextCell(String text) {
        return new Cell()
                .add(text)
                .setBold()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
    }

    public Cell getHeaderTextCellValue(String text) {
        return new Cell()
                .add(text)
                .setBold()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    public Cell getFromAndToAddress(String text) {
        return new Cell()
                .add(text)
                .setFontSize(12f)
                .setBold()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }

    public Cell getCell10Left(String text, boolean isBold) {
        Cell cell = new Cell()
                .add(text)
                .setFontSize(10f)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);

        return isBold ? cell.setBold() : cell;
    }
}
