package uz.test.project.example11_1.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.test.project.example11_1.demo.entities.Otp;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findOtpByUsername(String username);
}
