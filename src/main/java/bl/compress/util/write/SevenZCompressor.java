package bl.compress.util.write;

import bl.compress.util.exception.CompressUtilException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

/**
 *
 * @author hl
 */
class SevenZCompressor extends AbstractCompressor {

    public SevenZCompressor(Map<String, byte[]> content, File result) {
        super(content, result);
    }

    @Override
    protected File process() {
        try (SevenZOutputFile output = new SevenZOutputFile(result)) {
            content.entrySet().forEach(entry -> compressToFile(entry.getKey(), entry.getValue(), output));
            Logger.getLogger(SevenZCompressor.class.getName()).log(Level.INFO, "Written to file: {0}", result.getPath());
            return result;
        } catch (IOException ex) {
            throw new CompressUtilException("Could not create compressed file", ex);
        }
    }

    private void compressToFile(String name, byte[] content, SevenZOutputFile output) {
        try {
            File file = new File(name);
            SevenZArchiveEntry entry = output.createArchiveEntry(file, name);
            output.putArchiveEntry(entry);
            write(content, output);
        } catch (IOException ex) {
            throw new CompressUtilException("Could not create entry: " + name, ex);
        }
    }

    private void write(byte[] content, SevenZOutputFile output) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(content)) {
            byte[] bytes = new byte[1024];
            int count;
            while ((count = input.read(bytes)) > 0) {
                output.write(bytes, 0, count);
            }
            output.closeArchiveEntry();
        } catch (IOException ex) {
            throw new CompressUtilException("Could not create input stream", ex);
        }
    }
}
