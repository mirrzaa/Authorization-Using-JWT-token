package uz.test.project.example11_1.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.test.project.example11_1.demo.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username);
}
