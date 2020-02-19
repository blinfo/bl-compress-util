package box.convert.util.write;

import box.convert.util.CompressUtilException;
import box.convert.util.Suffix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Writer for file
 *
 * @author hl
 */
public interface Compressor {

    /**
     * Writes provided content to the resultFile.
     *
     * @return File
     */
    File toFile();

    default OutputStream write() {
        File file = toFile();
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            throw new CompressUtilException("Could not create output stream from file: " + file, ex);
        }
    }

    /**
     * Creates archive file with content from map.
     * <p>
     * The type of archive will be determined by the resultFile parameter.
     *
     * @param content
     * @param resultFile
     * @return
     */
    public static Compressor of(Map<String, byte[]> content, File resultFile) {
        Suffix suffix;
        if (resultFile.getName().endsWith(Suffix.TAR_GZ.get())) {
            suffix = Suffix.TAR_GZ;
        } else {
            suffix = Suffix.find(resultFile.getName().substring(resultFile.getName().lastIndexOf(".") + 1)).orElse(Suffix.DEFAULT);
        }
        return of(content, resultFile, suffix);

    }

    /**
     * Creates archive file with content from map.
     * <p>
     * The type of archive generated is specified by the suffix.
     *
     *
     * @param content
     * @param suffix
     * @param resultFile
     * @return
     */
    public static Compressor of(Map<String, byte[]> content, File resultFile, Suffix suffix) {
        if (!suffix.isWriteable()) {
            throw new CompressUtilException("The file format \"" + suffix.get() + "\" is not supported");
        }
        switch (suffix) {
            case EAR:
            case JAR:
            case WAR:
                return new JarCompressor(content, resultFile);
            case SEVEN_Z:
                return new SevenZCompressor(content, resultFile);
            case TAR_GZ:
            case TGZ:
                return new TgzCompressor(content, resultFile);
            case ZIP:
            default:
                return new ZipCompressor(content, resultFile);
        }
    }
}
