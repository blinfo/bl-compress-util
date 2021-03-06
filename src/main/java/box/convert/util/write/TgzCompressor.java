package box.convert.util.write;

import box.convert.util.CompressUtilException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author hl
 */
class TgzCompressor extends AbstractCompressor {

    public TgzCompressor(Map<String, byte[]> content, Path path) {
        super(content, path);
    }

    @Override
    protected File process() {
        TarArchiveOutputStream outputStream = createOutputStream();
        content.entrySet().forEach(entry -> compressToTarArchive(entry.getKey(), entry.getValue(), outputStream));
        IOUtils.closeQuietly(outputStream);
        Logger.getLogger(JarCompressor.class.getName()).log(Level.INFO, "Written to file: {0}", resultPath);
        return resultPath.toFile();
    }

    private TarArchiveOutputStream createOutputStream() {
        try {
            return new TarArchiveOutputStream(new GzipCompressorOutputStream(new FileOutputStream(resultPath.toFile())));
        } catch (FileNotFoundException ex) {
            throw new CompressUtilException("Could not find file: " + resultPath, ex);
        } catch (IOException ex) {
            throw new CompressUtilException("Could not create output stream", ex);
        }
    }

    private void compressToTarArchive(String name, byte[] content, TarArchiveOutputStream output) {
        try {
            TarArchiveEntry entry = new TarArchiveEntry(name);
            entry.setSize(content.length);
            output.putArchiveEntry(entry);
            IOUtils.copy(new ByteArrayInputStream(content), output);
            output.closeArchiveEntry();
        } catch (IOException ex) {
            throw new CompressUtilException("Could not write entry \"" + name + "\" to output stream ", ex);
        }
    }
}
