package client;

/**
 *
 * @author garci
 */
import clases.Mail;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.mail.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailClientConnection {

    private static String USERNAME;
    private static String PASSWORD;
    private List<File> archivosAdjuntos;
    private static Mail m;
    private static String bandejaA;
    private static int inicia, acaba;

    public EmailClientConnection(String USERNAME, String PASSWORD) {
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.archivosAdjuntos = new ArrayList();
    }

    public EmailClientConnection() {
        this.archivosAdjuntos = new ArrayList();
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

    public void EnviarMail(String asunto, String destinatarios, String cc, String bcc, String contenido) {
        try {
            Properties smtpProps = new Properties();
            smtpProps.setProperty("mail.smtp.host", "smtp.gmail.com");
            smtpProps.setProperty("mail.smtp.auth", "true");
            smtpProps.setProperty("mail.smtp.starttls.enable", "true");
            smtpProps.setProperty("mail.smtp.port", "587");

            Session session = Session.getInstance(smtpProps, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            MimeMessage message = new MimeMessage(session);

            // Añadir destinatarios al campo "Para"
            String[] destinatariosArray = destinatarios.split(",");
            for (String destinatario : destinatariosArray) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario.trim()));
            }

            // Añadir destinatarios al campo "Cc"
            if (cc != null && !cc.isBlank()) {
                String[] ccArray = cc.split(",");
                for (String ccAddress : ccArray) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress.trim()));
                }
            }

            message.setSubject(asunto);

            // Añadir destinatarios al campo "Bcc"
            if (bcc != null && !bcc.isBlank()) {
                String[] bccArray = bcc.split(",");
                for (String bccAddress : bccArray) {
                    message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccAddress.trim()));
                }
            }

            // Configurar el contenido del mensaje (texto y posiblemente adjuntos)
            MimeMultipart multipart = new MimeMultipart();

            // Añadir el contenido del correo (texto)
            MimeBodyPart textoPart = new MimeBodyPart();
            textoPart.setText(contenido);
            multipart.addBodyPart(textoPart);

            // Adjuntar archivos si hay algún archivo adjunto
            // Adjuntar archivos si hay algún archivo adjunto
            if (archivosAdjuntos != null && !archivosAdjuntos.isEmpty()) {
                for (File adjunto : archivosAdjuntos) {
                    adjuntarArchivo(adjunto, multipart);
                }
            }

            // Asignar el contenido mixto al mensaje
            message.setContent(multipart);

            // Enviar el mensaje
            Transport.send(message);
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
            
            Message[] mensajes;
            
            // Obtener los mensajes en orden inverso
            if(folder.getMessageCount() < endIndex){
                mensajes = folder.getMessages();
            } else {
                mensajes = folder.getMessages(folder.getMessageCount() - endIndex, folder.getMessageCount() - startIndex);
            }

            // Agregar los mensajes a la lista en orden inverso
            for (int i = mensajes.length - 1; i >= 0; i--) {
                Message mensaje = mensajes[i];
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                String contenido = "";
                Object mensajeContenido = mensaje.getContent();
                if (mensajeContenido instanceof String) {
                    contenido = (String) mensajeContenido;
                } else if (mensajeContenido instanceof Multipart) {
                    Multipart multipart = (Multipart) mensajeContenido;
                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart parte = multipart.getBodyPart(j);
                        if (parte.isMimeType("text/plain")) {
                            contenido += parte.getContent().toString();
                        }
                    }
                }
                m.setContingut(contenido);
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

    public List<Mail> ConseguirEnviados(int startIndex, int endIndex) {
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
            
            Message[] mensajes;

            if(folder.getMessageCount() < endIndex){
                mensajes = folder.getMessages();
            } else {
                mensajes = folder.getMessages(folder.getMessageCount() - endIndex, folder.getMessageCount() - startIndex);
            }

            for (Message mensaje : mensajes) {
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                String contenido = "";
                Object mensajeContenido = mensaje.getContent();
                if (mensajeContenido instanceof String) {
                    contenido = (String) mensajeContenido;
                } else if (mensajeContenido instanceof Multipart) {
                    Multipart multipart = (Multipart) mensajeContenido;
                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart parte = multipart.getBodyPart(j);
                        if (parte.isMimeType("text/plain")) {
                            contenido += parte.getContent().toString();
                        }
                    }
                }
                m.setContingut(contenido);
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

    public List<Mail> ConseguirEsborranys(int startIndex, int endIndex) {
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

            Message[] mensajes;

            if(folder.getMessageCount() < endIndex){
                mensajes = folder.getMessages();
            } else {
                mensajes = folder.getMessages(folder.getMessageCount() - endIndex, folder.getMessageCount() - startIndex);
            }

            for (Message mensaje : mensajes) {
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                String contenido = "";
                Object mensajeContenido = mensaje.getContent();
                if (mensajeContenido instanceof String) {
                    contenido = (String) mensajeContenido;
                } else if (mensajeContenido instanceof Multipart) {
                    Multipart multipart = (Multipart) mensajeContenido;
                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart parte = multipart.getBodyPart(j);
                        if (parte.isMimeType("text/plain")) {
                            contenido += parte.getContent().toString();
                        }
                    }
                }
                m.setContingut(contenido);
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

    public List<Mail> ConseguirCorreuBrossa(int startIndex, int endIndex) {
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
            
            Message[] mensajes;
            
            if(folder.getMessageCount() < endIndex){
                mensajes = folder.getMessages();
            } else {
                mensajes = folder.getMessages(folder.getMessageCount() - endIndex, folder.getMessageCount() - startIndex);
            }
            for (Message mensaje : mensajes) {
                Mail m = new Mail();
                m.setAsunto(mensaje.getSubject());
                m.setRemitente(mensaje.getFrom()[0].toString());
                String contenido = "";
                Object mensajeContenido = mensaje.getContent();
                if (mensajeContenido instanceof String) {
                    contenido = (String) mensajeContenido;
                } else if (mensajeContenido instanceof Multipart) {
                    Multipart multipart = (Multipart) mensajeContenido;
                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart parte = multipart.getBodyPart(j);
                        if (parte.isMimeType("text/plain")) {
                            contenido += parte.getContent().toString();
                        }
                    }
                }
                m.setContingut(contenido);
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

    public void adjuntarArchivo(File archivoAdjunto, MimeMultipart multipart) throws MessagingException {
        MimeBodyPart adjunto = new MimeBodyPart();
        try {
            adjunto.attachFile(archivoAdjunto);
        } catch (IOException ex) {
            Logger.getLogger(EmailClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        multipart.addBodyPart(adjunto);
    }

    public void agregarArchivoAdjunto(File archivoAdjunto) {
        archivosAdjuntos.add(archivoAdjunto);
    }

    public void eliminarMail(Mail mail) {
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

            Folder folder = store.getFolder(bandejaA);
            folder.open(Folder.READ_WRITE);

            // Obtener los mensajes en orden inverso
            Message[] mensajes = folder.getMessages(folder.getMessageCount() - acaba, folder.getMessageCount() - inicia);

            // Agregar los mensajes a la lista en orden inverso
            for (int i = mensajes.length - 1; i >= 0; i--) {
                Message mensaje = mensajes[i];
                String asunto = mensaje.getSubject();
                String remitente = mensaje.getFrom()[0].toString();
                String contenido = "";
                Object mensajeContenido = mensaje.getContent();
                if (mensajeContenido instanceof String) {
                    contenido = (String) mensajeContenido;
                } else if (mensajeContenido instanceof Multipart) {
                    Multipart multipart = (Multipart) mensajeContenido;
                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart parte = multipart.getBodyPart(j);
                        if (parte.isMimeType("text/plain")) {
                            contenido += parte.getContent().toString();
                        }
                    }
                }
                if (asunto == null || mail.getAsunto() == null) {
                    if (mail.getContingut().equals(contenido) && mail.getRemitente().equals(remitente)) {
                        mensaje.setFlag(javax.mail.Flags.Flag.DELETED, true);
                        break;
                    }
                } else {
                    if (mail.getAsunto().equals(asunto) && mail.getContingut().equals(contenido) && mail.getRemitente().equals(remitente)) {
                        mensaje.setFlag(javax.mail.Flags.Flag.DELETED, true);
                        break;
                    }
                }
            }

            folder.close(true);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println(ex.getMessage());
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void verMail(Mail mail, int startIndex, int endIndex, String bandeja) {
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
            
            bandejaA = bandeja;
            
            Folder folder = store.getFolder(bandeja);
            folder.open(Folder.READ_ONLY);
            
            inicia = startIndex;
            
            acaba = endIndex;

            // Obtener los mensajes en orden inverso
            Message[] mensajes = folder.getMessages(folder.getMessageCount() - endIndex, folder.getMessageCount() - startIndex);

            // Agregar los mensajes a la lista en orden inverso
            for (int i = mensajes.length - 1; i >= 0; i--) {
                Message mensaje = mensajes[i];
                String asunto = mensaje.getSubject();
                String remitente = mensaje.getFrom()[0].toString();
                String contenido = "";
                Object mensajeContenido = mensaje.getContent();
                if (mensajeContenido instanceof String) {
                    contenido = (String) mensajeContenido;
                } else if (mensajeContenido instanceof Multipart) {
                    Multipart multipart = (Multipart) mensajeContenido;
                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart parte = multipart.getBodyPart(j);
                        if (parte.isMimeType("text/plain")) {
                            contenido += parte.getContent().toString();
                        }
                    }
                }
                
                if (asunto == null || mail.getAsunto() == null) {
                    if (mail.getContingut().equals(contenido) && mail.getRemitente().equals(remitente)) {
                        m = new Mail(contenido, remitente, asunto);
                    }
                } else {
                    if (mail.getAsunto().equals(asunto) && mail.getContingut().equals(contenido) && mail.getRemitente().equals(remitente)) {
                        m = new Mail(contenido, remitente, asunto);
                    }
                }
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
        m = mail;
    }

    public Mail getMail() {
        return m;
    }

}
