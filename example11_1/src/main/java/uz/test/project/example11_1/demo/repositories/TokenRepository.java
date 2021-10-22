package uz.test.project.example11_1.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.test.project.example11_1.demo.entities.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUsername(String username);
}
