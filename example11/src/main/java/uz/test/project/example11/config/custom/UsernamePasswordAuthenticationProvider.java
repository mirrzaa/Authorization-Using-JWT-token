package uz.test.project.example11.config.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import uz.test.project.example11.component.AuthenticationServerProxy;
import uz.test.project.example11.dto.User;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerProxy serverProxy;
    private final HttpServletResponse httpServletResponse;
    @Value("${auth.server.base.url}")
    private String baseUrl;

    public UsernamePasswordAuthenticationProvider(AuthenticationServerProxy serverProxy, HttpServletResponse httpServletResponse) {
        this.serverProxy = serverProxy;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String[] names = username.split(" ");
        String password = String.valueOf(authentication.getCredentials());
        try {
            if (password.startsWith("Bearer")) {
                String url = baseUrl + "/users";
                ResponseEntity<User[]> array = serverProxy.getUsers(url);
                httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), array);
            } else {
                String code = serverProxy.sendAuth(names[0], names[1], password);
                httpServletResponse.setHeader("otp_code", code);
            }
            return new UsernamePasswordAuthenticationToken(username, password);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(aClass);
    }
}
