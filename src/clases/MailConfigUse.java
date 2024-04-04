package clases;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailConfigUse {
    

    public void EnviarMail(String user, String passwd) {
        try {
            Properties smtpProps = new Properties();
            smtpProps.setProperty("mail.smtp.host", "smtp.gmail.com");
            smtpProps.setProperty("mail.smtp.auth", "true");
            smtpProps.setProperty("mail.smtp.starttls.enable", "true");
            smtpProps.setProperty("mail.smtp.port", "587");
            
            Session session = Session.getDefaultInstance(smtpProps);
            
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("adam22mgarcia@inslaferreria.cat "));
            message.setSubject("email desde java");
            message.setText("Funciona!!!!");
            
            Transport transport = session.getTransport("smtp");
            transport.connect(user, passwd);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
