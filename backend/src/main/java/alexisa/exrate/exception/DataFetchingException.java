package alexisa.exrate.exception;

public class DataFetchingException extends RuntimeException {
    public DataFetchingException(String message) {
        super(message);
    }

    public DataFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
