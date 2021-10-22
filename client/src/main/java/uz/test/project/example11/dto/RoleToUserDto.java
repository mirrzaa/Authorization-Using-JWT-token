package uz.test.project.example11.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@RequiredArgsConstructor
public class RoleToUserDto {
    private String username;
    private String role;

    public RoleToUserDto(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
