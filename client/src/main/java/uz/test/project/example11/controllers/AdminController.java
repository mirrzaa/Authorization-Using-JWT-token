package uz.test.project.example11.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.test.project.example11.services.AdminService;
import uz.test.project.example11.utils.JSend;
import uz.test.project.example11.utils.SecurityUtils;

@Slf4j
@RestController
@RequestMapping("/api")
public class AdminController {
    private final SecurityUtils securityUtils;
    private AdminService adminService;

    public AdminController(SecurityUtils securityUtils, AdminService adminService) {
        this.securityUtils = securityUtils;
        this.adminService = adminService;
    }

    @PostMapping("/add/role")
    public JSend addRole(String role) {
        try {
            if (securityUtils.isAdminUser())
                return adminService.addRole(role);

            return JSend.error("Only Admin users can post data");
        } catch (Exception e) {
            return JSend.error("Something went wrong");
        }
    }

    @PostMapping("/add/roletouser")
    public JSend addRoleToUser(String userName, String role) {
        try {
            if (securityUtils.isAdminUser())
                return adminService.addRoleToUser(userName, role);

            return JSend.error("Only admin users can post data");
        } catch (Exception e) {
            return JSend.error("Something went wrong");
        }
    }
}
