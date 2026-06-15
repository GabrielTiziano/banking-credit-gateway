package com.gabrieltiziano.banking_credit_gateway.controller;

import com.gabrieltiziano.banking_credit_gateway.dto.PropostaRequest;
import com.gabrieltiziano.banking_credit_gateway.dto.PropostaResponse;
import com.gabrieltiziano.banking_credit_gateway.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proposta")
public class PropostaController {
    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @PostMapping
    public ResponseEntity<PropostaResponse> criar(@Valid @RequestBody PropostaRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(propostaService.criar(request));
    }
}
