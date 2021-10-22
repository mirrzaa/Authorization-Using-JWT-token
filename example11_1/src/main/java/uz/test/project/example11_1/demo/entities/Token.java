package uz.test.project.example11_1.demo.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity(name = "user_tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accessToken;
    private String refreshToken;
    private Date expiryDate;
    private String username;
}
