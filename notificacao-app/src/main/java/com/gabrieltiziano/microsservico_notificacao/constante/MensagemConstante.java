package com.gabrieltiziano.microsservico_notificacao.constante;

public class MensagemConstante {
    private MensagemConstante(){}
    public static final String PROPOSTA_EM_ANALISE = "Prezado(a) %s, a sua proposta de crédito no valor de R$ %.2f, com prazo de pagamento de %d meses, está em análise. Em breve você receberá uma resposta.";
    public static final String PROPOSTA_APROVADA =
            "Prezado(a) %s, sua proposta de crédito foi APROVADA! 🎉";
    public static final String PROPOSTA_REJEITADA =
            "Prezado(a) %s, sua proposta de crédito foi REJEITADA. Motivo: %s";
}
