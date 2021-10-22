package uz.test.project.example11.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import uz.test.project.example11.dto.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationServerProxy {
    private RestTemplate restTemplate;
    private HttpServletResponse httpServletResponse;

    @Value("${auth.server.base.url}")
    private String baseUrl;

    public AuthenticationServerProxy(RestTemplate restTemplate, HttpServletResponse httpServletResponse) {
        this.restTemplate = restTemplate;
        this.httpServletResponse = httpServletResponse;
    }

    public String sendAuth(String name, String username, String password) {
        String url = baseUrl + "/user/auth";
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);

        HttpEntity request = new HttpEntity<>(user);
        ResponseEntity<String> code = restTemplate.postForEntity(url, request, String.class);
        String generatedCode = code.getBody();

        return generatedCode;
    }

    public Object sendOTP(String name, String username, String code) throws IOException {
        String url = baseUrl + "/otp/check";
        if (ObjectUtils.isEmpty(username) && ObjectUtils.isEmpty(code))
            return null;

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setCode(code);

        HttpEntity request = new HttpEntity<>(user);
        ResponseEntity<Object> token = restTemplate.postForEntity(url, request, Object.class);
        return token.getBody();
    }

    public ResponseEntity<User[]> getUsers(String url) {
        return restTemplate.getForEntity(url, User[].class);
    }
}
