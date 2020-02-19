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
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author hl
 */
class ZipCompressor extends AbstractCompressor {

    public ZipCompressor(Map<String, byte[]> content, Path result) {
        super(content, result);
    }

    @Override
    protected File process() {
        ZipArchiveOutputStream zipFile = createZipFile();
        content.entrySet().forEach(entry -> {
            try {
                compressToZipFile(entry.getKey(), entry.getValue(), zipFile);
            } catch (IOException ex) {
                throw new CompressUtilException("Could not create ZipEntries", ex);
            }
        });
        IOUtils.closeQuietly(zipFile);
        Logger.getLogger(JarCompressor.class.getName()).log(Level.INFO, "Written to file: {0}", resultPath);
        return resultPath.toFile();
    }

    private ZipArchiveOutputStream createZipFile() {
        try {
            ZipArchiveOutputStream outputStream = new ZipArchiveOutputStream(new FileOutputStream(resultPath.toFile()));
            outputStream.setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS);
            return outputStream;
        } catch (FileNotFoundException ex) {
            throw new CompressUtilException("Could not find file: " + resultPath, ex);
        }
    }

    private void compressToZipFile(String name, byte[] content, ZipArchiveOutputStream output) throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        output.putArchiveEntry(entry);
        ByteArrayInputStream input = new ByteArrayInputStream(content);
        IOUtils.copy(input, output);
        IOUtils.closeQuietly(input);
        output.closeArchiveEntry();
    }
}
