package com.rinha.spring;

import java.time.LocalDateTime;

public class Transacao {

    private int id;
    private int valor;
    private char tipo;
    private String descricao;
    private Cliente cliente;
    private LocalDateTime realizadaEm;

    public Transacao(int valor, char tipo, String descricao, Cliente cliente) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.cliente = cliente;
        this.realizadaEm = LocalDateTime.now();
    }

    public Transacao(int id, int valor, char tipo, String descricao, LocalDateTime realizadaEm) {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadaEm = realizadaEm;
    }

    public boolean executar() {
        if ('c' == tipo) {
            cliente.creditar(valor);
            return true;
        }

        return cliente.debitar(valor);
    }

    public String getDescricao() {
        return descricao;
    }

    public char getTipo() {
        return tipo;
    }

    public int getValor() {
        return valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getRealizadaEm() {
        return realizadaEm;
    }
}