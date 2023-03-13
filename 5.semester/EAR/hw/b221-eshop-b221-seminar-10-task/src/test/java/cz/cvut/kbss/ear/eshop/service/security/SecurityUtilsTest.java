package cz.cvut.kbss.ear.eshop.service.security;

import cz.cvut.kbss.ear.eshop.environment.Environment;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.model.User;
import cz.cvut.kbss.ear.eshop.security.SecurityUtils;
import cz.cvut.kbss.ear.eshop.security.model.UserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityUtilsTest {

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = Generator.generateUser();
        user.setId(Generator.randomInt());
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getCurrentUserReturnsCurrentlyLoggedInUser() {
        Environment.setCurrentUser(user);
        final User result = SecurityUtils.getCurrentUser();
        assertEquals(user, result);
    }

    @Test
    public void getCurrentUserDetailsReturnsUserDetailsOfCurrentlyLoggedInUser() {
        Environment.setCurrentUser(user);
        final UserDetails result = SecurityUtils.getCurrentUserDetails();
        assertNotNull(result);
        assertTrue(result.isEnabled());
        assertEquals(user, result.getUser());
    }

    @Test
    public void getCurrentUserDetailsReturnsNullIfNoUserIsLoggedIn() {
        assertNull(SecurityUtils.getCurrentUserDetails());
    }
}
