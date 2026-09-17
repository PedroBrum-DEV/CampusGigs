package br.com.fiap.campusgigs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateAddressRequest(
        @NotBlank @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos numéricos") String cep
) {}