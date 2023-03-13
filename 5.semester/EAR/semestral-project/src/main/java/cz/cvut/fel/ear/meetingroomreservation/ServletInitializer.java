package cz.cvut.fel.ear.meetingroomreservation;

import cz.cvut.fel.ear.meetingroomreservation.MeetingRoomReservationApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MeetingRoomReservationApplication.class);
    }

}
