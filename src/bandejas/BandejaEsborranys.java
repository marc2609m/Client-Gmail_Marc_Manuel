package bandejas;

import clases.Mail;
import client.EmailClientConnection;
import funciones.EnviarMails;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import main.Login;

public class BandejaEsborranys extends JFrame{
    private EmailClientConnection ecc;

    public BandejaEsborranys() {
        bandejaEsborranys();
    }
    
    private void bandejaEsborranys(){
        setTitle("Esborranys");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
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
        
        ecc = new EmailClientConnection();
        
        List<Mail> mails = ecc.ConseguirEsborranys();
        
        Object[][] data = new Object[mails.size()][2];

        for (int i = 0; i < mails.size(); i++) {
            Mail m = mails.get(i);
            data[i][0] = m.getRemitente();
            data[i][1] = m.getAsunto();
        }

        String[] columnNames = {"Emissor", "Assumpte"};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        
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
        
        EnviarMailItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnviarMails em = new EnviarMails();
                em.setVisible(true);
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
        
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        setJMenuBar(menuBar);
    }
}
