package box.convert.util.read;

import box.convert.util.Suffix;
import java.io.InputStream;
import java.util.Map;

/**
 * Reader for file
 *
 * @author hl
 */
public interface Decompressor {

    /**
     * Reads content of file.
     *
     * Returns a map where the Keys are the names of the files and the Values
     * are the file content as a byte arrays.
     *
     * @return Map
     */
    Map<String, byte[]> read();
    
    
    /**
     * Returns a Decompressor.
     *
     * Creates a Decompressor from the input stream.
     *
     *
     * @param input InputStream
     * @return Decompressor
     */
    public static Decompressor of(InputStream input) {
        return of(input, Suffix.DEFAULT);
    }

    /**
     * Returns a Decompressor.
     *
     * Creates a Decompressor from the input stream and the provided suffix.
     *
     *
     * @param input InputStream
     * @param suffix Suffix
     * @return Decompressor
     */
    public static Decompressor of(InputStream input, Suffix suffix) {
        return of(input, suffix, "");
    }

    /**
     * Returns a Decompressor.
     *
     * Creates a Decompressor from the input stream, the provided suffix and
     * filename.
     *
     *
     * @param input InputStream
     * @param suffix Suffix
     * @param filename String used by some file-reader implementations
     * @return Decompressor
     */
    public static Decompressor of(InputStream input, Suffix suffix, String filename) {
        switch (suffix) {
            case EXE:
                return WinZipDecompressor.from(input);
            case GZ:
                if (filename != null) {
                    return GzDecompressor.of(input, filename);
                }
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
                return ZipDecompressor.from(input);
            case CSV:
            case TXT:
                if (filename != null) {
                    return PlainFileReader.of(input, filename);
                }
                return PlainFileReader.from(input);
            default:
                return PlainFileReader.of(input, filename);
        }
    }

}
