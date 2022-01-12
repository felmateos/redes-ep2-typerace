package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;

public class Server extends WebSocketServer {

    private final Map<String, WebSocket> connections;
    private final List<String> palavras = new ArrayList<>(Arrays.asList("@L: a","@L: b","@L: c"));
    private Map <String, Estatisticas> estatisticasMap;
    private boolean inciado = false;

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("server: Bem vindo ao servidor!"); //Msg para o new client
        conn.send("server: Seu id: " + conn.toString().split("@")[1]);
        broadcast("server: nova conexao: " + conn.toString().split("@")[1]); //Msg p tds os clients
        System.out.println(conn + " entrou no servidor!"); //Msg p server
        connections.put(conn.toString().split("@")[1], conn);
        broadcast("server: conexoes: " + connections.size());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn.toString().split("@")[1]);
        broadcast("server: " + conn.toString().split("@")[1] + " saiu do servidor!");
        System.out.println("server: " + conn.toString().split("@")[1] + " saiu do servidor!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast("(" + conn.toString().split("@")[1] + ") " + message);
        verificaMensagem(conn, message);
        System.out.println("(" + conn.toString().split("@")[1] + ") " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {

        }
    }

    @Override
    public void onStart() {
        System.out.println("Servidor pronto!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public void verificaMensagem(WebSocket conn, String message) {
        String messageC = message;
        if (message.contains(": ")) messageC = message.split(": ")[1];
        if (!inciado && messageC.equals("start"))
            iniciaPartida();
        if (inciado) {
            if (palavras.contains("@L: "+messageC))
                broadcast("server: " + conn.toString().split("@")[1] + " acertou " + "\'" + messageC + "\' ");
            if (messageC.contains("RESULTADOS :")) geraResultados(messageC);
        }
    }

    public void iniciaPartida() {
        try {
            broadcast("server: Websocket Typerace sera iniciado em: ");
            Thread.sleep(1000);
            broadcast("server: 3");
            Thread.sleep(1000);
            broadcast("server: 2");
            Thread.sleep(1000);
            broadcast("server: 1");
            Thread.sleep(1000);
            broadcast("server: VAI!");
            for (String p: palavras)
                broadcast("server: " + p);
            inciado = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void geraResultados(String messageC) {
        String messageE[] = messageC.split("/");
        String id = messageE[1];
        int total = Integer.parseInt(messageE[2]);
        int acertos = Integer.parseInt(messageE[3]);
        int erros = Integer.parseInt(messageE[4]);
        estatisticasMap.put(id, new Estatisticas(id, total, acertos, erros));
        if (estatisticasMap.size() == connections.size()) finalizaJogo();
    }

    private void finalizaJogo() {
        broadcast("server: acabou");
    }
}
