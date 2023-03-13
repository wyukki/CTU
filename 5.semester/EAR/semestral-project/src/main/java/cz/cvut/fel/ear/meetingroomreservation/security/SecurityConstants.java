package cz.cvut.fel.ear.meetingroomreservation.security;

public class SecurityConstants {
    SecurityConstants() {throw new AssertionError();}

    public static final String SESSION_COOKIE_NAME = "EAR_JSESSIONID";

    public static final String REMEMBER_ME_COOKIE_NAME = "remember-me";

    public static final String USERNAME_PARAM = "username";

    public static final String PASSWORD_PARAM = "password";

    public static final String COOKIE_URI = "/";

    public static final int SESSION_TIMEOUT = 60 * 60 ;
}
