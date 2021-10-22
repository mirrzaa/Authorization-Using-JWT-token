package uz.test.project.example11_1.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.test.project.example11_1.demo.dto.RoleDto;
import uz.test.project.example11_1.demo.dto.RoleToUserForm;
import uz.test.project.example11_1.demo.dto.UserDto;
import uz.test.project.example11_1.demo.entities.Otp;
import uz.test.project.example11_1.demo.entities.Role;
import uz.test.project.example11_1.demo.entities.Token;
import uz.test.project.example11_1.demo.entities.User;
import uz.test.project.example11_1.demo.repositories.TokenRepository;
import uz.test.project.example11_1.demo.repositories.UserRepository;
import uz.test.project.example11_1.demo.services.UserService;
import uz.test.project.example11_1.demo.utils.JSend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @GetMapping("/users")
    public JSend getUsers() {
        log.info(request.getServletPath() + " is executing");
        ObjectNode users = objectMapper.createObjectNode();
        users.putPOJO("users", userService.getUsers());
        return JSend.success(users);
    }

    @PostMapping("/user/add")
    public JSend addUser(@RequestBody UserDto user) {
        log.info(request.getServletPath() + " is executing");
        userService.addUser(user);
        return JSend.success();
    }

    @PostMapping("/user/auth")
    public JSend auth(@RequestBody UserDto user) {
        log.info(request.getServletPath() + " is executing");
        return JSend.success(userService.auth(user));
    }

    @PostMapping("/role/save")
    public JSend saveRole(@RequestBody RoleDto role) {
        log.info(request.getServletPath() + " is executing");
        return JSend.success(userService.saveRole(role));
    }

    @PostMapping("/role/add/to/user")
    public JSend saveRoleToUser(@RequestBody RoleToUserForm form) {
        log.info(request.getServletPath() + " is executing");
        userService.addRoleToUser(form.getUserName(), form.getRoleName());
        return JSend.success();
    }

    @PostMapping("/otp/check")
    public JSend check(@RequestBody Otp otp, HttpServletRequest request, HttpServletResponse servletResponse) {
        log.info(request.getServletPath() + " is executing");
        if (userService.check(otp)) {
            Optional<Token> isTokenExist = tokenRepository.findByUsername(otp.getUsername());
            if (!isTokenExist.isPresent()) {
                ObjectNode token = userService.generateAccessToken(otp, request);
                servletResponse.setStatus(HttpServletResponse.SC_OK);
                return JSend.success(token);
            } else {
                Optional<Token> isTokenEx = tokenRepository.findByUsername(otp.getUsername());
                Token tokenEntity = isTokenEx.get();
                servletResponse.setStatus(HttpServletResponse.SC_OK);
                ObjectNode token = objectMapper.createObjectNode();
                token.put("access_token", tokenEntity.getAccessToken());
                token.put("refresh_token", tokenEntity.getRefreshToken());
                token.put("expiry_date", tokenEntity.getExpiryDate().toString());
                return JSend.success(token);
            }
        }
        servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }

    @GetMapping("/refresh/token")
    public JSend refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        ObjectNode objectToken = objectMapper.createObjectNode();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                Optional<User> userOptional = userRepository.findUserByUsername(username);
                if (!userOptional.isPresent())
                    return JSend.error("User not found");

                Date expiryDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
                User user = userOptional.get();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                //access token
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(expiryDate)
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                //refresh token
                String refresh_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);

                objectToken.put("access_token", access_token);
                objectToken.put("refresh_token", refresh_token);
                objectToken.put("expiry_date", expiryDate.toString());
                return JSend.success(objectToken);

            } catch (Exception e) {
                log.error("Error  logging  in {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(403);
                ObjectNode tokens = objectMapper.createObjectNode();
                tokens.put("error_message", e.getMessage());
                return JSend.error("", null, tokens);
            }

        }
        return JSend.error("Refresh token is missing");
    }
}


