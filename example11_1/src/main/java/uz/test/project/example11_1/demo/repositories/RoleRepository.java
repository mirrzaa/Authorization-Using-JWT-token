package uz.test.project.example11_1.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.test.project.example11_1.demo.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
