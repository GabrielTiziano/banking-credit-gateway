package com.gabrieltiziano.analise_credito.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Usuario {
    private Long id;

    private String nome;

    private String sobrenome;

    private String cpf;

    private String telefone;

    private BigDecimal renda;
}
