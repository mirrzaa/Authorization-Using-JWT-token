package uz.test.project.example11_1.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.test.project.example11_1.demo.dto.RoleDto;
import uz.test.project.example11_1.demo.dto.UserDto;
import uz.test.project.example11_1.demo.entities.Otp;
import uz.test.project.example11_1.demo.entities.Role;
import uz.test.project.example11_1.demo.entities.User;
import uz.test.project.example11_1.demo.repositories.OtpRepository;
import uz.test.project.example11_1.demo.repositories.RoleRepository;
import uz.test.project.example11_1.demo.repositories.UserRepository;
import uz.test.project.example11_1.demo.utils.GenerateCodeUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final RoleRepository roleRepository;
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    private final ObjectMapper objectMapper;


    /*
    ------------------------------------------------------------------------------
                                    User Methods
    ------------------------------------------------------------------------------
     */

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public void addUser(UserDto user) {
        log.info("Saving new user {} to database", user.getName());
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setName(user.getName());
        userRepository.save(newUser);
    }

    /*
    ------------------------------------------------------------------------------
                                    User authentication
    ------------------------------------------------------------------------------
     */

    @Transactional(readOnly = true)
    @Override
    public boolean check(Otp otpValidate) {
        log.info("Validate otp by username");
        Optional<Otp> otpOptional = otpRepository.findOtpByUsername(otpValidate.getUsername());
        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            if (otp.getCode().equals(otpValidate.getCode()))
                return true;
        }
        return false;
    }

    @Override
    public String auth(UserDto user) {
        log.info("Authorizing user for otp code");
        Optional<User> o = userRepository.findUserByUsername(user.getUsername());
        String code = null;
        if (o.isPresent()) {
            User user1 = o.get();
            if (passwordEncoder.matches(user.getPassword(), user1.getPassword()))
                code = renewOtp(user);
            else {
                throw new BadCredentialsException("Bad credentials.");
            }
        } else {
            log.info("Creating new user");
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setUsername(user.getUsername());
            newUser.setName(user.getName());
            code = renewOtp(user);
            userRepository.save(newUser);
        }
        return code;
    }

    public String renewOtp(UserDto user) {
        log.info("Renewing Otp");
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(user.getUsername());
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
            otpRepository.save(otp);
        } else {
            Otp otp = new Otp();
            otp.setUsername(user.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
        return code;
    }

    /*
    ------------------------------------------------------------------------------
                                    User role
    ------------------------------------------------------------------------------
     */

    @Override
    public Role saveRole(RoleDto role) {
        log.info("Saving new role {} to database", role.getName());
        Role newRole = new Role();
        newRole.setName(role.getName());
        return roleRepository.save(newRole);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        log.info("Adding role {} to user {} to the database", roleName, userName);
        Optional<User> userOptional = userRepository.findUserByUsername(userName);
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (userOptional.isPresent() && roleOptional.isPresent()) {
            userOptional.get().getRoles().add(roleOptional.get());
        }
    }

     /*
    ------------------------------------------------------------------------------
                                    Uer token
    ------------------------------------------------------------------------------
     */

    @Override
    public ObjectNode generateAccessToken(Otp otpValidate, HttpServletRequest httpServletRequest) {
        log.info("Generating access token");
        ObjectNode response = objectMapper.createObjectNode();
        Optional<Otp> otpOptional = otpRepository.findOtpByUsername(otpValidate.getUsername());
        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            Optional<User> userOptional = userRepository.findUserByUsername(otp.getUsername());
            User user = userOptional.orElse(null);
            Collection<Role> roles = user.getRoles();

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            Date expiryDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
            String accessToken = JWT.create()
                    .withSubject(otp.getUsername())
                    .withExpiresAt(expiryDate)
                    .withIssuer(httpServletRequest.getRequestURL().toString())
                    .withClaim("roles", authorities)
                    .sign(algorithm);

            String refreshToken = JWT.create()
                    .withSubject(otp.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                    .withIssuer(httpServletRequest.getRequestURL().toString())
                    .sign(algorithm);

            response.put("access_token", accessToken);
            response.put("refresh_token", refreshToken);
            response.put("expiry_date", expiryDate.toString());
            return null;
        }
        return response;
    }
}
