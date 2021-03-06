/*
 * ThinkPalm, Technologies Pvt Ltd ("COMPANY") CONFIDENTIAL
 * Copyright (c) 2019-2020 [ThinkPalm, Inc.], All Rights Reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package com.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * To send mail
 * 
 * @author nasia.t
 *
 */
public class Mail {

	private static final ResourceBundle rb = ResourceBundle.getBundle("application");
	private static final String MAIL_FROM = rb.getString("mail.from");
	private static final String MAIL_TO = rb.getString("list.to.mail");
	private static final Logger LOGGER = LogManager.getLogger(Mail.class);
	ExcelRead readExcel = new ExcelRead();

	public void sendEmailWithTemplate(final String subject, final List<String> emailToAddresses, String templatePath,
			VelocityContext context) throws IOException {
		try {
			/* String host = "smtp.office365.com"; */
			String host = "mail.thinkpalm.info";
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			Velocity.init(p);
			Template template = Velocity.getTemplate(templatePath, "UTF-8");
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			try (FileWriter fwriter = new FileWriter("test-output/xray_report.html")) {
				fwriter.write(writer.toString());
			}

			// String path =
			// this.getClass().getClassLoader().getResource("templates/images/ato-bee-logo.jpg").getPath();
			// String fullPath = URLDecoder.decode(path, "UTF-8");
			// creates message part
			InputStream buff = null;
			buff = this.getClass().getClassLoader().getResourceAsStream("templates/images/a-to-be-logo.jpg");
			File targetFile = new File("src/main/resources/templates/images/a-to-be-logo1.jpg");
			FileUtils.copyInputStreamToFile(buff, targetFile);
			// DataSource fds = new FileDataSource(fullPath);
			DataSource fds = new FileDataSource(targetFile);
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(writer.toString(), "text/html");
			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			// DataSource fds1 = new FileDataSource(fullPath);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID", "<logo>");
			// String path1 =
			// this.getClass().getClassLoader().getResource("templates/images/banner.jpg").getPath();
			// String fullPath1 = URLDecoder.decode(path1, "UTF-8");
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			InputStream buff1 = null;
			buff1 = this.getClass().getClassLoader().getResourceAsStream("templates/images/banner.jpg");
			File targetFile1 = new File("src/main/resources/templates/images/banner1.jpg");
			FileUtils.copyInputStreamToFile(buff1, targetFile1);
			DataSource fds1 = new FileDataSource(targetFile1);
			messageBodyPart.setDataHandler(new DataHandler(fds1));
			messageBodyPart.setHeader("Content-ID", "<banner>");
			multipart.addBodyPart(messageBodyPart);
			Properties props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			/* props.put("mail.smtp.port", "587"); */
			props.put("mail.smtp.port", "25");
			props.put("mail.smtp.starttls.enable", "false");
			
			
props.put("mail.smtp.transport.protocol", "smtp"); 
props.put("mail.smtp.localhost", "192.168.1.158");
			
			
			
			Session session = Session.getDefaultInstance(props,

					new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(readExcel.getUserName(), readExcel.getPassword());
						}
					});
			String emails = null;
			// we create new message
			MimeMessage message = new MimeMessage(session);
			// set the from 'email address'
			message.setFrom(new InternetAddress(MAIL_FROM));
			// set email subject
			message.setSubject(subject);
			message.setContent(multipart);
			// message.setText(writer.toString(), "UTF-8", "html");
			// form all emails in a comma separated string
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (String email : emailToAddresses) {
				sb.append(email);
				i++;
				if (emailToAddresses.size() > i) {
					sb.append(",");
				}
			}
			emails = sb.toString();
			// you can set also CC or TO for recipient type
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emails));
			// send the email
			LOGGER.info("sending mails to " + emailToAddresses.toString());
			Transport.send(message);
			LOGGER.info("sent mails to " + emailToAddresses.toString());
		} catch (MessagingException e) {
			LOGGER.error("Exception inside MailUtil.sendEmail()" + e);
			LOGGER.info(e.getMessage());
		}
	}
}
