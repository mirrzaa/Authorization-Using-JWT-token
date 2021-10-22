package uz.test.project.example11_1.demo.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import uz.test.project.example11_1.demo.dto.RoleDto;
import uz.test.project.example11_1.demo.dto.UserDto;
import uz.test.project.example11_1.demo.entities.Otp;
import uz.test.project.example11_1.demo.entities.Role;
import uz.test.project.example11_1.demo.entities.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    void addUser(UserDto user);

    String auth(UserDto user);

    boolean check(Otp otpValidate);

    List<User> getUsers();

    Role saveRole(RoleDto role);

    void addRoleToUser(String userName, String roleName);

    ObjectNode generateAccessToken(Otp otpValidate, HttpServletRequest request);
}

