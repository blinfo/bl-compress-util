package bl.compress.util.read;

import bl.compress.util.exception.UnreadableSourceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author hl
 */
class GzDecompressor extends AbstractDecompressor {

    private final String name;

    private GzDecompressor(InputStream input, String name) {
        super(input);
        this.name = name;
    }

    public static GzDecompressor from(InputStream input) {
        return new GzDecompressor(input, "content");
    }

    public static GzDecompressor of(InputStream input, String name) {
        return new GzDecompressor(input, name);
    }

    @Override
    void populateResultMap() {
        try {
            GzipCompressorInputStream gzipInput = new GzipCompressorInputStream(input);
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                IOUtils.copy(gzipInput, output);
                result.put(name, output.toByteArray());
            }
            IOUtils.closeQuietly(gzipInput);
        } catch (IOException ex) {
            throw new UnreadableSourceException(ex);
        }
    }
}
