package cz.cvut.fel.ear.meetingroomreservation.config;

import cz.cvut.fel.ear.meetingroomreservation.security.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private static final String[] COOKIES_TO_DESTROY = {
            SecurityConstants.SESSION_COOKIE_NAME,
            SecurityConstants.REMEMBER_ME_COOKIE_NAME
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .httpBasic();
        return http.build();
    }

}
