package uz.test.project.example11_1.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RoleToUserForm {
    private String userName;
    private String roleName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleToUserForm{" +
                "userName='" + userName + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
