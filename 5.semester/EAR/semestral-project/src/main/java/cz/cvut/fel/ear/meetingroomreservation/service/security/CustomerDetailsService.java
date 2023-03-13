package cz.cvut.fel.ear.meetingroomreservation.service.security;

import cz.cvut.fel.ear.meetingroomreservation.repository.CustomerDao;
import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import cz.cvut.fel.ear.meetingroomreservation.security.model.CustomerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerDao customerDao;


    @Autowired
    public CustomerDetailsService(CustomerDao customerDao){
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Customer customer = customerDao.findByUsername(username);

        if (customer == null){
            throw new UsernameNotFoundException("User with name {"+ username + "} was not found!");
        }
        return new CustomerDetails(customer);
    }
}
