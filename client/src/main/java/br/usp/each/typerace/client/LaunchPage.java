package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class LaunchPage extends JFrame implements ActionListener {

    private final JTextArea pageTitle;
    private final JTextArea rulesContainer;
    private final JButton newPlayer;
    private final JButton close;
    List<ClientPage> lcp = new LinkedList<>();
    List<WebSocketClient> lwsc = new LinkedList<>();
    boolean hasConnection = false;
    private String endereco;

    // Grid para organizar o container
    private GridBagConstraints grid = new GridBagConstraints();

    Color lightGreen = new Color(154, 245, 120);
    Color darkGreen = new Color(87, 203, 29);
    Color disabledGreen = new Color(67, 107, 62);

    Color lightRed = new Color(241, 101, 101);
    Color darkRed = new Color(201, 42, 42);

    public LaunchPage(String endereco) {
        super("Launcher WebSocket typerace");

        this.endereco = endereco;

        // Container para comportar os elementos
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setBackground(Color.DARK_GRAY);

        grid.fill = GridBagConstraints.HORIZONTAL;


        // Setup do titulo do launcher
        pageTitle = new JTextArea();
        pageTitle.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        pageTitle.setBackground(Color.BLACK);
        pageTitle.setForeground(Color.LIGHT_GRAY);
        pageTitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        pageTitle.setText(
                "\n" +
                        "    __    __     _     __            _        _   \n" +
                        "   / / /\\ \\ \\___| |__ / _\\ ___   ___| | _____| |_ \n" +
                        "   \\ \\/  \\/ / _ \\ '_ \\\\ \\ / _ \\ / __| |/ / _ \\ __|\n" +
                        "    \\  /\\  /  __/ |_) |\\ \\ (_) | (__|   <  __/ |_ \n" +
                        "     \\/  \\/ \\___|_.__/\\__/\\___/ \\___|_|\\_\\___|\\__|\n" +
                        "                                                  \n" +
                        "       _                                          \n" +
                        "      | |_ _   _ _ __   ___ _ __ __ _  ___ ___    \n" +
                        "      | __| | | | '_ \\ / _ \\ '__/ _` |/ __/ _ \\   \n" +
                        "      | |_| |_| | |_) |  __/ | | (_| | (_|  __/   \n" +
                        "       \\__|\\__, | .__/ \\___|_|  \\__,_|\\___\\___|   \n" +
                        "           |___/|_|                               \n"
        );
        pageTitle.setEditable(false);
        setGrid(1, 2, 0, 0);
        container.add(pageTitle, grid);

        // setup do scroll do container de regras
        JScrollPane scroll = new JScrollPane();
        scroll.setBackground(Color.BLACK);
        scroll.setForeground(Color.LIGHT_GRAY);
        scroll.setBorder(new LineBorder(Color.DARK_GRAY, 2));

        // setup do container de regras
        rulesContainer = new JTextArea();
        rulesContainer.setBorder(new LineBorder(Color.DARK_GRAY, 0));
        rulesContainer.setBackground(Color.BLACK);
        rulesContainer.setForeground(Color.LIGHT_GRAY);
        rulesContainer.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        rulesContainer.setText("Instrucoes:\nregras:");
        rulesContainer.setEditable(false);
        scroll.setViewportView(rulesContainer);
        scroll.getComponent(0);
        grid.ipady = 200;
        setGrid(2, 2, 0, 1);
        container.add(scroll, grid);

        grid.ipady = 10;

        // Botão para se conectar
        newPlayer = new JButton("Novo Jogador");
        newPlayer.addActionListener(this);
        newPlayer.setBackground(darkGreen);
        newPlayer.setForeground(lightGreen);
        setGrid(1, 1, 0, 3);
        grid.weightx = 0.4;
        container.add(newPlayer, grid);

        // Botão para se desconectar
        close = new JButton("Sair");
        close.addActionListener(this);
        close.setBackground(darkRed);
        close.setForeground(lightRed);
        setGrid(1, 1, 1, 3);
        grid.weightx = 0.6;
        container.add(close, grid);

        // Tamanho do container
        java.awt.Dimension d = new java.awt.Dimension(400, 542);
        setPreferredSize(d);
        setSize(d);

        // Verifica se a janela foi fechada
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newPlayer) startNewPlayerPage();
        else if (e.getSource() == close) {
            dispose();
            System.exit(0);
        }
    }

    private void startNewPlayerPage() {
        ClientPage cp = new ClientPage(endereco, this);
        lcp.add(cp);
    }

    private void setGrid(int gridheight, int gridwidth, int gridx, int gridy) {
        grid.gridheight = gridheight;
        grid.gridwidth = gridwidth;
        grid.gridx = gridx;
        grid.gridy = gridy;
    }

    public void iniciado() {
        newPlayer.setEnabled(false);
        newPlayer.setBackground(disabledGreen);
    }

    public void finalizado() {
        newPlayer.setEnabled(true);
        newPlayer.setBackground(darkGreen);
    }

    public JTextArea getRulesContainer() {
        return rulesContainer;
    }
}
