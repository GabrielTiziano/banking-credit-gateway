package com.gabrieltiziano.banking_credit_gateway.service;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaPendenteMessage;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaRequest;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.repository.PropostaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropostaServiceTest {

    private static final String EXCHANGE = "proposta-pendente.ex";

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private NotificacaoService notificacaoService;

    private PropostaService service;

    @BeforeEach
    void setUp() {
        service = new PropostaService(propostaRepository, notificacaoService, EXCHANGE);
    }

    private PropostaRequest request() {
        return new PropostaRequest("Ana", "Souza", "11999999999", "12345678901",
                new BigDecimal("5000"), new BigDecimal("1000"), 24);
    }

    @Test
    @DisplayName("Criar salva com integrada=true, notifica e retorna o response")
    void criarSalvaNotificaERetorna() {
        when(propostaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PropostaResponse response = service.criar(request());

        assertThat(response).isNotNull();
        assertThat(response.aprovada()).isNull(); // recém-criada => EM_ANALISE
        verify(notificacaoService).notificar(any(PropostaPendenteMessage.class), any());

        ArgumentCaptor<Proposta> captor = ArgumentCaptor.forClass(Proposta.class);
        verify(propostaRepository).save(captor.capture());
        assertThat(captor.getValue().isIntegrada()).isTrue();
    }

    @Test
    @DisplayName("Se a notificação falha, marca integrada=false e salva de novo")
    void criarMarcaNaoIntegradaQuandoNotificacaoFalha() {
        when(propostaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doThrow(new RuntimeException("rabbit indisponível"))
                .when(notificacaoService).notificar(any(), any());

        service.criar(request());

        // Salva 2x: 1x na criação (true) e 1x no catch (false).
        ArgumentCaptor<Proposta> captor = ArgumentCaptor.forClass(Proposta.class);
        verify(propostaRepository, times(2)).save(captor.capture());
        assertThat(captor.getValue().isIntegrada()).isFalse();
    }

    @Test
    @DisplayName("listarPropostas mapeia cada entidade para PropostaResponse")
    void listarPropostasMapeia() {
        Proposta proposta = com.gabrieltiziano.banking_credit_gateway.mapper.PropostaMapper.toProposta(request());
        when(propostaRepository.findAll()).thenReturn(List.of(proposta));

        List<PropostaResponse> lista = service.listarPropostas();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).nome()).isEqualTo("Ana");
    }
}
