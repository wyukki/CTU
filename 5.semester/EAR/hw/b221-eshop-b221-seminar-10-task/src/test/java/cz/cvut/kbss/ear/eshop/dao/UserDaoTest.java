package cz.cvut.kbss.ear.eshop.dao;

import cz.cvut.kbss.ear.eshop.EShopApplication;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.TestConfiguration;
import cz.cvut.kbss.ear.eshop.model.User;
import cz.cvut.kbss.ear.eshop.service.SystemInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// For explanatory comments, see ProductDaoTest
@DataJpaTest
@ComponentScan(basePackageClasses = EShopApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SystemInitializer.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = TestConfiguration.class)})
public class UserDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao sut;

    @Test
    public void findByUsernameReturnsPersonWithMatchingUsername() {
        final User user = Generator.generateUser();
        em.persist(user);

        final User result = sut.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void findByUsernameReturnsNullForUnknownUsername() {
        assertNull(sut.findByUsername("unknownUsername"));
    }
}
