package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.net.URI;
import java.time.LocalDateTime;
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
    private int acertos = 0;
    private int erros = 0;
    private List<String> palavrasCertas = new LinkedList<>();



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
        if (iniciado && message.contains(id)) {
            chatPublico.append("ACERTO" + acertos);
            if (message.contains("acertou")) acerto(message);
            else erros++;
        }
        if (message.contains("server: ")) {
            if (message.contains("VAI!")) inicia();
            if (message.contains("Seu id: ")) id = message.split("Seu id: ")[1];
            if (message.contains("@L: ")) containerLista.append(message.split("@L: ")[1] + "\n");
            else chatPublico.append(message + "\n");
        } else {
            chatPublico.append(message + "\n");
        }
        chatPublico.setCaretPosition(chatPublico.getDocument().getLength());
        containerLista.setCaretPosition(containerLista.getDocument().getLength());
    }

    public void inicia() {
        lp.iniciado();
        cp.iniciado();
        iniciado = true;
        containerLista.setText("");
    }

    public void acerto(String message) {
        String messageC = message.split("\'")[1];
        if (!palavrasCertas.contains(messageC)) {
            palavrasCertas.add(messageC);
            acertos++;
            containerLista.replaceRange(null, 0, messageC.length()+1);
            if (acertos == 3) encerra();
        }
    }

    public void encerra() {
        cp.getEntradaMsg().setEditable(false);
        this.send("RESULTADOS :" +
                "/" + id +
                "/" + (acertos+erros) +
                "/" + acertos +
                "/" + (erros));
        containerLista.setText("Parabens voce digitou corretamente todas as palavras!\n" +
                "Assim que todos os outros participantes terminarem\n" +
                "os resultados aparecerao aqui.");
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