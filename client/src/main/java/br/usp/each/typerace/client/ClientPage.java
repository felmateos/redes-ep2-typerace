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
    private final Container container;
    private String nome;
    private boolean conectado = false;

    public ClientPage(String defaultlocation) {
        super("WebSocket typerace");

        // Cores
        Color lightGreen = new Color(146, 227, 116);
        Color darkGreen = new Color(85, 183, 35);

        Color lightRed = new Color(241, 101, 101);
        Color darkRed = new Color(183, 40, 40);

        // Grid para organizar o container
        GridBagConstraints grid = new GridBagConstraints();

        // container para comportar os elementos
        container = getContentPane();
        container.setLayout(new GridBagLayout());
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setBackground(Color.DARK_GRAY);

        // Area para o usuario digitar o seu apelido dentro da aplicação
        entradaNome = new JTextField();
        entradaNome.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        entradaNome.setBackground(Color.BLACK);
        entradaNome.setForeground(Color.LIGHT_GRAY);
        entradaNome.setText("insira o seu apelido aqui...");
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.gridwidth = 2;
        grid.gridx = 0;
        grid.gridy = 0;
        container.add(entradaNome, grid);

        // Endereço do servidor local
        uriServidor = new JTextField();
        uriServidor.setText(defaultlocation);
        uriServidor.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        uriServidor.setForeground(Color.LIGHT_GRAY);
        uriServidor.setBackground(Color.BLACK);
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.gridwidth = 2;
        grid.gridx = 0;
        grid.gridy = 1;
        container.add(uriServidor, grid);

        // Botão para se conectar
        conectar = new JButton("Conectar");
        conectar.addActionListener(this);
        conectar.setBackground(darkGreen);
        conectar.setForeground(lightGreen);
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.gridwidth = 1;
        grid.weightx = 0.5;
        grid.gridx = 0;
        grid.gridy = 2;
        container.add(conectar, grid);

        // Botão para se desconectar
        sair = new JButton("Sair");
        sair.addActionListener(this);
        sair.setEnabled(false);
        sair.setBackground(darkRed);
        sair.setForeground(lightRed);
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 0.5;
        grid.gridwidth = 1;
        grid.gridx = 1;
        grid.gridy = 2;
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
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.ipady = 200;
        grid.gridwidth = 2;
        grid.gridheight = 2;
        grid.gridx = 0;
        grid.gridy = 3;
        container.add(scroll, grid);

        // Area onde o usuario digita sua mensagem
        entradaMsg = new JTextField();
        entradaMsg.setText("");
        entradaMsg.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        entradaMsg.setBackground(Color.BLACK);
        entradaMsg.setForeground(Color.LIGHT_GRAY);
        entradaMsg.addActionListener(this);
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.ipady = 20;
        grid.gridwidth = 2;
        grid.gridheight = 1;
        grid.gridx = 0;
        grid.gridy = 6;
        container.add(entradaMsg, grid);

        // Tamanho do container
        java.awt.Dimension d = new java.awt.Dimension(400, 368);
        setPreferredSize(d);
        setSize(d);

        //Verifica se a janela foi fechada
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == conectar) conectar();
        else if (e.getSource() == sair) sair();
        else if (e.getSource() == entradaMsg) entradaMsg();
    }

    public void conectar() {
        try {
            cliente = new Client(new URI(uriServidor.getText()), container, chatPublico);
            nome = entradaNome.getText();
            sair.setEnabled(true);
            conectar.setEnabled(false);
            uriServidor.setEditable(false);
            entradaNome.setEditable(false);
            cliente.connect();
            conectado = true;
        } catch (URISyntaxException ex) {
            chatPublico.append(uriServidor.getText() + " não é um valor válido\n");
        }
    }

    public void sair() {
        cliente.close();
        entradaNome.setEditable(true);
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

    public WebSocketClient getCliente() {
        return cliente;
    }

    public boolean isConectado() {
        return conectado;
    }
}
