package br.com.fiap.campusgigs.dto;

import br.com.fiap.campusgigs.model.User;

public record UserResponse(
        Long id,
        String name,
        String username,
        String role,
        String cep,
        String cidade,
        String uf
) {
    public static UserResponse from(User user) {
        var address = user.getAddress();
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole().name(),
                address != null ? address.getCep() : null,
                address != null ? address.getCidade() : null,
                address != null ? address.getUf() : null
        );
    }
}