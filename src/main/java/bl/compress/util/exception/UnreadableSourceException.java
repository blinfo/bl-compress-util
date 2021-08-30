package bl.compress.util.exception;

/**
 *
 * @author Håkan Lidén
 */
public class UnreadableSourceException extends CompressUtilException {

    public UnreadableSourceException(Throwable cause) {
        this("Could not read source", cause);
    }

    public UnreadableSourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
