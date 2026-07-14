package com.gabrieltiziano.analise_credito.service.strategy.impl;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class RendaMaiorQueValorSolicitadoImplTest {

    private final RendaMaiorQueValorSolicitadoImpl strategy = new RendaMaiorQueValorSolicitadoImpl();

    private Proposta proposta(String renda, String valorSolicitado) {
        Usuario usuario = new Usuario();
        usuario.setRenda(new BigDecimal(renda));
        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);
        proposta.setValorSolicitado(new BigDecimal(valorSolicitado));
        return proposta;
    }

    @Test
    @DisplayName("Renda maior que o valor solicitado pontua 100")
    void rendaMaiorPontua100() {
        assertThat(strategy.calcular(proposta("5000", "3000"))).isEqualTo(100);
    }

    @Test
    @DisplayName("Renda menor que o valor solicitado pontua 0")
    void rendaMenorPontua0() {
        assertThat(strategy.calcular(proposta("3000", "5000"))).isEqualTo(0);
    }

    @Test
    @DisplayName("Renda igual ao valor solicitado pontua 0 (comparação por compareTo)")
    void rendaIgualPontua0() {
        assertThat(strategy.calcular(proposta("3000", "3000"))).isEqualTo(0);
    }
}
