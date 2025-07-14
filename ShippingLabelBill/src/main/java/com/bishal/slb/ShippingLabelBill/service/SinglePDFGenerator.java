package com.bishal.slb.ShippingLabelBill.service;


import java.io.File;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.bishal.slb.ShippingLabelBill.entity.ShippingDetails;
import com.bishal.slb.ShippingLabelBill.helper.BarCodeHelper;
import com.bishal.slb.ShippingLabelBill.helper.PdfHelper;
import com.bishal.slb.ShippingLabelBill.helper.QRCodeHelper;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

@Service
public class SinglePDFGenerator {

    private final PdfHelper pdfHelper;
    private final BarCodeHelper barCodeHelper;
    private final QRCodeHelper qrCodeHelper;

    public SinglePDFGenerator(PdfHelper pdfHelper, BarCodeHelper barCodeHelper, QRCodeHelper qrCodeHelper) {
        this.pdfHelper = pdfHelper;
        this.barCodeHelper = barCodeHelper;
        this.qrCodeHelper = qrCodeHelper;
    }

    public File createPdf(ShippingDetails label) throws Exception {
        String path = "label_" + label.getProductID() + ".pdf";
        File pdfFile = new File(path);
        if (pdfFile.exists()) {
            pdfFile.delete();
        }

        try (PdfWriter pdfWriter = new PdfWriter(path);
             PdfDocument pdfDocument = new PdfDocument(pdfWriter);
             Document document = new Document(pdfDocument)) {

            pdfDocument.setDefaultPageSize(PageSize.A4);

            float twoCol = 285f;
            float twoCol150 = twoCol + 150f;
            float threeCol = 190f;
            float[] fullWidth = { threeCol * 3 };
            float[] twoColumnWidth = { twoCol150, twoCol };
            float[] oneColWidth = { twoCol150 };

            Paragraph newline = new Paragraph("\n");

            Random random = new Random();
            String billNumber = String.valueOf(random.nextInt(1000000));
            String todayString = String.valueOf(LocalDate.now());

            // Header table
            Table headerTable = new Table(twoColumnWidth);
            headerTable.addCell(new Cell().add("Shipping Bill")
                .setFontSize(20f).setBorder(Border.NO_BORDER).setBold());

            Table nestedTable = new Table(new float[] { twoCol / 2, twoCol / 2 });
            nestedTable.addCell(pdfHelper.getHeaderTextCell("Bill Number: "));
            nestedTable.addCell(pdfHelper.getHeaderTextCellValue(billNumber));
            nestedTable.addCell(pdfHelper.getHeaderTextCell("Bill Date: "));
            nestedTable.addCell(pdfHelper.getHeaderTextCellValue(todayString));

            headerTable.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));
            document.add(headerTable);
            document.add(newline);

            // Divider line
            document.add(new Table(fullWidth).setBorder(new SolidBorder(Color.GRAY, 2f)));
            document.add(newline);

            // Address labels
            Table addressLabelTable = new Table(twoColumnWidth);
            addressLabelTable.addCell(pdfHelper.getFromAndToAddress("FROM ADDRESS:"));
            addressLabelTable.addCell(pdfHelper.getFromAndToAddress("TO ADDRESS:"));
            document.add(addressLabelTable.setMarginBottom(12f));

            // Address values
            Table addressDetailTable = new Table(twoColumnWidth);
            addressDetailTable.addCell(pdfHelper.getCell10Left("FROM ADDRESS", true));
            addressDetailTable.addCell(pdfHelper.getCell10Left("TO ADDRESS", true));
            addressDetailTable.addCell(pdfHelper.getCell10Left(label.getFromAddress(), false));
            addressDetailTable.addCell(pdfHelper.getCell10Left(label.getToAddress(), false));
            addressDetailTable.addCell(pdfHelper.getCell10Left("ZIP CODE", true));
            addressDetailTable.addCell(pdfHelper.getCell10Left("ZIP CODE", true));
            addressDetailTable.addCell(pdfHelper.getCell10Left(label.getFromPIN(), false));
            addressDetailTable.addCell(pdfHelper.getCell10Left(label.getToPIN(), false));
            document.add(addressDetailTable);
            document.add(newline);

            // Product header
            Table productHeaderTable = new Table(twoColumnWidth);
            Cell productHeaderCell = new Cell(1, 2)
                .add(pdfHelper.getFromAndToAddress("PRODUCT DETAILS"))
                .setBorder(Border.NO_BORDER);
            productHeaderTable.addCell(productHeaderCell);
            document.add(productHeaderTable.setMarginBottom(12f));

            // Product values
            Table productDetailTable = new Table(oneColWidth);
            productDetailTable.addCell(pdfHelper.getCell10Left("PRODUCT ID", true));
            productDetailTable.addCell(pdfHelper.getCell10Left(label.getProductID(), false));
            productDetailTable.addCell(pdfHelper.getCell10Left("PRODUCT NAME", true));
            productDetailTable.addCell(pdfHelper.getCell10Left(label.getProductName(), false));
            productDetailTable.addCell(pdfHelper.getCell10Left("PRODUCT TYPE", true));
            productDetailTable.addCell(pdfHelper.getCell10Left(label.getProductType(), false));
            document.add(productDetailTable);
            document.add(newline);

            // Dashed divider
            Table dashedDivider = new Table(fullWidth);
            dashedDivider.setBorder(new DashedBorder(Color.GRAY, 0.5f));
            document.add(dashedDivider.setMarginBottom(10f));

            // Barcode
            byte[] barcode = barCodeHelper.getBarCodeImage(billNumber);
            Image barcodeImage = new Image(com.itextpdf.io.image.ImageDataFactory.create(barcode));
            document.add(new Paragraph("BAR CODE"));
            document.add(barcodeImage);
            document.add(newline);

            // QR Code
            byte[] qrcode = qrCodeHelper.getQRCodeImage(
                "FROM:" + label.getFromAddress() + "\nTO:" + label.getToAddress() +
                "\nProduct ID:" + label.getProductID() + "\nProduct Name:" + label.getProductName()
            );
            Image qrcodeImage = new Image(com.itextpdf.io.image.ImageDataFactory.create(qrcode));
            document.add(new Paragraph("QR CODE"));
            document.add(qrcodeImage);
        }

        System.out.println("PDF generated successfully");
        return pdfFile;
    }
}
