package box.convert.util.write;

import box.convert.util.CompressUtilException;
import box.convert.util.Suffix;
import java.io.File;
import java.nio.file.Path;
import java.util.Map;

/**
 * Writer for file
 *
 * @author hl
 */
public interface Compressor {

    /**
     * Writes provided content to file.
     *
     * @return File
     */
    File write();

    /**
     * Writes provided content to file represented by Path.
     * <p>
     * Convenience method to create an archive file.<br>
     * The archive will be of the default suffix type.
     *
     * @param content Map&lt;String, byte[]&gt;
     * @param path Path
     * @return File
     */
    static File toFile(Map<String, byte[]> content, Path path) {
        return toFile(content, Suffix.DEFAULT, path);
    }

    /**
     * Writes provided content to file represented by Path.
     * <p>
     * Convenience method to create an archive file.<br>
     * The type of archive generated is specified by the suffix.
     *
     * @param content Map&lt;String, byte[]&gt;
     * @param suffix Suffix
     * @param path Path
     * @return File
     */
    static File toFile(Map<String, byte[]> content, Suffix suffix, Path path) {
        return of(content, path, suffix).write();
    }

    /**
     * Creates a Compressor.
     * <p>
     * The compressor's write-method will create an archive file of the default
     * suffix type.
     * <p>
     * The resulting file is specified by the provided path parameter.<br>
     * The archive will be of the default suffix type.
     *
     *
     * @param content Map&lt;String, byte[]&gt;
     * @param path Path
     * @return Compressor
     */
    static Compressor of(Map<String, byte[]> content, Path path) {
        Suffix suffix;
        if (path.toFile().getName().endsWith(Suffix.TAR_GZ.get())) {
            suffix = Suffix.TAR_GZ;
        } else {
            suffix = Suffix.find(path.toFile().getName().substring(path.toFile().getName().lastIndexOf(".") + 1)).orElse(Suffix.DEFAULT);
        }
        return of(content, path, suffix);

    }

    /**
     * Creates a Compressor.
     * <p>
     * The compressor's write-method will create an archive file of the default
     * suffix type.
     * <p>
     * The resulting file is specified by the provided path parameter.<br>
     * The type of archive generated is specified by the suffix.
     *
     *
     * @param content Map&lt;String, byte[]&gt;
     * @param suffix Suffix
     * @param path Path
     * @return Compressor
     * @throws CompressUtilException If the format specified by the suffix is
     * not writeable.
     */
    static Compressor of(Map<String, byte[]> content, Path path, Suffix suffix) {
        if (!suffix.isWriteable()) {
            throw new CompressUtilException("The file format \"" + suffix.get() + "\" is not supported");
        }
        switch (suffix) {
            case EAR:
            case JAR:
            case WAR:
                return new JarCompressor(content, path);
            case SEVEN_Z:
                return new SevenZCompressor(content, path);
            case TAR_GZ:
            case TGZ:
                return new TgzCompressor(content, path);
            case ZIP:
            default:
                return new ZipCompressor(content, path);
        }
    }
}
