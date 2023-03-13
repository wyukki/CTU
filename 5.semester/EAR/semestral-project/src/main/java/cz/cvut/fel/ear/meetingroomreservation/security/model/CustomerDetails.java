package cz.cvut.fel.ear.meetingroomreservation.security.model;

import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomerDetails implements UserDetails {

    private final Set<GrantedAuthority> authorities;
    private final transient Customer customer;


    public CustomerDetails(Customer customer) {
        Objects.requireNonNull(customer);
        this.customer = customer;
        this.authorities = new HashSet<>();
        addUserRole();
    }

    /**
     *
     * @param customer customer
     * @param authorities authorities
     */

    public CustomerDetails(Customer customer, Collection<GrantedAuthority> authorities){
        Objects.requireNonNull(customer);
        Objects.requireNonNull(authorities);
        this.customer = customer;
        this.authorities = new HashSet<>();
        addUserRole();
        this.authorities.addAll(authorities);
    }

    /**
     * add user role
     */
    public void addUserRole(){
        authorities.add(new SimpleGrantedAuthority(customer.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void eraseCredentials() {
        customer.erasePassword();
    }
}
