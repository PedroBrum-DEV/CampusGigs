package br.com.fiap.campusgigs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        boolean erro
) {}