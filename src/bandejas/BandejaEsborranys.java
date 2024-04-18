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

public class BandejaEsborranys extends JFrame {

    private EmailClientConnection ecc;
    private List<Mail> allMails;
    private int currentIndex = 0;
    private int endIndex, startIndex;

    public BandejaEsborranys() {
        bandejaEsborranys();
    }

    private void bandejaEsborranys() {
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

        loadInitialMails();

        Object[][] data = new Object[allMails.size()][3];

        populateTableData(data);

        String[] columnNames = {"Emissor", "Assumpte", "Contingut"};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton moreButton = new JButton("Més");
        moreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMoreMails();
                Object[][] newData = new Object[allMails.size()][3];
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(moreButton);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setJMenuBar(menuBar);
    }

    private void loadInitialMails() {
        // Cargar los últimos 10 correos
        allMails = ecc.ConseguirEsborranys(0, 9);
        currentIndex = allMails.size() - 1; // Actualizar el índice
    }

    private void loadMoreMails() {
        int startIndex = currentIndex + 1; // El índice del primer correo después del último correo actual
        int endIndex = startIndex + 9; // El índice del último correo después del primer correo actual

        // Cargar correos electrónicos adicionales
        List<Mail> additionalMails = ecc.ConseguirEsborranys(startIndex, endIndex);

        boolean ya = false;

        if (!additionalMails.isEmpty()) {
            for (int i = 0; i < additionalMails.size(); i++) {
                for (int j = 0; j < allMails.size(); j++) {
                    if (additionalMails.get(i).equals(allMails.get(j))) {
                        ya = true;
                        break;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hi ha mes correus per carregar");
        }

        if (ya) {
            JOptionPane.showMessageDialog(this, "Tots els correus carregats");
        } else {
            allMails.addAll(additionalMails);
        }

        // Actualizar el índice
        currentIndex = endIndex;
    }

    private void populateTableData(Object[][] data) {
        for (int i = 0; i < allMails.size(); i++) {
            Mail m = allMails.get(i);
            data[i][0] = m.getRemitente();
            data[i][1] = m.getAsunto();
            data[i][2] = m.getContingut();
        }
    }
}
