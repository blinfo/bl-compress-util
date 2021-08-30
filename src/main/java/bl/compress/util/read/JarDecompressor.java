package bl.compress.util.read;

import bl.compress.util.exception.UnreadableSourceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author hl
 */
class JarDecompressor extends AbstractDecompressor {

    private JarDecompressor(InputStream input) {
        super(input);
    }

    public static JarDecompressor from(InputStream input) {
        return new JarDecompressor(input);
    }

    @Override
    protected void populateResultMap() {
        try {
            JarArchiveInputStream jarInput = new JarArchiveInputStream(input);
            JarArchiveEntry tarGzEntry = jarInput.getNextJarEntry();
            while (tarGzEntry != null) {
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    IOUtils.copy(jarInput, outputStream);
                    result.put(tarGzEntry.getName(), outputStream.toByteArray());
                }
                tarGzEntry = jarInput.getNextJarEntry();
            }
            IOUtils.closeQuietly(input);
        } catch (IOException ex) {
            throw new UnreadableSourceException(ex);
        }
    }
}
