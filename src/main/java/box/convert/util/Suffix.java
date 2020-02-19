package box.convert.util;

import java.util.Optional;

/**
 *
 * Supported file suffices
 *
 * @author hl
 */
public enum Suffix {

    /**
     * <u>E</u>nterprise <u>A</u>pplication A<u>r</u>chive.
     */
    EAR,

    /**
     * <u>Exe</u>cutable.
     * <p>
     * Currently only readable.
     * 
     */
    EXE,

    /**
     * <u>GZ</u>ip.
     */
    GZ,
    /**
     * <u>J</u>ava <u>Ar</u>chive.
     */
    JAR,
    /**
     * <u>7Z</u>
     */
    SEVEN_Z {
        @Override
        public String get() {
            return "7z";
        }
    },
    /**
     * <u>Tar</u> <u>Gz</u>ip
     */
    TAR_GZ,
    /**
     * <u>T</u>ar <u>Gz</u>ip
     */
    TGZ,
    /**
     * <u>W</u>ebb <u>A</u>pplication <u>R</u>esource.
     */
    WAR,
    /**
     * <u>Zip</u>-archive
     */
    ZIP;
    public static final Suffix DEFAULT = ZIP;

    /**
     * Return a string representation of the suffix.
     * <p>
     * Returns a string corresponding to the common file suffix of the file
     * type, e.g. "zip", "7z", "tar.gz"
     *
     * @return String
     */
    public String get() {
        return name().toLowerCase().replaceAll("_", ".");
    }

    /**
     * Tells whether the suffix is of a writeable format.
     * <p>
     * Shows if the the Compressor can handle files with this suffix.<br>
     * Currently the only non-writeable format is "EXE".
     *
     * @return Boolean
     */
    public Boolean isWriteable() {
        return !this.equals(EXE);
    }

    /**
     * Finder method for Suffices.
     * <p>
     * Returns an Optional of found suffix, or an empty Optional if no suffix is
     * found.
     *
     * @param string String
     * @return Optional Suffix
     */
    public static Optional<Suffix> find(String string) {
        if (string != null && !string.isEmpty()) {
            for (Suffix s : values()) {
                if (s.name().equalsIgnoreCase(string) 
                        || s.get().equalsIgnoreCase(string.replaceAll("_", "."))) {
                    return Optional.of(s);
                }
            }
        }
        return Optional.empty();
    }
}
