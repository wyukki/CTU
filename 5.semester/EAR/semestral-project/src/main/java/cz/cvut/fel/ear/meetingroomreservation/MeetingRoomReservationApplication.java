package cz.cvut.fel.ear.meetingroomreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@EntityScan(basePackages = {"cz.cvut.fel.ear.meetingroomreservation.*"})
@ComponentScan(basePackages = {"cz.cvut.fel.ear.meetingroomreservation.*"})
@SpringBootApplication
public class MeetingRoomReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingRoomReservationApplication.class, args);
    }
}
