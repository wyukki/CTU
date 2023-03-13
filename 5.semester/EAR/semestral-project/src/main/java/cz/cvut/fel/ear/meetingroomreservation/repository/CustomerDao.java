package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class CustomerDao extends BaseDao<Customer> {

    public CustomerDao() {
        super(Customer.class);
    }

    public Customer findByUsername(String username) {
        try {
            return em.createNamedQuery("Customer.findByUsername", Customer.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }
}
