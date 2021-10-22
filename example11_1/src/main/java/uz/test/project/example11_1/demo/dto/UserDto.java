package uz.test.project.example11_1.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {
    private String name;
    private String username;
    private String password;
    private String code;
}
