package bl.compress.util.exception;

import bl.compress.util.Suffix;

/**
 *
 * @author Håkan Lidén
 */
public class UnsupportedFileFormatException extends CompressUtilException {

    public UnsupportedFileFormatException(String fileExtension) {
        super("The file format \"" + fileExtension + "\" is not supported");
    }

    public UnsupportedFileFormatException(Suffix suffix) {
        this(suffix.get());
    }
}
