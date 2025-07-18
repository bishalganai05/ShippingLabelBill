package com.bishal.slb.ShippingLabelBill.service;


import java.io.File;
import java.io.InputStream;

public interface EmailService {

    void sendMail(String to, String subject, String message);

    void sendMailWithHtml(String to, String subject, String htmlContent);

    void sendMailWithAttachment(String to, String subject, String message, File file);

    void sendMailWithAttachmentFile(String to, String subject, String message, InputStream file);
}
