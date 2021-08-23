package bl.compress.util.write;

import bl.compress.util.CompressUtilException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Creates GZIP file of provided data.
 *
 * <ul>Input File:
 * <li>Provide name for uncompressed file in which case the .gz suffix will be
 * added</li>
 * <li>Provide name for the resulting file according to gz standard, <br>e. g.
 * <code>testfile.txt.gz</code></li>
 * </ul>
 *
 * @author hl
 */
class GzCompressor implements Compressor {

    private final byte[] content;
    private final File resultFile;

    /**
     * Creates GZIP file of provided data.
     *
     * <ul>Input File:
     * <li>Provide name for uncompressed file in which case the .gz suffix will
     * be added</li>
     * <li>Provide name for the resulting file according to gz standard, <br>e.
     * g. <code>testfile.txt.gz</code></li>
     * </ul>
     *
     * @param content
     * @param result
     */
    public GzCompressor(byte[] content, File result) {
        this.content = content;
        this.resultFile = createGzFile(result);
    }

    @Override
    public File write() {
        try {
            GzipCompressorOutputStream output = createOutputStream();
            IOUtils.copy(new ByteArrayInputStream(content), output);
            IOUtils.closeQuietly(output);
            Logger.getLogger(GzCompressor.class.getName()).log(Level.INFO, "Written to file: {0}", resultFile.getPath());
            return resultFile;
        } catch (IOException ex) {
            throw new CompressUtilException("Could not create output stream", ex);
        }
    }

    private static File createGzFile(File result) {
        if (result.getAbsolutePath().endsWith(".gz")) {
            return result;
        }
        return new File(result.getAbsolutePath() + ".gz");
    }

    private GzipCompressorOutputStream createOutputStream() {
        try {
            return new GzipCompressorOutputStream(new FileOutputStream(resultFile));
        } catch (FileNotFoundException ex) {
            throw new CompressUtilException("Could not find file: " + resultFile.getAbsolutePath(), ex);
        } catch (IOException ex) {
            throw new CompressUtilException("Could not create output stream", ex);
        }
    }
}
