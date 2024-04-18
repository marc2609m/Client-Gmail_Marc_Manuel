package funciones;

import bandejas.BandejaEsborranys;
import clases.Mail;
import client.EmailClientConnection;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VerMail extends JFrame{
    
    private EmailClientConnection ecc = new EmailClientConnection();
    private Mail m;

    public VerMail() {
        verMail();
    }
    
    
    
    private void verMail(){
        setTitle("Safata principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JLabel remitentText = new JLabel("Remitent");
        JLabel contingutText = new JLabel("Contingut");
        JLabel asuntoText = new JLabel("Assumpte");
        
        ecc = new EmailClientConnection();
        
        m = ecc.getMail();
        
        JLabel remitent = new JLabel(m.getRemitente());
        JLabel contingut = new JLabel(m.getContingut());
        JLabel asunto = new JLabel(m.getAsunto());
        
        JButton eliminarBoton = new JButton("Eliminar");
        
        eliminarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecc.eliminarMail(m, 0, 9);
            }
        });
        
        JPanel panel1 = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;
        
        c.gridx = 0;
        c.gridy = 0;
        
        panel1.add(remitentText, c);
        
        c.gridx = 1;
        c.gridy = 0;
        
        panel1.add(remitent, c);
        
        c.gridx = 0;
        c.gridy = 1;
        
        panel1.add(asuntoText, c);
        
        c.gridx = 1;
        c.gridy = 1;
        
        panel1.add(asunto, c);
        
        c.gridx = 2;
        c.gridy = 0;
        
        panel1.add(contingutText, c);
        
        c.gridx = 2;
        c.gridy = 1;
        
        panel1.add(contingut, c);
        
        JPanel panel2 = new JPanel(new GridBagLayout());
        
        c.gridx = 0;
        c.gridy = 0;
        
        panel2.add(eliminarBoton, c);
        
        add(panel1, BorderLayout.CENTER);
        
        add(panel2, BorderLayout.SOUTH);
    }
}
