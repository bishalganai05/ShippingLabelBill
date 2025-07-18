package com.bishal.slb.ShippingLabelBill.controller;


import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bishal.slb.ShippingLabelBill.entity.ShippingDetails;
import com.bishal.slb.ShippingLabelBill.service.ExcelService;
import com.bishal.slb.ShippingLabelBill.service.SinglePDFGenerator;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final ExcelService excelService;
    private final SinglePDFGenerator singlePDFGenerator;

    public ShippingController(ExcelService excelService, SinglePDFGenerator singlePDFGenerator) {
        this.excelService = excelService;
		this.singlePDFGenerator = singlePDFGenerator;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("Shipping API is working!");
    }

    
    @GetMapping("/excel-data")
    public ResponseEntity<?> getExcelData() {
        try {
            List<ShippingDetails> data = excelService.readFromExcel();
            if (data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found in Excel file.");
            }
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading Excel data: " + e.getMessage());
        }
    }
    @PostMapping("/submit-to-excel")
    public ResponseEntity<String> saveDetails(@RequestBody ShippingDetails label) {
        try {
           excelService.saveToExcel(label);
            return ResponseEntity.ok("Data saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving data: " + e.getMessage());
        }
    }

    @PostMapping("/create-pdf")
    public ResponseEntity<byte[]> createPdf(@RequestBody ShippingDetails request) {
        try {
            File pdfFile = singlePDFGenerator.createPdf(request);
            byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename(pdfFile.getName())
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @PostMapping("/send-mail")
    public ResponseEntity<String> generatePdf(@RequestBody ShippingDetails request) {
        try {
            excelService.generatePdfFromExcel(request.getEmailID());
            return ResponseEntity.ok("PDF generated and sent to email: " + request.getEmailID());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating/sending PDF: " + e.getMessage());
        }
    }
    
    
}
