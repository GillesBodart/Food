package bodart.food.db.exceptions;

/**
 *
 * @author Gilles
 */
public class FoodMinorException extends Exception {

    /**
     * Creates a new instance of <code>FoodMinorException</code> without detail
     * message.
     */
    public FoodMinorException() {
    }

    /**
     * Constructs an instance of <code>FoodMinorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodMinorException(String msg) {
        super(msg);
    }
}
