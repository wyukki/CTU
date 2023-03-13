package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Admin;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao extends BaseDao<Admin> {

    public AdminDao() {
        super(Admin.class);
    }
}
