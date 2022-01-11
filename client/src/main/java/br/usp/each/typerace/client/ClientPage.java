package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientPage extends JFrame implements ActionListener {

    private final JTextField uriServidor;
    private final JButton conectar;
    private final JButton sair;
    private final JTextArea chatPublico;
    private final JTextField entradaMsg;
    private final JTextField entradaNome;
    private WebSocketClient cliente;
    private String nome;
    private boolean conectado = false;
    private final LaunchPage lp;

    // Grid para organizar o container
    private GridBagConstraints grid = new GridBagConstraints();

    // Cores
    Color lightGreen = new Color(154, 245, 120);
    Color darkGreen = new Color(87, 203, 29);
    Color disabledGreen = new Color(67, 107, 62);

    Color lightRed = new Color(241, 101, 101);
    Color darkRed = new Color(201, 42, 42);
    Color disabledRed = new Color(117, 57, 57);

    public ClientPage(String endereco, LaunchPage lp) {
        super("WebSocket typerace");

        this.lp = lp;

        // Container para comportar os elementos
        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setBackground(Color.DARK_GRAY);

        grid.fill = GridBagConstraints.HORIZONTAL;

        // Area para o usuario digitar o seu apelido dentro da aplicação
        entradaNome = new JTextField();
        entradaNome.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        entradaNome.setBackground(Color.BLACK);
        entradaNome.setForeground(Color.LIGHT_GRAY);
        entradaNome.setText("insira o seu apelido aqui...");
        grid.gridwidth = 2;
        grid.gridx = 0;
        grid.gridy = 0;
        container.add(entradaNome, grid);

        // Endereço do servidor local
        uriServidor = new JTextField();
        uriServidor.setText(endereco);
        uriServidor.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        uriServidor.setForeground(Color.LIGHT_GRAY);
        uriServidor.setBackground(Color.BLACK);
        setGrid(1, 2, 0, 1);
        container.add(uriServidor, grid);

        // Botão para se conectar
        conectar = new JButton("Conectar");
        conectar.addActionListener(this);
        conectar.setBackground(darkGreen);
        conectar.setForeground(lightGreen);
        setGrid(1, 1, 0, 2);
        grid.weightx = 0.45;
        container.add(conectar, grid);

        // Botão para se desconectar
        sair = new JButton("Sair");
        sair.addActionListener(this);
        sair.setEnabled(false);
        sair.setBackground(disabledRed);
        sair.setForeground(lightRed);
        setGrid(1, 1, 1, 2);
        grid.weightx = 0.55;
        container.add(sair, grid);

        // Area onde as mensagens do servidor e dos usuarios aparecem
        JScrollPane scroll = new JScrollPane();
        scroll.setBackground(Color.BLACK);
        scroll.setForeground(Color.LIGHT_GRAY);
        scroll.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        chatPublico = new JTextArea();
        chatPublico.setBorder(new LineBorder(Color.DARK_GRAY, 0));
        chatPublico.setBackground(Color.BLACK);
        chatPublico.setForeground(Color.LIGHT_GRAY);
        chatPublico.setEditable(false);
        scroll.setViewportView(chatPublico);
        scroll.getComponent(0);
        setGrid(2, 2, 0, 3);
        grid.ipady = 200;
        container.add(scroll, grid);

        // Area onde o usuario digita sua mensagem
        entradaMsg = new JTextField();
        entradaMsg.setText("");
        entradaMsg.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        entradaMsg.setBackground(Color.BLACK);
        entradaMsg.setForeground(Color.LIGHT_GRAY);
        entradaMsg.addActionListener(this);
        setGrid(1, 2, 0, 6);
        grid.ipady = 20;
        container.add(entradaMsg, grid);

        // Tamanho do container
        java.awt.Dimension d = new java.awt.Dimension(400, 368);
        setPreferredSize(d);
        setSize(d);

        // Verifica se a janela foi fechada
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (cliente != null) cliente.close();
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == conectar) conectar();
        else if (e.getSource() == sair) sair();
        else if (e.getSource() == entradaMsg) entradaMsg();
    }

    public void conectar() {
        try {
            cliente = new Client(new URI(uriServidor.getText()), this);
            nome = entradaNome.getText();
            sair.setEnabled(true);
            conectar.setEnabled(false);
            uriServidor.setEditable(false);
            entradaNome.setEditable(false);
            cliente.connect();
            conectar.setBackground(disabledGreen);
            sair.setBackground(darkRed);
            conectado = true;
            lp.hasConnection = true;
            lp.lwsc.add(cliente);
        } catch (URISyntaxException ex) {
            chatPublico.append(uriServidor.getText() + " não é um valor válido\n");
        }
    }

    public void sair() {
        cliente.close();
        entradaNome.setEditable(true);
        sair.setBackground(disabledRed);
        conectar.setBackground(darkGreen);
    }

    public void entradaMsg() {
        if (cliente != null) {
            cliente.send(nome + ": " + entradaMsg.getText());
            entradaMsg.setText("");
            entradaMsg.requestFocus();
        } else {
            chatPublico.append("usuario não conectado!\n");
            chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        }
    }

    private void setGrid(int gridheight, int gridwidth, int gridx, int gridy) {
        grid.gridheight = gridheight;
        grid.gridwidth = gridwidth;
        grid.gridx = gridx;
        grid.gridy = gridy;
    }

    public WebSocketClient getCliente() {
        return cliente;
    }

    public JTextField getUriServidor() {
        return uriServidor;
    }

    public JButton getConectar() {
        return conectar;
    }

    public JButton getSair() {
        return sair;
    }

    public JTextArea getChatPublico() {
        return chatPublico;
    }

    public boolean isConectado() {
        return conectado;
    }
}
