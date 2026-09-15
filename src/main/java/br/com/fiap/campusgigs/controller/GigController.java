package br.com.fiap.campusgigs.controller;

import br.com.fiap.campusgigs.dto.GigRequest;
import br.com.fiap.campusgigs.dto.GigResponse;
import br.com.fiap.campusgigs.model.Gig;
import br.com.fiap.campusgigs.model.User;
import br.com.fiap.campusgigs.service.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gigs")
@RequiredArgsConstructor
public class GigController {

    private final GigService gigService;

    @GetMapping
    public List<GigResponse> findAll() {
        return gigService.findAll().stream().map(GigResponse::from).toList();
    }

    @GetMapping("/{id}")
    public GigResponse findById(@PathVariable Long id) {
        return GigResponse.from(gigService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GigResponse> create(@Valid @RequestBody GigRequest request,
                                              @AuthenticationPrincipal User currentUser) {
        Gig created = gigService.create(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(GigResponse.from(created));
    }

    @PutMapping("/{id}")
    public GigResponse update(@PathVariable Long id,
                              @Valid @RequestBody GigRequest request,
                              @AuthenticationPrincipal User currentUser) {
        return GigResponse.from(gigService.update(id, request, currentUser));
    }

    @PatchMapping("/{id}/close")
    public GigResponse close(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return GigResponse.from(gigService.close(id, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        gigService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}