package funciones;

import bandejas.BandejaCorreuBrossa;
import bandejas.BandejaEnviats;
import bandejas.BandejaPrincipal;
import client.EmailClientConnection;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import main.Login;

public class EnviarMails extends JFrame {

    private Component component = this;
    private EmailClientConnection ecc;
    private JTextField textoDestinatario, textoAsunto, textoCC, textoBCC;
    private JTextArea textoContenido;
    private JButton botonAdjuntar;

    public EnviarMails() {
        enviarMails();
    }

    private void enviarMails() {
        setTitle("Enviar Mail");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu menuBandejas = new JMenu("Safates");
        JMenuItem BandejaPrincipalItem = new JMenuItem("Safata principal");
        JMenuItem EnviadoslItem = new JMenuItem("Enviats");
        JMenuItem EsborranysItem = new JMenuItem("Esborranys");
        JMenuItem CorreuBrossaItem = new JMenuItem("Correu brossa");
        menuBandejas.add(BandejaPrincipalItem);
        menuBandejas.add(EnviadoslItem);
        menuBandejas.add(EsborranysItem);
        menuBandejas.add(CorreuBrossaItem);

        JMenu menuFunciones = new JMenu("Funcions");
        JMenuItem EnviarMailItem = new JMenuItem("Enviar Mail");
        menuFunciones.add(EnviarMailItem);

        JMenu menuLogout = new JMenu("Log out");
        JMenuItem LogoutItem = new JMenuItem("Log out");
        menuLogout.add(LogoutItem);

        menuBar.add(menuBandejas);
        menuBar.add(menuFunciones);
        menuBar.add(menuLogout);

        JPanel panel1 = new JPanel(new GridBagLayout());

        JLabel labelDestinatario = new JLabel("Destinatari");
        JLabel labelCC = new JLabel("Cc");
        JLabel labelBCC = new JLabel("Bcc");
        JLabel labelAsunto = new JLabel("Assumpte");
        JLabel labelContenido = new JLabel("Contingut");
        textoDestinatario = new JTextField(20);
        textoCC = new JTextField(20);
        textoBCC = new JTextField(20);
        textoAsunto = new JTextField(20);
        textoContenido = new JTextArea(10, 20);
        botonAdjuntar = new JButton("Adjuntar");

        GridBagConstraints c = new GridBagConstraints();

        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;

        panel1.add(labelDestinatario, c);

        c.gridx = 1;
        c.gridy = 0;

        panel1.add(textoDestinatario, c);

        c.gridx = 0;
        c.gridy = 1;

        panel1.add(labelCC, c);

        c.gridx = 1;
        c.gridy = 1;

        panel1.add(textoCC, c);

        c.gridx = 0;
        c.gridy = 2;

        panel1.add(labelBCC, c);

        c.gridx = 1;
        c.gridy = 2;

        panel1.add(textoBCC, c);

        c.gridx = 0;
        c.gridy = 3;

        panel1.add(labelAsunto, c);

        c.gridx = 1;
        c.gridy = 3;

        panel1.add(textoAsunto, c);

        c.gridx = 0;
        c.gridy = 4;

        panel1.add(labelContenido, c);

        c.gridx = 1;
        c.gridy = 4;

        panel1.add(textoContenido, c);

        c.gridx = 0;
        c.gridy = 5;

        panel1.add(botonAdjuntar, c);

        add(panel1, BorderLayout.CENTER);

        JPanel panel2 = new JPanel(new GridBagLayout());

        JButton botonBorrar = new JButton("Esborrar");
        JButton botonEnviar = new JButton("Enviar");

        c.gridx = 0;
        c.gridy = 0;

        panel2.add(botonBorrar, c);

        c.gridx = 1;
        c.gridy = 0;

        panel2.add(botonEnviar, c);

        add(panel2, BorderLayout.SOUTH);

        ecc = new EmailClientConnection();

        botonBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textoAsunto.setText("");
                textoContenido.setText("");
                textoDestinatario.setText("");
                textoCC.setText("");
                textoBCC.setText("");
            }
        });

        botonAdjuntar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(component);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Agregar el archivo seleccionado a la lista de archivos adjuntos
                    ecc.agregarArchivoAdjunto(selectedFile);
                }
            }
        });

        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destinatarios = textoDestinatario.getText();
                String cc = textoCC.getText();
                String bcc = textoBCC.getText();
                String asunto = textoAsunto.getText();
                String contenido = textoContenido.getText();

                if (asunto.isBlank() || contenido.isBlank() || destinatarios.isBlank()) {
                    JOptionPane.showMessageDialog(component, "Omple tots els camps");
                } else {
                    // Mensaje de advertencia si los correos de CC no están separados por comas
                    if (!cc.isBlank() && !cc.contains(",")) {
                        JOptionPane.showMessageDialog(component, "Separa els correus de CC amb comes");
                    } else {
                        ecc.EnviarMail(asunto, destinatarios, cc, bcc, contenido);
                        JOptionPane.showMessageDialog(component, "Missatge enviat");
                    }
                }
            }
        });

        LogoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecc.CloseConectionImap();
                ecc.CloseConectionSMTP();
                Login l = new Login();
                l.setVisible(true);
                setVisible(false);
            }
        });

        BandejaPrincipalItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BandejaPrincipal bp = new BandejaPrincipal();
                bp.setVisible(true);
                setVisible(false);
            }
        });

        EnviadoslItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BandejaEnviats be = new BandejaEnviats();
                be.setVisible(true);
                setVisible(false);
            }
        });

        CorreuBrossaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BandejaCorreuBrossa bcb = new BandejaCorreuBrossa();
                bcb.setVisible(true);
                setVisible(false);
            }
        });

        setJMenuBar(menuBar);
    }

}
