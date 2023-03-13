package cz.cvut.fel.ear.meetingroomreservation.repository;

import cz.cvut.fel.ear.meetingroomreservation.model.Worker;
import org.springframework.stereotype.Repository;

@Repository
public class WorkerDao extends BaseDao<Worker> {

    public WorkerDao() {
        super(Worker.class);
    }
}
