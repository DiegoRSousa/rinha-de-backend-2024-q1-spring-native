package com.rinha.spring;

public class Cliente {
    private int id;
    private int limite;
    private int saldo;

    public Cliente(int id, int limite, int saldo) {
        this.id = id;
        this.limite = limite;
        this.saldo = saldo;
    }

    public boolean debitar(int valor){
        if(saldo + limite < valor)
            return false;

        saldo -= valor;
        return true;
    }

    public void creditar(int valor) {
        saldo += valor;
    }

    public int getId() {
        return id;
    }

    public int getLimite() {
        return limite;
    }
    public int getSaldo() {
        return saldo;
    }
}