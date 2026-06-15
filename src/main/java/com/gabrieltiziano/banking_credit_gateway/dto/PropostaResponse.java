package com.gabrieltiziano.banking_credit_gateway.dto;

import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.entities.StatusProposta;

import java.math.BigDecimal;

public record PropostaResponse(
        Long id,
        String nome,
        String sobrenome,
        BigDecimal valorSolicitado,
        Integer prazoPagamento,
        StatusProposta status,
        String observacao
) {
    public static PropostaResponse from(Proposta proposta){
        return new PropostaResponse(
                proposta.getId(),
                proposta.getUsuario().getNome(),
                proposta.getUsuario().getSobrenome(),
                proposta.getValorSolicitado(),
                proposta.getPrazoPagamento(),
                proposta.getStatus(),
                proposta.getObservacao()
        );
    }
}
