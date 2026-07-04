package com.gabrieltiziano.banking_credit_gateway.dto;

import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.entities.StatusProposta;
import com.gabrieltiziano.banking_credit_gateway.entities.Usuario;

import java.math.BigDecimal;

public record PropostaPendenteMessage(
        Long id,
        BigDecimal valorSolicitado,
        Integer prazoPagamento,
        StatusProposta status,
        String observacao,
        UsuarioMessage usuario
) {
    public record UsuarioMessage(String nome, String sobrenome, String cpf, String telefone, BigDecimal renda) {}
    public static PropostaPendenteMessage from(Proposta proposta) {
        Usuario u = proposta.getUsuario();
        return new PropostaPendenteMessage(
                proposta.getId(),
                proposta.getValorSolicitado(),
                proposta.getPrazoPagamento(),
                proposta.getStatus(),
                proposta.getObservacao(),
                new UsuarioMessage(u.getNome(), u.getSobrenome(), u.getCpf(), u.getTelefone(), u.getRenda())
        );
    }
}
