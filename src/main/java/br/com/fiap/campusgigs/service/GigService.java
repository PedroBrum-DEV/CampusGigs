package br.com.fiap.campusgigs.service;

import br.com.fiap.campusgigs.dto.GigRequest;
import br.com.fiap.campusgigs.exception.ForbiddenActionException;
import br.com.fiap.campusgigs.exception.ResourceNotFoundException;
import br.com.fiap.campusgigs.model.Gig;
import br.com.fiap.campusgigs.model.GigStatus;
import br.com.fiap.campusgigs.model.User;
import br.com.fiap.campusgigs.repository.GigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GigService {

    private final GigRepository gigRepository;

    @Transactional(readOnly = true)
    public List<Gig> findAll() {
        return gigRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Gig findById(Long id) {
        return gigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Freela não encontrado com id " + id));
    }

    @Transactional
    public Gig create(GigRequest request, User currentUser) {
        Gig gig = Gig.builder()
                .title(request.title())
                .description(request.description())
                .category(request.category())
                .price(request.price())
                .status(GigStatus.OPEN)
                .owner(currentUser)
                .build();

        return gigRepository.save(gig);
    }

    @Transactional
    public Gig update(Long id, GigRequest request, User currentUser) {
        Gig gig = findById(id);
        assertOwnerOrAdmin(gig, currentUser, "editar");

        gig.setTitle(request.title());
        gig.setDescription(request.description());
        gig.setCategory(request.category());
        gig.setPrice(request.price());

        return gigRepository.save(gig);
    }

    @Transactional
    public Gig close(Long id, User currentUser) {
        Gig gig = findById(id);
        assertOwnerOrAdmin(gig, currentUser, "encerrar");
        gig.setStatus(GigStatus.CLOSED);
        return gigRepository.save(gig);
    }

    @Transactional
    public void delete(Long id, User currentUser) {
        Gig gig = findById(id);
        assertOwnerOrAdmin(gig, currentUser, "apagar");
        gigRepository.delete(gig);
    }

    private void assertOwnerOrAdmin(Gig gig, User currentUser, String action) {
        if (!gig.isOwnedBy(currentUser) && !currentUser.isAdmin()) {
            throw new ForbiddenActionException(
                    "Você não tem permissão para " + action + " este freela: apenas o dono ou um administrador pode fazer isso"
            );
        }
    }
}