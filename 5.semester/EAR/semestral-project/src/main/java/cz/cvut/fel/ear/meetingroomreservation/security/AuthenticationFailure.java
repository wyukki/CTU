package cz.cvut.fel.ear.meetingroomreservation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.ear.meetingroomreservation.security.model.LoginStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Type of Authentication failure
 */
@Service
public class AuthenticationFailure implements AuthenticationFailureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailure.class);

    private final ObjectMapper mapper;


    /**
     *  Instance of a new Authentication failure
     * @param mapper the mapper
     */

    @Autowired
    public AuthenticationFailure(ObjectMapper mapper){
        this.mapper = mapper;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException{
        LOG.debug("Login failed for user " + request.getParameter(SecurityConstants.USERNAME_PARAM) + ".");

        final LoginStatus status = new LoginStatus(false, null, exception.getMessage(), false);
        mapper.writeValue(response.getOutputStream(), status);
    }
}
