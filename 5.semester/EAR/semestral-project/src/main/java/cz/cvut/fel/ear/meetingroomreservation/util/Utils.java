package cz.cvut.fel.ear.meetingroomreservation.util;

import java.util.Objects;

public class Utils {
    public static void checkParametersNotNull(Object... objects) {
        Objects.requireNonNull(objects);
        for (Object object : objects) {
            Objects.requireNonNull(object);
        }
    }
}
