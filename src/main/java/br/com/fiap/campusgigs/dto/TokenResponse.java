package br.com.fiap.campusgigs.dto;

public record TokenResponse(
        String token,
        String type,
        String username,
        String role
) {}