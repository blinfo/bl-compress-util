package box.convert.util.write;

import box.convert.util.CompressUtilException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author hl
 */
class JarCompressor extends AbstractCompressor {

    public JarCompressor(Map<String, byte[]> content, File result) {
        super(content, result);
    }

    @Override
    protected File process() {
        JarArchiveOutputStream jarFile = createJarFile();
        content.entrySet().forEach(entry -> {
            try {
                compressToJarFile(entry.getKey(), entry.getValue(), jarFile);
            } catch (IOException ex) {
                throw new CompressUtilException("Could not create JarEntries", ex);
            }
        });
        IOUtils.closeQuietly(jarFile);
        Logger.getLogger(JarCompressor.class.getName()).log(Level.INFO, "Written to file: {0}", result.getPath());
        return result;
    }

    private JarArchiveOutputStream createJarFile() {
        try {
            JarArchiveOutputStream outputStream = new JarArchiveOutputStream(new FileOutputStream(result));
            outputStream.setCreateUnicodeExtraFields(JarArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS);
            return outputStream;
        } catch (FileNotFoundException ex) {
            throw new CompressUtilException("Could not find file: " + result.getAbsolutePath(), ex);
        }
    }

    private void compressToJarFile(String name, byte[] content, JarArchiveOutputStream output) throws IOException {
        JarArchiveEntry entry = new JarArchiveEntry(name);
        output.putArchiveEntry(entry);
        ByteArrayInputStream input = new ByteArrayInputStream(content);
        IOUtils.copy(input, output);
        IOUtils.closeQuietly(input);
        output.closeArchiveEntry();
    }
}
