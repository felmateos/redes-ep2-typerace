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

        LaunchPage lp = new LaunchPage(endereco);

        while(!lp.hasConnection) {}

        ClientMain cm1 = new ClientMain(lp.lwsc.get(0));
        if (lp.lwsc.size() > 1) {
            for (WebSocketClient c : lp.lwsc)
                new ClientMain(c);
        }

        // Duas paginas de clientes para teste
        //ClientPage pg1 = new ClientPage(endereco);
        //ClientPage pg2 = new ClientPage(endereco);

        // Fica travado no loop até que os clientes se conectem
        //while (!pg1.isConectado() || !pg2.isConectado()) {}

        // Caso ambos se conectem são criados dois objetos ClientMain com os CLients conectados 
        //ClientMain cara1 = new ClientMain(pg1.getCliente());
        //ClientMain cara2 = new ClientMain(pg2.getCliente());


    }
}
