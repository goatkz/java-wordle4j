package exceptions;

public class InvalidWordException extends Exception {

    public InvalidWordException(final String message) {
        super(message);
    }
}