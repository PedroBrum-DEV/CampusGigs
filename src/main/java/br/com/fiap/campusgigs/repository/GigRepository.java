package br.com.fiap.campusgigs.repository;

import br.com.fiap.campusgigs.model.Gig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GigRepository extends JpaRepository<Gig, Long> {
    List<Gig> findAllByOwnerId(Long ownerId);
    List<Gig> findAllByCategoryIgnoreCase(String category);
}