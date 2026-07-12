package com.gabrieltiziano.banking_credit_gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gabrieltiziano.banking_credit_gateway.entities.StatusProposta;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PropostaConcluidaMessage(
        Long id,
        StatusProposta status,
        String observacao
) {}