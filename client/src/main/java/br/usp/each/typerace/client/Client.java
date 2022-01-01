package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class Client extends WebSocketClient {

    private final JTextField uriServidor;
    private final JButton conectar;
    private final JButton sair;
    private final JTextArea chatPublico;

    public Client(URI serverUri, Container c, JTextArea chatPublico) {
        super(serverUri);
        this.uriServidor = (JTextField) c.getComponent(1);
        this.conectar = (JButton) c.getComponent(2);
        this.sair = (JButton) c.getComponent(3);
        this.chatPublico = chatPublico;
    }

    @Override
    public void onMessage(String message) {
        chatPublico.append(message + "\n");
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        chatPublico.append("Você está conectado a: " + getURI() + "\n");
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        chatPublico.append("Você se desconectou de: " + getURI() + "; Codigo: " + code + " " + reason + "\n");
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        conectar.setEnabled(true);
        uriServidor.setEditable(true);
        sair.setEnabled(false);
    }

    @Override
    public void onError(Exception ex) {
        chatPublico.append("Erro ocorreu em...\n" + ex + "\n");
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        ex.printStackTrace();
        conectar.setEnabled(true);
        uriServidor.setEditable(true);
        sair.setEnabled(false);
    }
}