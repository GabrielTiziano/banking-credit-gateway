package com.gabrieltiziano.analise_credito;

import com.gabrieltiziano.analise_credito.domain.Proposta;
import com.gabrieltiziano.analise_credito.service.AnaliseCreditoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AnaliseCreditoApplication {
	private final AnaliseCreditoService analiseCreditoService;

    public AnaliseCreditoApplication(AnaliseCreditoService analiseCreditoService) {
        this.analiseCreditoService = analiseCreditoService;
    }

    public static void main(String[] args) {
		SpringApplication.run(AnaliseCreditoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(Proposta proposta) {
		return args -> {
			analiseCreditoService.analisar(proposta);
		};
	}
}
