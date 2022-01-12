package br.usp.each.typerace.server;

public class Estatisticas {
    String id;
    int total;
    int acertos;
    int erros;
    
    Estatisticas(String id, int total, int acertos, int erros) {
        this.id = id;
        this.total = total;
        this.acertos = acertos;
        this.erros = erros;
    }
}
