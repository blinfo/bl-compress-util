package box.convert.util.read;

import box.convert.util.CompressUtilException;
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

    private GzDecompressor(InputStream input) {
        super(input);
    }

    public static GzDecompressor from(InputStream input) {
        return new GzDecompressor(input);
    }

    @Override
    void populateResultMap() {
        try {
            GzipCompressorInputStream gzipInput = new GzipCompressorInputStream(input);
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                IOUtils.copy(gzipInput, output);
                String entryName = "content.gz";
                result.put(entryName, output.toByteArray());
            }
            IOUtils.closeQuietly(gzipInput);
        } catch (IOException ex) {
            throw new CompressUtilException("Could not read source", ex);
        }
    }
}
