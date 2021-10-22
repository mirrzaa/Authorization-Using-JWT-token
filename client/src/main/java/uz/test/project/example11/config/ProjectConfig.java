package uz.test.project.example11.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import uz.test.project.example11.component.InitialAuthenticationFilter;
import uz.test.project.example11.component.JwtAuthenticationFilter;
import uz.test.project.example11.config.custom.OtpAuthenticationProvider;
import uz.test.project.example11.config.custom.UsernamePasswordAuthenticationProvider;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private final InitialAuthenticationFilter initialAuthenticationFilter;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final OtpAuthenticationProvider otpAuthenticationProvider;

    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    public ProjectConfig(InitialAuthenticationFilter initialAuthenticationFilter, JwtAuthenticationFilter jwtAuthenticationFilter, OtpAuthenticationProvider otpAuthenticationProvider, UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider) {
        this.initialAuthenticationFilter = initialAuthenticationFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.otpAuthenticationProvider = otpAuthenticationProvider;
        this.usernamePasswordAuthenticationProvider = usernamePasswordAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterAt(initialAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(otpAuthenticationProvider)
                .authenticationProvider(usernamePasswordAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.requestFactory(new MyRequestFactorySupplier())
                .setConnectTimeout(Duration.ofMillis(30000))
                .setReadTimeout(Duration.ofMillis(30000))
                .build();
    }

    class MyRequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {

        @Override
        public ClientHttpRequestFactory get() {
            CloseableHttpClient httpClient = HttpClients
                    .custom()
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(45000);
            requestFactory.setConnectTimeout(45000);
            requestFactory.setConnectionRequestTimeout(45000);
            return requestFactory;
        }
    }
}
