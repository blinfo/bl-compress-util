package bl.compress.util.exception;

/**
 *
 * @author Håkan Lidén
 */
public class EmptyContentException extends CompressUtilException {

    public EmptyContentException() {
        super("Content is empty");
    }

    public EmptyContentException(String message) {
        super(message);
    }

    public EmptyContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyContentException(Throwable cause) {
        super(cause);
    }

}
