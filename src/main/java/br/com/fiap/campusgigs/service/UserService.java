package br.com.fiap.campusgigs.service;

import br.com.fiap.campusgigs.exception.ForbiddenActionException;
import br.com.fiap.campusgigs.exception.ResourceNotFoundException;
import br.com.fiap.campusgigs.model.Address;
import br.com.fiap.campusgigs.model.User;
import br.com.fiap.campusgigs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressService addressService;

    @Transactional(readOnly = true)
    public List<User> findAll(User currentUser) {
        assertAdmin(currentUser);
        return userRepository.findAll();
    }

    @Transactional
    public void delete(Long userId, User currentUser) {
        assertAdmin(currentUser);

        if (currentUser.getId().equals(userId)) {
            throw new ForbiddenActionException("Um administrador não pode remover a própria conta por esta rota");
        }

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id " + userId));

        userRepository.delete(target);
    }

    @Transactional
    public User updateAddress(User currentUser, String cep) {
        Address address = addressService.resolveByCep(cep);
        currentUser.setAddress(address);
        return userRepository.save(currentUser);
    }

    private void assertAdmin(User currentUser) {
        if (!currentUser.isAdmin()) {
            throw new ForbiddenActionException("Apenas administradores podem executar esta ação");
        }
    }
}