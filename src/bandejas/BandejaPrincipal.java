package bandejas;

import clases.Mail;
import client.EmailClientConnection;
import funciones.EnviarMails;
import funciones.VerMail;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.Login;

public class BandejaPrincipal extends JFrame {

    private EmailClientConnection ecc;
    private List<Mail> allMails;
    private int currentIndex = 0;
    private int endIndex, startIndex;

    public BandejaPrincipal() {
        bandejaPrincipal();
    }

    private void bandejaPrincipal() {
        setTitle("Safata principal");
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
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Evita que el evento se dispare dos veces
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) { // Verifica si se ha seleccionado alguna fila
                        String remitente = (String) table.getValueAt(selectedRow, 0);
                        String asunto = (String) table.getValueAt(selectedRow, 1);
                        String contenido = (String) table.getValueAt(selectedRow, 2);
                        Mail m = new Mail();
                        m.setAsunto(asunto);
                        m.setRemitente(remitente);
                        m.setContingut(contenido);
                        ecc.verMail(m, startIndex, currentIndex, "INBOX");
                        VerMail vm = new VerMail();
                        vm.setVisible(true);
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

        EnviarMailItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnviarMails em = new EnviarMails();
                em.setVisible(true);
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
        allMails = ecc.ConseguirInbox(0, 9);
        currentIndex = allMails.size() - 1; // Actualizar el índice
    }

    private void loadMoreMails() {
        endIndex = currentIndex - 1; // El índice del correo anterior al último correo actual
        startIndex = Math.max(0, endIndex - 9); // El índice del correo anterior al primer correo actual
        allMails.addAll(0, ecc.ConseguirInbox(startIndex, endIndex));
        currentIndex = startIndex - 1; // Actualizar el índice
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
