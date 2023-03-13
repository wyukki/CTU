package cz.cvut.fel.ear.meetingroomreservation.service;

import cz.cvut.fel.ear.meetingroomreservation.repository.CustomerDao;
import cz.cvut.fel.ear.meetingroomreservation.repository.ReservationDao;
import cz.cvut.fel.ear.meetingroomreservation.repository.RoomDao;
import cz.cvut.fel.ear.meetingroomreservation.model.*;
import cz.cvut.fel.ear.meetingroomreservation.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final RoomDao roomDao;
    private final ReservationDao reservationDao;

    private final PasswordEncoder encoder;

    @Autowired
    public CustomerService(CustomerDao customerDao, RoomDao roomDao, ReservationDao reservationDao, PasswordEncoder encoder) {
        this.customerDao = customerDao;
        this.roomDao = roomDao;
        this.reservationDao = reservationDao;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<Customer> findAllCustomers() {
        return customerDao.findAll();
    }

    @Transactional(readOnly = true)
    public Customer findCustomerById(Long id) {
        return customerDao.find(id);
    }

    @Transactional
    public void persist(Customer customer) {
        Objects.requireNonNull(customer);
        customer.encodePassword(encoder);
        customerDao.persist(customer);
    }

    @Transactional
    public void setWorkerIsPrior(Worker worker) {
        Utils.checkParametersNotNull(worker);
        worker.setPriority(Priority.PRIOR);
        customerDao.update(worker);
    }

    @Transactional
    public void setWorkerIsNotPrior(Worker worker) {
        Utils.checkParametersNotNull(worker);
        worker.setPriority(Priority.NON_PRIOR);
        customerDao.update(worker);
    }
}
