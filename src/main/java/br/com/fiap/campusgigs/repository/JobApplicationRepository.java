package br.com.fiap.campusgigs.repository;

import br.com.fiap.campusgigs.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findAllByGigId(Long gigId);
    List<JobApplication> findAllByApplicantId(Long applicantId);
    Optional<JobApplication> findByGigIdAndApplicantId(Long gigId, Long applicantId);
}