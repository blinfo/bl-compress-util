package bl.compress.util;

import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * Supported file suffices
 *
 * @author hl
 */
public enum Suffix {
    CSV,
    EAR,
    EXE,
    GIF,
    GZ,
    JAR,
    JPG,
    JSON,
    PDF,
    PNG,
    SEVEN_Z {
        @Override
        public String get() {
            return "7z";
        }
    },
    TAR_GZ,
    TGZ,
    TXT,
    WAR,
    ZIP,
    DEFAULT;

    /**
     * Return a string representation of the suffix.
     *
     * Returns a string corresponding to the common file suffix of the file
     * type, e.g. "zip", "7z", "tar.gz"
     *
     * @return String
     */
    public String get() {
        return name().toLowerCase().replaceAll("_", ".");
    }

    public Boolean isWriteable() {
        return !this.equals(EXE);
    }

    /**
     * Finder method for Suffices.
     *
     * Returns an Optional of found suffix, or an empty Optional if no suffix is
     * found.
     *
     * @param string String
     * @return Optional Suffix
     */
    public static Optional<Suffix> find(String string) {
        if (string == null || string.isBlank()) {
            return Optional.empty();
        }
        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(string) || s.get().equalsIgnoreCase(string.replaceAll("_", ".")))
                .findFirst();
    }
}
