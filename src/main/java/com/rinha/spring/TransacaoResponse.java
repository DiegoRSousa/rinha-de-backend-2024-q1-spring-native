package com.rinha.spring;

public record TransacaoResponse(int limite, int saldo) {

    public TransacaoResponse(Cliente cliente) {
        this(cliente.getLimite(), cliente.getSaldo());
    }

}
