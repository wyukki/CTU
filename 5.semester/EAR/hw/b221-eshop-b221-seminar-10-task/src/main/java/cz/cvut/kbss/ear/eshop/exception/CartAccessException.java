package cz.cvut.kbss.ear.eshop.exception;

/**
 * Indicates that an error occurred when trying to access or work with a user's cart.
 */
public class CartAccessException extends EarException {

    public CartAccessException(String message) {
        super(message);
    }
}
