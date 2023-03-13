package cz.cvut.fel.ear.meetingroomreservation.model;


import javax.persistence.*;

@Entity
@DiscriminatorValue("ROLE_WORKER")
public class Worker extends Customer {

    @Enumerated(value = EnumType.STRING)
    @Column
    private Priority priority;

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}