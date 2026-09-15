package br.com.fiap.campusgigs.dto;

import br.com.fiap.campusgigs.model.JobApplication;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long gigId,
        String gigTitle,
        Long applicantId,
        String applicantName,
        String message,
        String status,
        LocalDateTime createdAt
) {
    public static ApplicationResponse from(JobApplication app) {
        return new ApplicationResponse(
                app.getId(),
                app.getGig().getId(),
                app.getGig().getTitle(),
                app.getApplicant().getId(),
                app.getApplicant().getName(),
                app.getMessage(),
                app.getStatus().name(),
                app.getCreatedAt()
        );
    }
}