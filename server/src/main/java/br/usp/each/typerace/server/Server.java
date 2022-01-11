package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;

public class Server extends WebSocketServer {

    private final Map<String, WebSocket> connections;
    private final List<String> palavras = new ArrayList<>(Arrays.asList("@L: a","@L: b","@L: c"));
    private boolean start = false;

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Bem vindo ao servidor!"); //Msg para o new client
        conn.send("Seu id: " + conn.toString().split("@")[1]);
        broadcast("nova conexão: " + handshake.toString()); //Msg p tds os clients
        System.out.println(conn + " entrou no servidor!"); //Msg p server
        connections.put(conn.toString().split("@")[1], conn);
        broadcast("conexoes: " + connections.size());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(connections.remove(conn.toString().split("@")[1]) + " saiu do servidor!");
        System.out.println(conn + " saiu do servidor!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast(message);
        verificaMensagem(conn, message);
        System.out.println(conn.toString().split("@")[1] + " - " + message);
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
        if (!start && messageC.equals("s"))
            iniciaPartida();
        if (start && palavras.contains("@L: "+messageC)) {
            broadcast(conn + " acertou");
            //broadcast("@LS: ");
        }
    }

    public void iniciaPartida() {
        try {
            broadcast("Websocket Typerace será iniciado em: ");
            Thread.sleep(1000);
            broadcast("3");
            Thread.sleep(1000);
            broadcast("2");
            Thread.sleep(1000);
            broadcast("1");
            Thread.sleep(1000);
            broadcast("VAI!");
            for (String p: palavras)
                broadcast(p);
            start = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
