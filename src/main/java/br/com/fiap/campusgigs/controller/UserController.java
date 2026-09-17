package br.com.fiap.campusgigs.controller;

import br.com.fiap.campusgigs.dto.UpdateAddressRequest;
import br.com.fiap.campusgigs.dto.UserResponse;
import br.com.fiap.campusgigs.model.User;
import br.com.fiap.campusgigs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/me")
    public UserResponse me(@AuthenticationPrincipal User currentUser) {
        return UserResponse.from(currentUser);
    }

    @PatchMapping("/users/me/address")
    public UserResponse updateAddress(@Valid @RequestBody UpdateAddressRequest request,
                                      @AuthenticationPrincipal User currentUser) {
        return UserResponse.from(userService.updateAddress(currentUser, request.cep()));
    }

    @GetMapping("/users")
    public List<UserResponse> findAll(@AuthenticationPrincipal User currentUser) {
        return userService.findAll(currentUser).stream().map(UserResponse::from).toList();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        userService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}