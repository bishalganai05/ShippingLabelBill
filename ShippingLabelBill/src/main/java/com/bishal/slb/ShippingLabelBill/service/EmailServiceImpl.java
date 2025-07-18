package com.bishal.slb.ShippingLabelBill.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private final JavaMailSender javaMailSender;
	private final String from;

	public EmailServiceImpl(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String from) {
		this.javaMailSender = javaMailSender;
		this.from = from;
	}

	@Override
	public void sendMail(String to, String subject, String message) {
		try {
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(to);
			mail.setSubject(subject);
			mail.setText(message);
			mail.setFrom(from);
			javaMailSender.send(mail);
			logger.info("Plain text email sent to {}", to);
		} catch (Exception e) {
			logger.error("Failed to send plain text email to {}: {}", to, e.getMessage(), e);
		}
	}

	@Override
	public void sendMailWithHtml(String to, String subject, String htmlContent) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setFrom(from);
			helper.setText(htmlContent, true); // true = HTML
			javaMailSender.send(message);
			logger.info("HTML email sent to {}", to);
		} catch (MessagingException e) {
			logger.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
		}
	}

	@Override
	public void sendMailWithAttachment(String to, String subject, String message, File file) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message);
			helper.setFrom(from);
			helper.addAttachment(file.getName(), new FileSystemResource(file));
			javaMailSender.send(mimeMessage);
			logger.info("Email with attachment sent to {}", to);
		} catch (MessagingException e) {
			logger.error("Failed to send email with attachment to {}: {}", to, e.getMessage(), e);
		}
	}

	@Override
	public void sendMailWithAttachmentFile(String to, String subject, String message, InputStream inputStream) {
	    File tempFile = null;
	    try {

	        tempFile = File.createTempFile("attachment-", ".pdf");

	        try (inputStream) {
	            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	        }

	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(message);
	        helper.setFrom(from);

	        FileSystemResource fileResource = new FileSystemResource(tempFile);
	        helper.addAttachment("ShippingLabel.pdf", fileResource);

	        javaMailSender.send(mimeMessage);
	        logger.info("Email with PDF attachment sent to {}", to);

	    } 
	    catch (MessagingException | IOException e) {
	        logger.error("Error sending email with attachment to {}: {}", to, e.getMessage());
	    } 
	    finally {

	        if (tempFile != null && tempFile.exists()) {
	            tempFile.delete();
	        }
	    }
	}

}
