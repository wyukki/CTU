package cz.cvut.fel.ear.meetingroomreservation.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken implements Principal {

    private final CustomerDetails customerDetails;


    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, CustomerDetails customerDetails){
        super(authorities);
        this.customerDetails = customerDetails;
        super.setAuthenticated(true);
        super.setDetails(customerDetails);

    }

    @Override
    public String getCredentials() {
        return customerDetails.getPassword();
    }

    @Override
    public CustomerDetails getPrincipal() {
        return customerDetails;
    }
}
