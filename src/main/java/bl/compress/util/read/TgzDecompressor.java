package bl.compress.util.read;

import bl.compress.util.exception.UnreadableSourceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

/**
 *
 * @author hl
 */
class TgzDecompressor extends AbstractDecompressor {

    private TgzDecompressor(InputStream input) {
        super(input);
    }

    public static TgzDecompressor from(InputStream input) {
        return new TgzDecompressor(input);
    }

    @Override
    protected void populateResultMap() {
        try {
            TarArchiveInputStream tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(input));
            TarArchiveEntry tarGzEntry = tarInput.getNextTarEntry();
            byte[] buffer = new byte[1024];
            while (tarGzEntry != null) {
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    int length;
                    while ((length = tarInput.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    result.put(tarGzEntry.getName(), outputStream.toByteArray());
                }
                tarGzEntry = tarInput.getNextTarEntry();
            }
        } catch (IOException ex) {
            throw new UnreadableSourceException(ex);
        }
    }
}
