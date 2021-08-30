package bl.compress.util.write;

import bl.compress.util.exception.CompressUtilException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hl
 */
class PlainFileWriter implements Compressor {

    private final byte[] content;
    private final File result;

    public PlainFileWriter(byte[] content, File result) {
        this.content = content;
        this.result = result;
    }

    @Override
    public File write() {
        try {
            Files.write(result.toPath(), content);
            Logger.getLogger(PlainFileWriter.class.getName()).log(Level.INFO, "Written to file: {0}", result.getPath());
            return result;
        } catch (IOException ex) {
            throw new CompressUtilException("Write to file '" + result.getPath() + "' failed", ex);
        }
    }
}
