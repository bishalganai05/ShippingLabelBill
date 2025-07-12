package com.bishal.slb.ShippingLabelBill.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bishal.slb.ShippingLabelBill.entity.ShippingDetails;

@Service
public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    private final EmailService emailService;
    private final SinglePDFGenerator singlePDFGenerator;

    private String excelPath;

    public ExcelService(EmailService emailService,
                        SinglePDFGenerator singlePDFGenerator,
                        @Value("${excel.file.path:ShippingData.xlsx}") String excelPath) {
        this.emailService = emailService;
        this.singlePDFGenerator = singlePDFGenerator;
        this.excelPath = excelPath;
    }

    public void setExcelPath(String path) {
        this.excelPath = path;
    }

    public void saveToExcel(ShippingDetails label) throws Exception {
        File file = new File(excelPath);
        Workbook workbook;
        Sheet sheet;

        if (file.exists() && file.length() == 0) {
            logger.warn("Empty Excel file at {}. Recreating...", excelPath);
            file.delete();
        }

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Shipping Labels");
            createHeaderRow(sheet);
        }

        int rowCount = sheet.getLastRowNum();
        Row row = sheet.createRow(rowCount + 1);

        row.createCell(0).setCellValue(label.getFromAddress());
        row.createCell(1).setCellValue(label.getFromPIN());
        row.createCell(2).setCellValue(label.getToAddress());
        row.createCell(3).setCellValue(label.getToPIN());
        row.createCell(4).setCellValue(label.getProductID());
        row.createCell(5).setCellValue(label.getProductName());
        row.createCell(6).setCellValue(label.getProductType());
        row.createCell(7).setCellValue(label.getEmailID());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            logger.info("Data saved to Excel for: {}", label.getEmailID());
        } finally {
            workbook.close();
        }
    }

    public List<ShippingDetails> readFromExcel() {
        List<ShippingDetails> detailsList = new ArrayList<>();
        File file = new File(excelPath);

        if (!file.exists()) {
            logger.warn("Excel file not found at path: {}", excelPath);
            return detailsList;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                ShippingDetails label = new ShippingDetails();
                label.setFromAddress(row.getCell(0).getStringCellValue());
                label.setFromPIN(row.getCell(1).getStringCellValue());
                label.setToAddress(row.getCell(2).getStringCellValue());
                label.setToPIN(row.getCell(3).getStringCellValue());
                label.setProductID(row.getCell(4).getStringCellValue());
                label.setProductName(row.getCell(5).getStringCellValue());
                label.setProductType(row.getCell(6).getStringCellValue());
                label.setEmailID(row.getCell(7).getStringCellValue());

                detailsList.add(label);
            }

        } 
        catch (Exception e) {
            logger.error("Failed to read from Excel file: {}", e.getMessage(), e);
        }

        return detailsList;
    }
    
    public void generatePdfFromExcel() {
        File file = new File(excelPath);
        if (!file.exists()) {
            logger.warn("Excel file not found: {}", excelPath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                ShippingDetails label = new ShippingDetails();
                label.setFromAddress(row.getCell(0).getStringCellValue());
                label.setFromPIN(row.getCell(1).getStringCellValue());
                label.setToAddress(row.getCell(2).getStringCellValue());
                label.setToPIN(row.getCell(3).getStringCellValue());
                label.setProductID(row.getCell(4).getStringCellValue());
                label.setProductName(row.getCell(5).getStringCellValue());
                label.setProductType(row.getCell(6).getStringCellValue());
                label.setEmailID(row.getCell(7).getStringCellValue());

                File pdfFile = singlePDFGenerator.createPdf(label);

                try (InputStream iStream = new FileInputStream(pdfFile)) {
                    emailService.sendMailWithAttachmentFile(
                        label.getEmailID(), "Spring Boot Application", "New Shipping Label", iStream
                    );
                    logger.info("Email sent to {}", label.getEmailID());
                } catch (Exception e) {
                    logger.error("Failed to send email to {}", label.getEmailID(), e);
                }
            }

        } 
        catch (Exception e) {
            logger.error("Error processing Excel for PDF generation", e);
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("From Address");
        header.createCell(1).setCellValue("From Pin");
        header.createCell(2).setCellValue("To Address");
        header.createCell(3).setCellValue("To Pin");
        header.createCell(4).setCellValue("Product Id");
        header.createCell(5).setCellValue("Product Name");
        header.createCell(6).setCellValue("Product Type");
        header.createCell(7).setCellValue("Email ID");
    }
}
