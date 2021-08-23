package bl.compress.util.read;

import bl.compress.util.CompressUtilException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

/**
 *
 * @author hl
 */
class SevenZDecompressor extends AbstractDecompressor {

    private SevenZDecompressor(InputStream input) {
        super(input);
    }

    public static SevenZDecompressor from(InputStream input) {
        return new SevenZDecompressor(input);
    }

    @Override
    protected void populateResultMap() {
        // 7z-files created in Ubuntu/Linuz causes an exception.
        File tempFile = new File("temp-" + LocalDateTime.now().toString() + ".7z");
        try {
            Files.copy(input, tempFile.toPath());
            try (SevenZFile sevenZFile = new SevenZFile(tempFile)) {
                SevenZArchiveEntry entry;
                while ((entry = sevenZFile.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }
                    byte[] content = new byte[(int) entry.getSize()];
                    sevenZFile.read(content, 0, content.length);
                    result.put(entry.getName(), content);
                }
            } finally {
                tempFile.delete();
            }
        } catch (IOException e) {
            tempFile.delete();
            throw new CompressUtilException(e);
        }
    }
}
