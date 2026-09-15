package br.com.fiap.campusgigs.service;

import br.com.fiap.campusgigs.dto.ApplicationRequest;
import br.com.fiap.campusgigs.exception.BusinessRuleException;
import br.com.fiap.campusgigs.exception.ForbiddenActionException;
import br.com.fiap.campusgigs.exception.ResourceNotFoundException;
import br.com.fiap.campusgigs.model.*;
import br.com.fiap.campusgigs.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final GigService gigService;

    @Transactional
    public JobApplication apply(Long gigId, ApplicationRequest request, User currentUser) {
        Gig gig = gigService.findById(gigId);

        if (gig.isOwnedBy(currentUser)) {
            throw new BusinessRuleException("Você não pode se candidatar ao seu próprio freela");
        }

        if (gig.getStatus() != GigStatus.OPEN) {
            throw new BusinessRuleException("Este freela não está mais aberto para candidaturas");
        }

        applicationRepository.findByGigIdAndApplicantId(gigId, currentUser.getId())
                .ifPresent(a -> {
                    throw new BusinessRuleException("Você já se candidatou a este freela");
                });

        JobApplication application = JobApplication.builder()
                .gig(gig)
                .applicant(currentUser)
                .message(request.message())
                .status(ApplicationStatus.PENDING)
                .build();

        return applicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> findByGig(Long gigId, User currentUser) {
        Gig gig = gigService.findById(gigId);

        if (!gig.isOwnedBy(currentUser) && !currentUser.isAdmin()) {
            throw new ForbiddenActionException("Apenas o dono do freela ou um administrador pode ver as candidaturas");
        }

        return applicationRepository.findAllByGigId(gigId);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> findMine(User currentUser) {
        return applicationRepository.findAllByApplicantId(currentUser.getId());
    }

    @Transactional
    public JobApplication updateStatus(Long applicationId, ApplicationStatus newStatus, User currentUser) {
        JobApplication application = findById(applicationId);
        Gig gig = application.getGig();

        if (!gig.isOwnedBy(currentUser) && !currentUser.isAdmin()) {
            throw new ForbiddenActionException("Apenas o dono do freela ou um administrador pode decidir sobre a candidatura");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessRuleException("Esta candidatura já foi respondida");
        }

        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }

    @Transactional
    public void withdraw(Long applicationId, User currentUser) {
        JobApplication application = findById(applicationId);

        boolean isOwner = application.getApplicant().getId().equals(currentUser.getId());

        if (!isOwner && !currentUser.isAdmin()) {
            throw new ForbiddenActionException("Você só pode retirar suas próprias candidaturas");
        }

        if (isOwner && !currentUser.isAdmin() && application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessRuleException("Não é possível retirar uma candidatura que já foi respondida");
        }

        applicationRepository.delete(application);
    }

    private JobApplication findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidatura não encontrada com id " + id));
    }
}