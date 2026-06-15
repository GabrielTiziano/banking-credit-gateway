package com.gabrieltiziano.banking_credit_gateway.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record PropostaRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 150, message = "Nome deve conter entre 2 e 150 caracteres")
        String nome,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(min = 2, max = 150, message = "Sobrenome deve conter entre 2 e 150 caracteres")
        String sobrenome,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos (apenas números)")
        String telefone,

        @NotBlank(message = "CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotNull(message = "Renda é obrigatória")
        @Positive(message = "Renda deve ser maior que zero")
        BigDecimal renda,

        @NotNull(message = "Valor solicitado é obrigatório")
        @Positive(message = "Valor solicitado deve ser maior que zero")
        BigDecimal valorSolicitado,

        @NotNull(message = "Prazo de pagamento é obrigatório")
        @Min(value = 1, message = "Prazo deve ser de no mínimo 1 mês")
        @Max(value = 120, message = "Prazo não pode exceder 120 meses")
        Integer prazoPagamento
) {
}
