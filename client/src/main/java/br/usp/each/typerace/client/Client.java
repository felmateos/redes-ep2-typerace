package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.net.URI;

public class Client extends WebSocketClient {

    private final JTextField uriServidor;
    private final JButton conectar;
    private final JButton sair;
    private final JTextArea containerLista;
    private final JTextArea chatPublico;
    private String id;

    public Client(URI serverUri, ClientPage cp) {
        super(serverUri);
        this.uriServidor = cp.getUriServidor();
        this.conectar = cp.getConectar();
        this.sair = cp.getSair();
        this.containerLista = cp.getContainerLista();
        this.chatPublico = cp.getChatPublico();
    }

    @Override
    public void onMessage(String message) {
        if (message.contains("@L: ")) {
            containerLista.append("[ ]" + message.split("@L: ")[1] + "\n");
        } else chatPublico.append(message + "\n");
        if (message.contains("Seu id: ")) {
            id = message.split("Seu id: ")[1];
            this.send(id);
        }
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        containerLista.setCaretPosition(containerLista.getDocument().getLength());
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        chatPublico.append("Você está conectado a: " + getURI() + "\n");
        chatPublico.append("Handshake: " + handshake + "\n");
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