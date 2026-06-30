package com.gabrieltiziano.banking_credit_gateway.mapper;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaRequest;
import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import com.gabrieltiziano.banking_credit_gateway.entities.Usuario;

public class PropostaMapper {
    private PropostaMapper(){

    }

    public static Proposta toProposta(PropostaRequest propostaRequest){
        Usuario usuario = Usuario.builder()
                .nome(propostaRequest.nome())
                .sobrenome(propostaRequest.sobrenome())
                .telefone(propostaRequest.telefone())
                .cpf(propostaRequest.cpf())
                .renda(propostaRequest.renda())
                .build();

        Proposta proposta = Proposta.builder()
                .valorSolicitado(propostaRequest.valorSolicitado())
                .prazoPagamento(propostaRequest.prazoPagamento())
                .usuario(usuario)
                .build();

        usuario.setProposta(proposta);

        return proposta;
    }
}
