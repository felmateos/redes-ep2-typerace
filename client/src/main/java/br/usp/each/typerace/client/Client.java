package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class Client extends WebSocketClient {

    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("conectado à: " + handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Você se desconectou de: " + getURI() + "; Codigo: " + code + " " + reason + "\n");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Erro ocorreu em...\n" + ex + "\n");
    }
}
