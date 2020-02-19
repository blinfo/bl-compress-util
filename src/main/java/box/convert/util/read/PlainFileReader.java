package box.convert.util.read;

import box.convert.util.CompressUtilException;
import box.convert.util.Suffix;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author hl
 */
class PlainFileReader extends AbstractDecompressor {

    private final Suffix suffix;

    private PlainFileReader(InputStream input, Suffix suffix) {
        super(input);
        this.suffix = suffix;
    }

    public static PlainFileReader from(InputStream input) {
        return of(input, Suffix.TXT);
    }
    
    public static PlainFileReader of(InputStream input, Suffix suffix) {
        return new PlainFileReader(input, suffix);
    }

    @Override
    void populateResultMap() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(input, outputStream);
            String entryName = "content";
            if (suffix != null) {
                entryName += "." + suffix.get();
            }
            result.put(entryName, outputStream.toByteArray());
            IOUtils.closeQuietly(input);
        } catch (IOException ex) {
            throw new CompressUtilException("Could not read source", ex);
        }
    }
}
