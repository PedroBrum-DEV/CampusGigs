package br.com.fiap.campusgigs.service;

import br.com.fiap.campusgigs.client.CepClient;
import br.com.fiap.campusgigs.dto.CepResponse;
import br.com.fiap.campusgigs.exception.CepNotFoundException;
import br.com.fiap.campusgigs.exception.CepServiceUnavailableException;
import br.com.fiap.campusgigs.model.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final CepClient cepClient;

    public Address resolveByCep(String rawCep) {
        String cep = rawCep == null ? "" : rawCep.replaceAll("\\D", "");

        if (cep.length() != 8) {
            throw new CepNotFoundException("CEP inválido: informe 8 dígitos numéricos");
        }

        CepResponse response;
        try {
            response = cepClient.buscarPorCep(cep);
        } catch (RestClientException ex) {
            // serviço externo fora do ar, lento ou com erro de rede
            throw new CepServiceUnavailableException(
                    "Não foi possível consultar o serviço de CEP no momento. Tente novamente em instantes."
            );
        }

        if (response == null || response.erro()) {
            throw new CepNotFoundException("CEP não encontrado: " + cep);
        }

        return Address.builder()
                .cep(cep)
                .logradouro(response.logradouro())
                .bairro(response.bairro())
                .cidade(response.localidade())
                .uf(response.uf())
                .build();
    }
}