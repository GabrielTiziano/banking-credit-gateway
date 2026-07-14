package com.gabrieltiziano.banking_credit_gateway.listener;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaConcluidaMessage;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.entities.StatusProposta;
import com.gabrieltiziano.banking_credit_gateway.entities.Usuario;
import com.gabrieltiziano.banking_credit_gateway.repository.PropostaRepository;
import com.gabrieltiziano.banking_credit_gateway.service.WebSocketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropostaConcluidaListenerTest {

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private PropostaConcluidaListener listener;

    private Proposta existente() {
        Usuario usuario = Usuario.builder()
                .nome("Ana").sobrenome("Souza").cpf("12345678901")
                .telefone("11999999999").renda(new BigDecimal("5000")).build();
        return Proposta.builder()
                .id(1L).valorSolicitado(new BigDecimal("1000")).prazoPagamento(24)
                .status(StatusProposta.EM_ANALISE).usuario(usuario).build();
    }

    @Test
    @DisplayName("Atualiza status/observação, persiste e notifica o WebSocket")
    void atualizaPersisteENotifica() {
        Proposta proposta = existente();
        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));
        PropostaConcluidaMessage msg = new PropostaConcluidaMessage(1L, StatusProposta.APROVADA, "ok");

        listener.propostaConcluida(msg);

        assertThat(proposta.getStatus()).isEqualTo(StatusProposta.APROVADA);
        assertThat(proposta.getObservacao()).isEqualTo("ok");
        verify(propostaRepository).save(proposta);
        verify(webSocketService).notificar(any(PropostaResponse.class));
    }

    @Test
    @DisplayName("Proposta inexistente lança exceção e não persiste nem notifica")
    void propostaInexistenteLancaExcecao() {
        when(propostaRepository.findById(99L)).thenReturn(Optional.empty());
        PropostaConcluidaMessage msg = new PropostaConcluidaMessage(99L, StatusProposta.APROVADA, null);

        assertThatThrownBy(() -> listener.propostaConcluida(msg))
                .isInstanceOf(NoSuchElementException.class);

        verify(propostaRepository, never()).save(any());
        verify(webSocketService, never()).notificar(any());
    }
}
