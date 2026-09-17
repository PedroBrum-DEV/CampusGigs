package br.com.fiap.campusgigs.service;

import br.com.fiap.campusgigs.dto.LoginRequest;
import br.com.fiap.campusgigs.dto.RegisterRequest;
import br.com.fiap.campusgigs.dto.TokenResponse;
import br.com.fiap.campusgigs.exception.UsernameAlreadyExistsException;
import br.com.fiap.campusgigs.model.Address;
import br.com.fiap.campusgigs.model.Role;
import br.com.fiap.campusgigs.model.User;
import br.com.fiap.campusgigs.repository.UserRepository;
import br.com.fiap.campusgigs.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AddressService addressService;

    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Já existe um usuário com o username: " + request.username());
        }

        Address address = addressService.resolveByCep(request.cep());

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.STUDENT)
                .address(address)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new TokenResponse(token, jwtService.getTokenType(), user.getUsername(), user.getRole().name());
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));

        String token = jwtService.generateToken(user);
        return new TokenResponse(token, jwtService.getTokenType(), user.getUsername(), user.getRole().name());
    }
}