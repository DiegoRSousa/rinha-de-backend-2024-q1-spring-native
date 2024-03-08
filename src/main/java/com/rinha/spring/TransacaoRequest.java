package com.rinha.spring;

public record TransacaoRequest(double valor, char tipo, String descricao) {

    boolean validar() {
        if (valor % 1 != 0)
            return false;

        if (tipo() != 'c' && tipo() != 'd')
            return false;

        if (null == descricao() || descricao().length() < 1 || descricao().length() > 10)
            return false;

        return true;
    }

    public Transacao toModel(Cliente cliente) {
        return new Transacao((int) valor, tipo, descricao, cliente);
    }
}
