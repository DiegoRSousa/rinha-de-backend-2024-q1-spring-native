package com.rinha.spring;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Extrato(Saldo saldo, @JsonProperty("ultimas_transacoes") List<UltimaTransacao> transacaos) {
    public Extrato(Cliente cliente, List<Transacao> transacoes){
        this(new Saldo(cliente), transacoes.stream().map(t ->
                new UltimaTransacao(t.getValor(), t.getTipo(), t.getDescricao(), t.getRealizadaEm())
        ).collect(Collectors.toList()));
    }
}


record Saldo(int total, @JsonProperty("data_extrato") LocalDateTime dataExtrato, int limite) {
    public Saldo(Cliente cliente) {
        this(cliente.getSaldo(), LocalDateTime.now(), cliente.getLimite());

    }
}


record UltimaTransacao(int valor, char tipo, String descricao,
                       @JsonProperty("realizada_em") LocalDateTime realizadaEm) {
}
