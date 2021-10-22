//package uz.test.project.example11.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.stereotype.Component;
//import uz.test.project.example11.dto.User;
//import uz.test.project.example11.repositories.UserRepository;
//
//import java.util.Optional;
//
//@Slf4j
//@Component
//public class SecurityUtils {
//    private SecurityContext securityContextHolder;
//    private final UserRepository userRepository;
//
//    public SecurityUtils(SecurityContext securityContextHolder, UserRepository userRepository) {
//        this.securityContextHolder = securityContextHolder;
//        this.userRepository = userRepository;
//    }
//
//    public Authentication getAuthentication() {
//        return securityContextHolder.getAuthentication();
//    }
//
//    public boolean isAdminUser() {
//        log.info("is Admin User");
//        Authentication authentication = securityContextHolder.getAuthentication();
//        Optional<User> userOptional = userRepository.findUserByUsername(authentication.getName());
//        if (userOptional.isPresent()) {
//            return authentication.getAuthorities().stream().anyMatch(userAuthority -> userAuthority.equals("ROlE_ADMIN"));
//        }
//        return false;
//    }
//}
