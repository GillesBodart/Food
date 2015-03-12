package bodart.food.db.exceptions;

/**
 *
 * @author Gilles
 */
public class FoodMajorException extends Exception {

    /**
     * Creates a new instance of <code>FoodMajorException</code> without detail
     * message.
     */
    public FoodMajorException() {
    }

    /**
     * Constructs an instance of <code>FoodMajorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodMajorException(String msg) {
        super(msg);
    }
}
