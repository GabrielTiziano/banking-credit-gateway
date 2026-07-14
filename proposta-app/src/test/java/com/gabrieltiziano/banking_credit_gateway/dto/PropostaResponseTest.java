package com.gabrieltiziano.banking_credit_gateway.dto;

import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.entities.StatusProposta;
import com.gabrieltiziano.banking_credit_gateway.entities.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PropostaResponseTest {

    private Proposta proposta(StatusProposta status) {
        Usuario usuario = Usuario.builder()
                .nome("Ana")
                .sobrenome("Souza")
                .cpf("12345678901")
                .telefone("11999999999")
                .renda(new BigDecimal("5000"))
                .build();
        return Proposta.builder()
                .id(1L)
                .valorSolicitado(new BigDecimal("1000.00"))
                .prazoPagamento(24)
                .status(status)
                .usuario(usuario)
                .build();
    }

    @Test
    @DisplayName("Status APROVADA vira aprovada = true")
    void aprovadaQuandoStatusAprovada() {
        assertThat(PropostaResponse.from(proposta(StatusProposta.APROVADA)).aprovada()).isTrue();
    }

    @Test
    @DisplayName("Status REJEITADA vira aprovada = false")
    void reprovadaQuandoStatusRejeitada() {
        assertThat(PropostaResponse.from(proposta(StatusProposta.REJEITADA)).aprovada()).isFalse();
    }

    @Test
    @DisplayName("Status EM_ANALISE vira aprovada = null (ainda indefinido)")
    void nuloQuandoStatusEmAnalise() {
        assertThat(PropostaResponse.from(proposta(StatusProposta.EM_ANALISE)).aprovada()).isNull();
    }

    @Test
    @DisplayName("Mapeia os dados do usuário e formata o valor em moeda pt-BR")
    void mapeiaDadosEFormataMoeda() {
        PropostaResponse response = PropostaResponse.from(proposta(StatusProposta.APROVADA));

        assertThat(response.nome()).isEqualTo("Ana");
        assertThat(response.cpf()).isEqualTo("12345678901");
        assertThat(response.prazoPagamento()).isEqualTo(24);
        // NumberFormat pt-BR usa espaço não separável; validamos o miolo do valor.
        assertThat(response.valorSolicitadoFmt()).contains("1.000,00");
    }
}
