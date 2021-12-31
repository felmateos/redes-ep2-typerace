package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Map;

public class Server extends WebSocketServer {

    private final Map<String, WebSocket> connections;

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Bem vindo ao servidor!"); //Msg para o new client
        broadcast("nova conexão: " + handshake.toString()); //Msg p tds os clients
        System.out.println(conn + " entrou no servidor!"); //Msg p server
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " saiu do servidor!");
        System.out.println(conn + " saiu do servidor!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast(message);
        System.out.println(conn + " - " + message);
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
}
