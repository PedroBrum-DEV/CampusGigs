package br.com.fiap.campusgigs.client;

import br.com.fiap.campusgigs.dto.CepResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface CepClient {
    @GetExchange("/{cep}/json")
    CepResponse buscarPorCep(@PathVariable String cep);
}