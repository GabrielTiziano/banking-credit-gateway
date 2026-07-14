package com.gabrieltiziano.microsservico_notificacao.listener;

import com.gabrieltiziano.microsservico_notificacao.domain.Proposta;
import com.gabrieltiziano.microsservico_notificacao.domain.StatusProposta;
import com.gabrieltiziano.microsservico_notificacao.domain.Usuario;
import com.gabrieltiziano.microsservico_notificacao.service.SmsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AnaliseConcluidaListenerTest {

    @Mock
    private SmsService smsService;

    @InjectMocks
    private AnaliseConcluidaListener listener;

    private Proposta proposta(StatusProposta status, String observacao) {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setTelefone("+5511999999999");
        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);
        proposta.setStatus(status);
        proposta.setObservacao(observacao);
        return proposta;
    }

    @Test
    @DisplayName("Proposta APROVADA envia SMS de aprovação")
    void aprovadaEnviaSmsAprovacao() {
        listener.analiseConcluida(proposta(StatusProposta.APROVADA, null));

        ArgumentCaptor<String> msg = ArgumentCaptor.forClass(String.class);
        verify(smsService).enviarSms(eq("+5511999999999"), msg.capture());
        assertThat(msg.getValue()).contains("APROVADA");
    }

    @Test
    @DisplayName("Proposta REJEITADA envia SMS com o motivo")
    void rejeitadaEnviaSmsComMotivo() {
        listener.analiseConcluida(proposta(StatusProposta.REJEITADA, "Pontuação baixa"));

        ArgumentCaptor<String> msg = ArgumentCaptor.forClass(String.class);
        verify(smsService).enviarSms(eq("+5511999999999"), msg.capture());
        assertThat(msg.getValue()).contains("REJEITADA").contains("Pontuação baixa");
    }

    @Test
    @DisplayName("Sem usuário não envia SMS")
    void semUsuarioNaoEnviaSms() {
        Proposta proposta = new Proposta();
        proposta.setUsuario(null);

        listener.analiseConcluida(proposta);

        verifyNoInteractions(smsService);
    }

    @Test
    @DisplayName("Usuário sem telefone não envia SMS")
    void semTelefoneNaoEnviaSms() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setTelefone(null);
        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);
        proposta.setStatus(StatusProposta.APROVADA);

        listener.analiseConcluida(proposta);

        verifyNoInteractions(smsService);
    }
}
