package br.com.fiap.campusgigs.client;

import br.com.fiap.campusgigs.dto.CepResponse;
import org.springframework.web.service.annotation.GetExchange;

public interface CepClient {
    @GetExchange("/{cep}/json")
    CepResponse buscarPorCep(String cep);
}