package br.usp.each.typerace.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;

public class Server extends WebSocketServer {

    private final Map<String, WebSocket> connections;
    private final List<String> palavras = new ArrayList<>(Arrays.asList("alfabeto","bela","cachorro","respeito","amigos","felicidade","entretenimento","otorrinolaringologista","inconstitucionalissimamente","pneu","covid","jobson","futebol","basquete","volei","computador","mouse","teclado"));
    private Map <String, Estatisticas> estatisticas = new HashMap<>();
    private boolean iniciado = false;
    private long tempo;

    public Server(int port, Map<String, WebSocket> connections) {
        super(new InetSocketAddress(port));
        this.connections = connections;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("CP: Bem vindo ao servidor!"); //Msg para o new client
        conn.send("CP: Seu id: " + conn.toString().split("@")[1]);
        broadcast("CP: nova conexao: " + conn.toString().split("@")[1]); //Msg p tds os clients
        System.out.println(conn + " entrou no servidor!"); //Msg p server
        connections.put(conn.toString().split("@")[1], conn);
        broadcast("CP: conexoes: " + connections.size());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn.toString().split("@")[1]);
        broadcast("CP: " + conn.toString().split("@")[1] + " saiu do servidor!");
        System.out.println("CP: " + conn.toString().split("@")[1] + " saiu do servidor!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast("(" + conn.toString().split("@")[1] + ") " + message);
        System.out.println("(" + conn.toString().split("@")[1] + ") " + message);
        verificaMensagem(conn, message);
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
        String id = conn.toString().split("@")[1];
        String messageC = message;
        if (message.split(": ")[1] != null) messageC = message.split(": ")[1];
        if (!iniciado && messageC.equals("start")) iniciaPartida();
        else if (iniciado && !estatisticas.get(id).terminou)
            atualizaEstatisticas(id, messageC, estatisticas.get(id).palavrasRestantes.contains(messageC));
    }

    public void iniciaPartida() {
        try {
            for (String conn : connections.keySet())
                geraEstatisticas(conn);
            broadcast("CP: Websocket Typerace sera iniciado em: ");
            Thread.sleep(1000);
            broadcast("CP: 3");
            Thread.sleep(1000);
            broadcast("CP: 2");
            Thread.sleep(1000);
            broadcast("CP: 1");
            Thread.sleep(1000);
            broadcast("CP: VAI!");
            for (String p: palavras) broadcast("CLL: " + p);
            iniciado = true;
            tempo = System.currentTimeMillis();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void geraEstatisticas(String id) {
        estatisticas.put(id, new Estatisticas(0, 0, 0, 0, new LinkedList<>(palavras)));
    }
    
    private void atualizaEstatisticas(String id, String message, boolean acerto) {
        if (acerto) {
            estatisticas.get(id).palavrasRestantes.remove(message);
            estatisticas.get(id).acertos++;
            broadcast("CP: (" + id + ") acertou: " + message);
        } else estatisticas.get(id).erros++;
        estatisticas.get(id).total++;
        if (estatisticas.get(id).palavrasRestantes.isEmpty()) {
            finalizaJogo();
        }
    }

    private void finalizaJogo() {
        tempo = System.currentTimeMillis() - tempo;
        broadcast("CP: Partida encerrada!");
        broadcast("CL: Resultados Finais:");
        int pos = 1;

        Map.Entry<String, Estatisticas> ma = null;

        while (!estatisticas.isEmpty()) {
            for (Map.Entry<String, Estatisticas> atual : estatisticas.entrySet()) {
                if (ma == null || atual.getValue().acertos > ma.getValue().acertos)
                    ma = atual;
            }
            broadcast("CL: - " + pos + ": (" + ma.getKey() + ") " + "Tentativas: " + ma.getValue().total + ", Acertos: " + ma.getValue().acertos + ", Erros: " + ma.getValue().erros);
            pos++;
            estatisticas.remove(ma.getKey());
            ma = null;
        }
    }
}
