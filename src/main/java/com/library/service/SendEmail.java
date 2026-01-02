package com.library.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {

    public void sendEmailTo(String emailOfClient, String code) {

        final String username = "nguyenquangphonglibrary@gmail.com";
        final String password = "xkqx loaq prhz uyci"; 

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));  
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailOfClient)
            );
            message.setSubject("Mã xác nhận OTP cập nhật tài khoản");
            message.setText("Dear you," +
                    "\n\n Ma xac nhan doi mat khau la: "+code);


            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();  
        }
    }
    public void sendEmailToPP(String emailOfClient, String nameReader) {

        final String username = "nguyenquangphonglibrary@gmail.com";
        final String password = "xkqx loaq prhz uyci";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailOfClient)
            );
            message.setSubject("THông báo quá hạn mượn sách của thư viện!!!");
            message.setText("Dear " + nameReader +", " +
                    "\n\n Bạn đã quá hạn mượn sách, yêu cầu độc giả gửi trả lại sách cho thư viện, đừng hòng thoát khỏi thư viện này");


            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
