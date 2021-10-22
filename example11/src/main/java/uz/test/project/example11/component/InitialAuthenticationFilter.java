package uz.test.project.example11.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.test.project.example11.config.custom.OtpAuthentication;
import uz.test.project.example11.config.custom.UsernamePasswordAuthentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class InitialAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationManager manager;

    public InitialAuthenticationFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String name = httpServletRequest.getHeader("name");
        String username = httpServletRequest.getHeader("username");
        String password = httpServletRequest.getHeader("password");
        String code = httpServletRequest.getHeader("code");

        log.info(httpServletRequest.getRequestURI() + httpServletRequest.getServletPath());
        if (code == null) {
            Authentication a = new UsernamePasswordAuthentication(name + " " + username, password);
            manager.authenticate(a);
        } else {
            Authentication a = new OtpAuthentication(name + " " + username, code);
            a = manager.authenticate(a);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login");
    }
}
