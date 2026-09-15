package br.com.fiap.campusgigs.dto;

import br.com.fiap.campusgigs.model.Gig;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GigResponse(
        Long id,
        String title,
        String description,
        String category,
        BigDecimal price,
        String status,
        Long ownerId,
        String ownerName,
        LocalDateTime createdAt
) {
    public static GigResponse from(Gig gig) {
        return new GigResponse(
                gig.getId(),
                gig.getTitle(),
                gig.getDescription(),
                gig.getCategory(),
                gig.getPrice(),
                gig.getStatus().name(),
                gig.getOwner().getId(),
                gig.getOwner().getName(),
                gig.getCreatedAt()
        );
    }
}