package br.com.fiap.campusgigs.controller;

import br.com.fiap.campusgigs.dto.ApplicationRequest;
import br.com.fiap.campusgigs.dto.ApplicationResponse;
import br.com.fiap.campusgigs.dto.ApplicationStatusUpdateRequest;
import br.com.fiap.campusgigs.model.JobApplication;
import br.com.fiap.campusgigs.model.User;
import br.com.fiap.campusgigs.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService applicationService;

    @PostMapping("/gigs/{gigId}/applications")
    public ResponseEntity<ApplicationResponse> apply(@PathVariable Long gigId,
                                                     @RequestBody(required = false) ApplicationRequest request,
                                                     @AuthenticationPrincipal User currentUser) {
        ApplicationRequest body = request != null ? request : new ApplicationRequest(null);
        JobApplication created = applicationService.apply(gigId, body, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApplicationResponse.from(created));
    }

    @GetMapping("/gigs/{gigId}/applications")
    public List<ApplicationResponse> findByGig(@PathVariable Long gigId,
                                               @AuthenticationPrincipal User currentUser) {
        return applicationService.findByGig(gigId, currentUser).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    @GetMapping("/applications/me")
    public List<ApplicationResponse> findMine(@AuthenticationPrincipal User currentUser) {
        return applicationService.findMine(currentUser).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    @PatchMapping("/applications/{id}/status")
    public ApplicationResponse updateStatus(@PathVariable Long id,
                                            @Valid @RequestBody ApplicationStatusUpdateRequest request,
                                            @AuthenticationPrincipal User currentUser) {
        JobApplication updated = applicationService.updateStatus(id, request.status(), currentUser);
        return ApplicationResponse.from(updated);
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        applicationService.withdraw(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}