package bl.compress.util.read;

import bl.compress.util.exception.UnreadableSourceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author hl
 */
class PlainFileReader extends AbstractDecompressor {

    private final String name;

    private PlainFileReader(InputStream input, String name) {
        super(input);
        this.name = name;
    }

    public static PlainFileReader from(InputStream input) {
        return PlainFileReader.of(input, "temp-file.txt");
    }

    public static PlainFileReader of(InputStream input, String name) {
        return new PlainFileReader(input, name);
    }

    @Override
    void populateResultMap() {
        String entryName = name.contains("/") ? name.substring(name.lastIndexOf("/") + 1) : name;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.copy(input, outputStream);
            result.put(entryName, outputStream.toByteArray());
            IOUtils.closeQuietly(input);
        } catch (IOException ex) {
            throw new UnreadableSourceException(ex);
        }
    }
}
