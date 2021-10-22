package uz.test.project.example11.config.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import uz.test.project.example11.component.AuthenticationServerProxy;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AuthenticationServerProxy serverProxy;
    private final HttpServletResponse response;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String[] names = username.split(" ");
        String code = String.valueOf(authentication.getCredentials());
        try {
            Object accessToken = serverProxy.sendOTP(names[0], names[1], code);
            if (accessToken != null) {
                log.info("Access token is received");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), accessToken);
                return new OtpAuthentication(username, code);
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials");
        }
        return new OtpAuthentication(username, code);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OtpAuthentication.class.isAssignableFrom(aClass);
    }
}
