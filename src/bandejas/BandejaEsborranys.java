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
    private List<Mail> allMailsDraft;
    private int currentIndex = 0;

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

        Object[][] data = new Object[allMailsDraft.size()][2];
        populateTableData(data);

        String[] columnNames = {"Emissor", "Assumpte"};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton moreButton = new JButton("Més");
        moreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMoreMails();
                Object[][] newData = new Object[allMailsDraft.size()][2];
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
        // Cargar la primera página de correos electrónicos en borrador (por ejemplo, los primeros 10 correos)
        allMailsDraft = ecc.ConseguirEsborranys(1, 10); // Cambiado a cargar solo una página
        currentIndex = allMailsDraft.size(); // Actualizar el índice
    }

    private void loadMoreMails() {
        if (allMailsDraft.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay más correos electrónicos disponibles.", "No hay más correos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int endIndex = currentIndex - 1; // El índice del correo anterior al último correo actual
        int startIndex = Math.max(1, endIndex - 9); // El índice del correo anterior al primer correo actual, asegurando que no sea menor que 1

        // Si el índice de inicio es 1, significa que estamos en la primera página, por lo que no hay más correos para cargar
        if (startIndex == 1) {
            JOptionPane.showMessageDialog(this, "No hay más correos electrónicos disponibles.", "No hay más correos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Cargar correos electrónicos adicionales
        List<Mail> additionalMails = ecc.ConseguirEsborranys(startIndex, endIndex);

        // Agregar correos electrónicos cargados al principio de la lista
        allMailsDraft.addAll(0, additionalMails);

        // Actualizar el índice
        currentIndex = startIndex - 1;
    }

    private void populateTableData(Object[][] data) {
        for (int i = 0; i < allMailsDraft.size(); i++) {
            Mail m = allMailsDraft.get(i);
            data[i][0] = m.getRemitente();
            data[i][1] = m.getAsunto();
        }
    }
}
