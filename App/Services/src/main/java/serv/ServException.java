package serv;

public class ServException extends Exception {
    public ServException() {
    }

    public ServException(String message) {
        super(message);
    }

    public ServException(String message, Throwable cause) {
        super(message, cause);
    }

}
