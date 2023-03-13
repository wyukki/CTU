package cz.cvut.kbss.ear.eshop.environment;

import cz.cvut.kbss.ear.eshop.model.Product;
import cz.cvut.kbss.ear.eshop.model.User;

import java.util.Random;

public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static int randomInt(int max) {
        return RAND.nextInt(max);
    }

    public static int randomInt(int min, int max) {
        assert min >= 0;
        assert min < max;

        int result;
        do {
            result = randomInt(max);
        } while (result < min);
        return result;
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User generateUser() {
        final User user = new User();
        user.setFirstName("FirstName" + randomInt());
        user.setLastName("LastName" + randomInt());
        user.setUsername("username" + randomInt() + "@kbss.felk.cvut.cz");
        user.setPassword(Integer.toString(randomInt()));
        return user;
    }

    public static Product generateProduct() {
        final Product p = new Product();
        p.setName("Product" + randomInt());
        p.setAmount(1);
        p.setPrice(1.0);
        return p;
    }
}
