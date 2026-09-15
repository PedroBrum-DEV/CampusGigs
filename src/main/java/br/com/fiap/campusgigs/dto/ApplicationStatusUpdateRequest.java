package br.com.fiap.campusgigs.dto;

import br.com.fiap.campusgigs.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusUpdateRequest(
        @NotNull ApplicationStatus status
) {}