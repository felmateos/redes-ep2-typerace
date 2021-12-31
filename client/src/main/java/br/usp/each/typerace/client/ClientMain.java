package br.usp.each.typerace.client;

import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

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
        String removeMe = "ws://localhost:8080";
        String removeMe2 = "Gilmar";


        try {
            WebSocketClient client = new Client(new URI(removeMe));

            ClientMain main = new ClientMain(client);

            main.init(removeMe2);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
