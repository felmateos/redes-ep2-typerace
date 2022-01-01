package br.usp.each.typerace.server;

import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ServerMain {

    private WebSocketServer server;

    public ServerMain(WebSocketServer server) {
        this.server = server;
    }

    public void init() {
        try {
            System.out.println("Iniciando servidor...");
            server.start();
            System.out.println("Servidor iniciado na porta: " + server.getPort());
            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String in = sysin.readLine();
                server.broadcast(in);
                if (in.equals("sair")) {
                    server.stop(1000);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebSocketServer server = new Server(3004, new HashMap<>());

        ServerMain main = new ServerMain(server);

        main.init();
    }

}
