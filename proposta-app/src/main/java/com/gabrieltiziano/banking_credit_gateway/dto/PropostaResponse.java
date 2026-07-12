package com.gabrieltiziano.banking_credit_gateway.dto;

import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.entities.StatusProposta;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public record PropostaResponse(
        Long id,
        String nome,
        String sobrenome,
        String cpf,
        String valorSolicitadoFmt,
        Integer prazoPagamento,
        Boolean aprovada,
        String observacao
) {
    public static PropostaResponse from(Proposta proposta){
        return new PropostaResponse(
                proposta.getId(),
                proposta.getUsuario().getNome(),
                proposta.getUsuario().getSobrenome(),
                proposta.getUsuario().getCpf(),
                formatarMoeda(proposta.getValorSolicitado()),
                proposta.getPrazoPagamento(),
                converterStatusParaAprovada(proposta.getStatus()),
                proposta.getObservacao()
        );
    }

    private static Boolean converterStatusParaAprovada(StatusProposta status){
        return switch (status) {
            case APROVADA -> Boolean.TRUE;
            case REJEITADA -> Boolean.FALSE;
            case EM_ANALISE -> null;
        };
    }

    private static String formatarMoeda(BigDecimal valor){
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }
}