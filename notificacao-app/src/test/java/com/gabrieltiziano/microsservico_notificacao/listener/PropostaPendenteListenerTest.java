package com.gabrieltiziano.microsservico_notificacao.listener;

import com.gabrieltiziano.microsservico_notificacao.domain.Proposta;
import com.gabrieltiziano.microsservico_notificacao.domain.Usuario;
import com.gabrieltiziano.microsservico_notificacao.service.SmsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PropostaPendenteListenerTest {

    @Mock
    private SmsService smsService;

    @InjectMocks
    private PropostaPendenteListener listener;

    @Test
    @DisplayName("Proposta válida envia SMS de 'em análise' com o nome do cliente")
    void enviaSmsEmAnalise() {
        Usuario usuario = new Usuario();
        usuario.setNome("Ana");
        usuario.setTelefone("+5511999999999");
        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);
        proposta.setValorSolicitado(new BigDecimal("1000"));
        proposta.setPrazoPagamento(24);

        listener.propostaPendente(proposta);

        ArgumentCaptor<String> msg = ArgumentCaptor.forClass(String.class);
        verify(smsService).enviarSms(eq("+5511999999999"), msg.capture());
        assertThat(msg.getValue()).contains("Ana").contains("análise");
    }

    @Test
    @DisplayName("Sem usuário não envia SMS")
    void semUsuarioNaoEnviaSms() {
        Proposta proposta = new Proposta();
        proposta.setUsuario(null);

        listener.propostaPendente(proposta);

        verifyNoInteractions(smsService);
    }
}
