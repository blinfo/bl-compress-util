package box.convert.util.read;

import box.convert.util.CompressUtilException;
import box.convert.util.Suffix;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * Reader for file
 *
 * @author hl
 */
public interface Decompressor {

    /**
     * Reads content of input stream into a map.
     * <p>
     * Returns a map where the Keys are the names of the archived files and the
     * Values are the file content as a byte arrays.
     *
     * @return Map
     */
    Map<String, byte[]> read();

    /**
     * Reads content of file into a map.
     * <p>
     * Convenicence method for Decompressing an archive file.<br>
     * Returns a map where the Keys are the names of the archived files and the
     * Values are the file content as a byte arrays.
     *
     * @param file File
     * @return Map
     */
    public static Map<String, byte[]> fromFile(File file) {
        Suffix suffix = file.getName().endsWith(".tar.gz")
                ? Suffix.TAR_GZ
                : Suffix.find(file.getName().substring(file.getName().lastIndexOf(".") + 1))
                        .orElse(Suffix.DEFAULT);
        try {
            return Decompressor.of(new FileInputStream(file), suffix).read();
        } catch (FileNotFoundException ex) {
            throw new CompressUtilException("Could not read file: " + file, ex);
        }
    }

    /**
     * Returns a Decompressor.
     * <p>
     * Creates a Decompressor from the input stream.<br>
     * The Decompressor will be for the default suffix type.
     *
     * @param input InputStream
     * @return Decompressor
     */
    public static Decompressor of(InputStream input) {
        return of(input, Suffix.DEFAULT);
    }

    /**
     * Returns a Decompressor.
     * <p>
     * Creates a Decompressor from the input stream and the provided suffix.<br>
     * The suffix indicates which type of compression the input stream contains.
     *
     * @param input InputStream
     * @param suffix Suffix
     * @return Decompressor
     */
    public static Decompressor of(InputStream input, Suffix suffix) {
        switch (suffix) {
            case EXE:
                return WinZipDecompressor.from(input);
            case GZ:
                return GzDecompressor.from(input);
            case EAR:
            case JAR:
            case WAR:
                return JarDecompressor.from(input);
            case SEVEN_Z:
                return SevenZDecompressor.from(input);
            case TAR_GZ:
            case TGZ:
                return TgzDecompressor.from(input);
            case ZIP:
            default:
                return ZipDecompressor.from(input);
        }
    }

}
