package main;

import Client.EmailClientConnection;
import bandejas.BandejaPrincipal;
import clases.MailConfigUse;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Login extends JFrame{
    
    private JTextField TextCorreu;
    private JPasswordField TextPasswd;
    private Component component = this;
    private EmailClientConnection ecc;

    public Login() {
        setTitle("Login Gmail");
        setSize(600,146);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel panel1 = new JPanel();
        JLabel LabelLogin = new JLabel("Inici de sessió");
        panel1.add(LabelLogin);
        add(panel1, BorderLayout.NORTH);
        
        JPanel panel2 = new JPanel();
        JLabel LabelCorreu = new JLabel("Correu");
        JLabel LabelPasswd = new JLabel("Contrasenya");
        TextCorreu = new JTextField(20);
        TextPasswd = new JPasswordField(20);
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        
        c.gridx = 0;
        c.gridy = 0;
        
        panel2.add(LabelCorreu, c);
        
        c.gridx = 1;
        c.gridy = 0;
        
        panel2.add(TextCorreu, c);
        
        c.gridx = 0;
        c.gridy = 1;
        
        panel2.add(LabelPasswd, c);
        
        c.gridx = 1;
        c.gridy = 1;
        
        panel2.add(TextPasswd, c);
        
        add(panel2, BorderLayout.CENTER);
        
        JPanel panel3 = new JPanel();
        
        JButton BotonBorrar = new JButton("Esborrar");
        
        JButton BotonISessio = new JButton("Iniciar Sessio");
        
        GridBagConstraints cc = new GridBagConstraints();
        
        cc.gridwidth = 1;
        cc.weightx = 1;
        cc.weighty = 1;
        
        cc.gridx = 0;
        cc.gridy = 0;
        
        panel3.add(BotonBorrar, cc);
        
        cc.gridx = 1;
        cc.gridy = 0;
        
        panel3.add(BotonISessio, cc);
        
        add(panel3, BorderLayout.SOUTH);
                
        BotonBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TextCorreu.setText("");
                TextPasswd.setText("");
            }
        });
        
        BotonISessio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(TextCorreu.getText().isBlank() || TextPasswd.getText().isBlank()){
                    JOptionPane.showMessageDialog(component, "Omple tots els camps");
                } else {
                    char[] passwordChars = TextPasswd.getPassword();
                    String password = new String(passwordChars);
                    ecc = new EmailClientConnection(TextCorreu.getText(), password);
                    ecc.ConectionImap();
                    ecc.ConectionSMTP();
                    BandejaPrincipal bp = new BandejaPrincipal();
                    bp.setVisible(true);
                    setVisible(false);
                }
            }
        });
        
        
    }
    
    
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    
}
