package client;

/**
 *
 * @author garci
 */
import javax.mail.*;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailClientConnection {

    private static String USERNAME;
    private static String PASSWORD;

    public EmailClientConnection(String USERNAME, String PASSWORD) {
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public EmailClientConnection() {
    }
    
    
    
    

    public void ConectionImap() {
        // Conectar al servidor IMAP
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", USERNAME, PASSWORD);

            System.out.println("Conectado al servidor Imap");
        } catch (AuthenticationFailedException ex) {
            System.out.println("Error de autenticación: Habilita la opción de 'Acceso de aplicaciones menos seguras' o comprueba la contraseña.");
            ex.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("Error al conectar al servidor IMAP.");
            e.printStackTrace();
        }
    }

    public void ConectionSMTP() {
        // Conectar al servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        System.out.println("Conectado al servidor SMTP");
    }

    public void CloseConectionImap() {
        // Desconectar del servidor IMAP
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", USERNAME, PASSWORD);
            store.close();
            System.out.println("Desconectado del servidor IMAP");
        } catch (AuthenticationFailedException ex) {
            System.out.println("Error de autenticación: Habilita la opción de 'Acceso de aplicaciones menos seguras' o comprueba la contraseña.");
            ex.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("Error al conectar al servidor IMAP.");
            e.printStackTrace();
        }
    }

    public void CloseConectionSMTP() {
        // Desconectar del servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Transport transport = session.getTransport("smtp");
            transport.connect(USERNAME, PASSWORD);
            transport.close();
            System.out.println("Desconectado del servidor SMTP");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public void EnviarMail(String asunto, String destinatario, String contenido) {
        try {
            Properties smtpProps = new Properties();
            smtpProps.setProperty("mail.smtp.host", "smtp.gmail.com");
            smtpProps.setProperty("mail.smtp.auth", "true");
            smtpProps.setProperty("mail.smtp.starttls.enable", "true");
            smtpProps.setProperty("mail.smtp.port", "587");
            
            Session session = Session.getDefaultInstance(smtpProps);
            
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(destinatario));
            message.setSubject(asunto);
            message.setText(contenido);
            
            Transport transport = session.getTransport("smtp");
            transport.connect(USERNAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
