package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.net.URI;
import java.util.*;

public class Client extends WebSocketClient {

    private final JTextField uriServidor;
    private final JButton conectar;
    private final JButton sair;
    private final JTextArea containerLista;
    private final JTextArea chatPublico;
    private ClientPage cp;
    private LaunchPage lp;
    private String id;
    private boolean iniciado = false;



    public Client(URI serverUri, ClientPage cp, LaunchPage lp) {
        super(serverUri);
        this.uriServidor = cp.getUriServidor();
        this.conectar = cp.getConectar();
        this.sair = cp.getSair();
        this.containerLista = cp.getContainerLista();
        this.chatPublico = cp.getChatPublico();
        this.cp = cp;
        this.lp = lp;
    }

    @Override
    public void onMessage(String message) {
        if (message.contains("CP")) {
            if (message.contains("VAI!")) inicia();
            if (message.contains("Seu id: ")) id = message.split("Seu id: ")[1];
            if (message.contains("acertou") && message.contains(id)) removePalavra(message.split(": ")[2]);
            chatPublico.append(message.split("CP: ")[1] + "\n");
            chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        } else if (message.contains("CL")) {
            if (message.contains("Resultados Finais:")) containerLista.setText("");
            containerLista.append(message.split("L: ")[1] + "\n");
            containerLista.setCaretPosition(containerLista.getDocument().getLength());
        } else {
            chatPublico.append(message + "\n");
            chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        }
    }

    public void removePalavra(String message) {
        String[] newContent = containerLista.getText().split(message);
        containerLista.setText(newContent[0]);
        containerLista.append(newContent[1]);
        containerLista.setCaretPosition(containerLista.getDocument().getLength());
    }

    public void inicia() {
        lp.iniciado();
        cp.iniciado();
        iniciado = true;
        containerLista.setText("");
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        chatPublico.append("Voce esta conectado a: " + getURI() + "\n");
        //chatPublico.append("Handshake: " + handshake + "\n");
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        chatPublico.append("Você se desconectou de: " + getURI() + "; Codigo: " + code + " " + reason + "\n");
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        conectar.setEnabled(true);
        uriServidor.setEditable(true);
        sair.setEnabled(false);
        cp.dispose();
    }

    @Override
    public void onError(Exception ex) {
        chatPublico.append("Erro ocorreu em...\n" + ex + "\n");
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        ex.printStackTrace();
        conectar.setEnabled(true);
        uriServidor.setEditable(true);
        sair.setEnabled(false);
        cp.dispose();
    }
}