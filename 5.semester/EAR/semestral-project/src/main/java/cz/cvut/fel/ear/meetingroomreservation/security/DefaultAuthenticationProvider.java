package cz.cvut.fel.ear.meetingroomreservation.security;


import cz.cvut.fel.ear.meetingroomreservation.security.model.AuthenticationToken;
import cz.cvut.fel.ear.meetingroomreservation.security.model.CustomerDetails;
import cz.cvut.fel.ear.meetingroomreservation.service.security.CustomerDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticationProvider.class);

    private final CustomerDetailsService customerDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultAuthenticationProvider(CustomerDetailsService customerDetailsService,
                                         PasswordEncoder passwordEncoder){
        this.customerDetailsService = customerDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();

        final CustomerDetails customerDetails = (CustomerDetails) customerDetailsService.loadUserByUsername(username);
        final String password = (String) authentication.getCredentials();

        if (!passwordEncoder.matches(password, customerDetails.getPassword())){
            throw new BadCredentialsException("Provided credentials do not match!");
        }
        LOG.debug("Authentication user {}.", username);
        return SecurityUtils.setCurrentCustomer(customerDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication) ||
                AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
