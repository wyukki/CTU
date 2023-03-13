package cz.cvut.fel.ear.meetingroomreservation.security;

import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import cz.cvut.fel.ear.meetingroomreservation.security.model.AuthenticationToken;
import cz.cvut.fel.ear.meetingroomreservation.security.model.CustomerDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.Objects;

public class SecurityUtils {

    public static Customer getCurrentCustomer(){
        final SecurityContext context = SecurityContextHolder.getContext();
        Objects.requireNonNull(context);
        final CustomerDetails customerDetails = (CustomerDetails) context.getAuthentication().getDetails();
        return customerDetails.getCustomer();
    }

    public static CustomerDetails getCustomerDetails(){
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication().getDetails() instanceof CustomerDetails){
            return (CustomerDetails) context.getAuthentication().getDetails();
        }
        return null;
    }

    public static AuthenticationToken setCurrentCustomer(CustomerDetails customerDetails){
        final AuthenticationToken token = new AuthenticationToken(customerDetails.getAuthorities(), customerDetails);
        token.setAuthenticated(true);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
        return token;
    }

    public static boolean isAuthenticatedAnonymously(){
        return getCustomerDetails() == null;
    }
}
