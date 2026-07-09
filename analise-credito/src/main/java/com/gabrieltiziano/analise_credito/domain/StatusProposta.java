package com.gabrieltiziano.analise_credito.domain;

public enum StatusProposta{
    APROVADA("Aprovada"),
    REJEITADA("Rejeitada"),
    EM_ANALISE("Em análise");

    private final String descricao;

    StatusProposta(String descricao) {
        this.descricao = descricao;
    }

    public String descricao(){
        return descricao;
    }
}
