package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

public class ClientMain {

    private WebSocketClient client;

    public ClientMain(WebSocketClient client) {
        this.client = client;
    }

    public void init(String idCliente) {
        System.out.println("Iniciando cliente: " + idCliente);
    }

    public static void main(String[] args) {
        /*
           FIXME: Remover essas strings fixas
           Como podemos fazer para que o cliente receba um parâmetro indicando a qual servidor
           ele deve se conectar e o seu ID?
        */

        String endereco = "ws://localhost:3004";

        ClientPage pg1 = new ClientPage(endereco);
        ClientPage pg2 = new ClientPage(endereco);

        while (!pg1.isConectado() || !pg2.isConectado()) {}

        ClientMain cara1 = new ClientMain(pg1.getCliente());
        ClientMain cara2 = new ClientMain(pg2.getCliente());


    }
}
