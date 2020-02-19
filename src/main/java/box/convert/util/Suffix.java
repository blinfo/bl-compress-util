package box.convert.util;

import java.util.Optional;

/**
 *
 * Supported file suffices
 *
 * @author hl
 */
public enum Suffix {
    EAR,
    EXE,
    GZ,
    JAR,
    SEVEN_Z {
        @Override
        public String get() {
            return "7z";
        }
    },
    TAR_GZ,
    TGZ,
    WAR,
    ZIP;
    public static final Suffix DEFAULT = ZIP;

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
        if (string != null && !string.isEmpty()) {
            for (Suffix s : values()) {
                if (s.name().equalsIgnoreCase(string) || s.get().equalsIgnoreCase(string.replaceAll("_", "."))) {
                    return Optional.of(s);
                }
            }
        }
        return Optional.empty();
    }
}
