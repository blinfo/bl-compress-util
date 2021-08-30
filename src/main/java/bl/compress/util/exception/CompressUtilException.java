package bl.compress.util.exception;

/**
 *
 * @author hl
 */
public class CompressUtilException extends RuntimeException {

    public CompressUtilException(String message) {
        super(message);
    }

    public CompressUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompressUtilException(Throwable cause) {
        super(cause);
    }

}
