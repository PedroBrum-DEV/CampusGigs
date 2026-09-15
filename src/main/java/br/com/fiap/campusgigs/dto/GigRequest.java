package br.com.fiap.campusgigs.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record GigRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String category,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price
) {}