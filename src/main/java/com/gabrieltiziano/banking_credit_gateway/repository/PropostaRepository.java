package com.gabrieltiziano.banking_credit_gateway.repository;

import com.gabrieltiziano.banking_credit_gateway.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    List<Proposta> findAllByIntegradaIsFalse();
}
