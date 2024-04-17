package client;

/**
 *
 * @author garci
 */
import clases.Mail;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.mail.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public List<Mail> ConseguirInbox(int startIndex, int endIndex) {
        List<Mail> mails = new ArrayList();
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.host", "imap.gmail.com");
            properties.put("mail.imaps.port", "993");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Store store = session.getStore("imaps");
            store.connect();

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] mensajes = folder.getMessages(startIndex + 1, endIndex); // +1 because index starts from 1

            for (Message mensaje : mensajes) {
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                m.setContingut(mensaje.getContent().toString());
                mails.add(m);
            }

            folder.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println(ex.getMessage());
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return mails;
    }

    public List<Mail> ConseguirEnviados() {
        List<Mail> mails = new ArrayList();
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.host", "imap.gmail.com");
            properties.put("mail.imaps.port", "993");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Store store = session.getStore("imaps");
            store.connect();

            Folder folder = store.getFolder("[Gmail]/Enviats");
            folder.open(Folder.READ_ONLY);

            Message[] mensajes = folder.getMessages();

            for (Message mensaje : mensajes) {
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                m.setContingut(mensaje.getContent().toString());
                mails.add(m);
            }

            folder.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println(ex.getMessage());
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return mails;
    }

    public List<Mail> ConseguirEsborranys() {
        List<Mail> mails = new ArrayList();
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.host", "imap.gmail.com");
            properties.put("mail.imaps.port", "993");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Store store = session.getStore("imaps");
            store.connect();

            Folder folder = store.getFolder("[Gmail]/Esborranys");
            folder.open(Folder.READ_ONLY);

            Message[] mensajes = folder.getMessages();

            for (Message mensaje : mensajes) {
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                m.setContingut(mensaje.getContent().toString());
                mails.add(m);
            }

            folder.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println(ex.getMessage());
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return mails;
    }

    public List<Mail> ConseguirCorreuBrossa() {
        List<Mail> mails = new ArrayList();
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");
            properties.put("mail.imaps.host", "imap.gmail.com");
            properties.put("mail.imaps.port", "993");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Store store = session.getStore("imaps");
            store.connect();

            Folder folder = store.getFolder("[Gmail]/Correu brossa");
            folder.open(Folder.READ_ONLY);

            Message[] mensajes = folder.getMessages();

            for (Message mensaje : mensajes) {
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                m.setContingut(mensaje.getContent().toString());
                mails.add(m);
            }

            folder.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println(ex.getMessage());
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return mails;
    }

}
