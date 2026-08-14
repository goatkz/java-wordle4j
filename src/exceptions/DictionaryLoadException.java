package exceptions;

public class DictionaryLoadException extends Exception {

    public DictionaryLoadException(final String message) {
        super(message);
    }

    public DictionaryLoadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}