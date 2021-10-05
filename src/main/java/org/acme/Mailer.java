package org.acme;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

    private String from;
    private String to;
    private String header = "New Binance orders";
    private String text;

    public Mailer(String from, String to, String header, String text) {
        this.from = from;
        this.to = to;
        this.header = header;
        this.text = text;
    }

    public void sendMessage(){

        final String password = "Spartak.1998";
        final String userName = "richardzaduban.development@gmail.com";
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");     
        props.setProperty("mail.host", "smtp.gmail.com");  
        props.put("mail.smtp.auth", "true");  
        props.put("mail.smtp.port", "587");  
        props.put("mail.debug", "true");  
        props.put("mail.smtp.starttls.enable", "true");
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getDefaultInstance(props,authenticator);

        try{  
            MimeMessage message = new MimeMessage(session);  
            message.setFrom(new InternetAddress(from));  
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
            message.setSubject(header);  
            message.setText(text);  
     
            // Send message 
            //Transport transport = session.getTransport("smtp");
            //transport.connect();
            Transport.send(message);
            //transport.close();
            System.out.println("message sent successfully...."); 
     
         }catch (MessagingException e) {e.printStackTrace();}  

    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
