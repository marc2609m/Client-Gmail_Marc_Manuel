package bandejas;

import clases.Mail;
import client.EmailClientConnection;
import funciones.EnviarMails;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import main.Login;

public class BandejaCorreuBrossa extends JFrame {
    private EmailClientConnection ecc;
    private List<Mail> allMails;
    private int currentIndex = 0;

    public BandejaCorreuBrossa() {
        bandejaCorreuBrossa();
    }

    private void bandejaCorreuBrossa() {
        setTitle("Correu Brossa");
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

        allMails = ecc.ConseguirCorreuBrossa();

        Object[][] data = new Object[allMails.size()][2];
        populateTableData(data);

        String[] columnNames = {"Emissor", "Assumpte"};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton moreButton = new JButton("Més");
        moreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMoreMails();
                Object[][] newData = new Object[allMails.size()][2];
                populateTableData(newData);
                table.setModel(new DefaultTableModel(newData, columnNames));
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

        EsborranysItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BandejaEsborranys be = new BandejaEsborranys();
                be.setVisible(true);
                setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(moreButton);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setJMenuBar(menuBar);
    }

    private void loadMoreMails() {
        // Aquí deberías implementar la lógica para cargar más correos electrónicos
        // desde la bandeja de correo basura.
        // Por simplicidad, asumiremos que no hay más correos para cargar.
        JOptionPane.showMessageDialog(this, "No hay más correos electrónicos disponibles.", "No hay más correos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void populateTableData(Object[][] data) {
        for (int i = 0; i < allMails.size(); i++) {
            Mail m = allMails.get(i);
            data[i][0] = m.getRemitente();
            data[i][1] = m.getAsunto();
        }
    }
}
