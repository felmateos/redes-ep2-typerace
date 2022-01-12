package br.usp.each.typerace.server;

import java.util.*;

public class Estatisticas {
    int total;
    int acertos;
    int erros;
    int pos;
    List<String> palavrasRestantes;
    boolean terminou = false;
    
    Estatisticas(int total, int acertos, int erros, int pos, List<String> palavrasRestantes) {
        this.total = total;
        this.acertos = acertos;
        this.erros = erros;
        this.pos = pos;
        this.palavrasRestantes = palavrasRestantes;
    }
}
