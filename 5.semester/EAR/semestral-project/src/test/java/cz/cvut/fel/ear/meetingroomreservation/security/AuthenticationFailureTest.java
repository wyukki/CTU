package cz.cvut.fel.ear.meetingroomreservation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.ear.meetingroomreservation.config.AppConfig;
import cz.cvut.fel.ear.meetingroomreservation.security.model.LoginStatus;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationFailureTest {

    private final ObjectMapper mapper = new AppConfig().objectMapper();

    private AuthenticationFailure authenticationFailure;


    @BeforeEach
    public void setUp(){
        this.authenticationFailure = new AuthenticationFailure(mapper);
    }


    @Test
    public void setAuthenticationFailureReturnsLoginStatusWithErrorInfo() throws Exception{
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final String message = "Username not found";
        final AuthenticationException exception = new UsernameNotFoundException(message);
        authenticationFailure.onAuthenticationFailure(request, response, exception);
        final LoginStatus status = mapper.readValue(response.getContentAsString(), LoginStatus.class);
        assertFalse(status.isSuccess());
        assertFalse(status.isLogged());
        assertNull(status.getUsername());
        assertEquals(message, status.getErrorMessage());
    }

}
