package bl.compress.util.write;

import bl.compress.util.Suffix;
import bl.compress.util.exception.EmptyContentException;
import bl.compress.util.exception.UnsupportedFileFormatException;
import java.io.File;
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
    File write();

    public static Compressor of(byte[] content, File resultFile) {
        Suffix suffix = Suffix.find(resultFile.getName().substring(resultFile.getName().lastIndexOf(".") + 1)).orElse(Suffix.TXT);
        return of(content, resultFile, suffix);
    }

    /**
     * Creates a file from single content.
     *
     * @param content
     * @param suffix
     * @param resultFile
     * @return
     */
    public static Compressor of(byte[] content, File resultFile, Suffix suffix) {
        if (!suffix.isWriteable()) {
            throw new UnsupportedFileFormatException(suffix);
        }
        switch (suffix) {
            case GIF:
            case JPG:
            case PDF:
            case PNG:
                return new PlainFileWriter(content, resultFile);
            case CSV:
            case JSON:
            case TXT:
                return new PlainFileWriter(content, resultFile);
            case GZ:
                return new GzCompressor(content, resultFile);
            default:
                return Compressor.of(Collections.singletonMap(resultFile.getName(), content), resultFile, suffix);
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
        if (content.isEmpty()) {
            throw new EmptyContentException();
        }
        if (!suffix.isWriteable()) {
            throw new UnsupportedFileFormatException(suffix);
        }
        switch (suffix) {
            case GIF:
            case JPG:
            case PNG:
                return of(content.values().iterator().next(), resultFile, suffix);
            case TXT:
            case CSV:
            case GZ:
                return of(content.values().iterator().next(), resultFile, suffix);
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
                return new ZipCompressor(content, resultFile);
            case DEFAULT:
                return content.keySet().size() == 1 
                        ? new PlainFileWriter(content.values().iterator().next(), resultFile) 
                        : new ZipCompressor(content, resultFile);
            default:
                return new ZipCompressor(content, resultFile);
        }
    }
}
