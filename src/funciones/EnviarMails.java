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
import javax.swing.JButton;
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

public class EnviarMails extends JFrame{
    
    private Component component = this;
    private EmailClientConnection ecc;
    private JTextField TextoDestinatario, TextoAsunto;
    private JTextArea TextoContenido;

    public EnviarMails() {
        enviarMails();
    }
    
    private void enviarMails(){
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
        JLabel labelAsunto = new JLabel("Assumpte");
        JLabel labelContenido = new JLabel("Contingut");
        TextoDestinatario = new JTextField(20);
        TextoAsunto = new JTextField(20);
        TextoContenido = new JTextArea(10, 20);
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        
        c.gridx = 0;
        c.gridx = 0;
        
        panel1.add(labelDestinatario, c);
        
        c.gridx = 1;
        c.gridy = 0;
        
        panel1.add(TextoDestinatario, c);
        
        c.gridx = 0;
        c.gridy = 1;
        
        panel1.add(labelAsunto, c);
        
        c.gridx = 1;
        c.gridy = 1;
        
        panel1.add(TextoAsunto, c);
        
        c.gridx = 0;
        c.gridy = 2;
        
        panel1.add(labelContenido, c);
        
        c.gridx = 1;
        c.gridy = 2;
        
        panel1.add(TextoContenido, c);
        
        add(panel1, BorderLayout.CENTER);
        
        JPanel panel2 = new JPanel(new GridBagLayout());
        
        JButton BotonBorrar = new JButton("Esborrar");
        JButton BotonEnviar = new JButton("Enviar");
        
        c.gridx = 0;
        c.gridx = 0;
        
        panel2.add(BotonBorrar, c);
        
        c.gridx = 1;
        c.gridy = 0;
        
        panel2.add(BotonEnviar, c);
        
        add(panel2, BorderLayout.SOUTH);
        
        ecc = new EmailClientConnection();
        
        BotonBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TextoAsunto.setText("");
                TextoContenido.setText("");
                TextoDestinatario.setText("");
            }
        });
        
        BotonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(TextoAsunto.getText().isBlank() || TextoContenido.getText().isBlank() || TextoDestinatario.getText().isBlank()){
                    JOptionPane.showMessageDialog(component, "Omple tots els camps");
                } else {
                    ecc.EnviarMail(TextoAsunto.getText(), TextoDestinatario.getText(), TextoContenido.getText());
                    JOptionPane.showMessageDialog(component, "Missatge enviat");
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
