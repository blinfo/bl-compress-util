package box.convert.util;

import box.convert.util.write.Compressor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author hl
 */
public class Helper {

    private static final List<String> LINES = getLines();

    private Helper() {
    }

    public static Map<String, byte[]> generateMap(int numberOfEntries) {
        Map<String, byte[]> contentMap = new LinkedHashMap<>();
        for (int i = 0; i < numberOfEntries; i++) {
            int entryId = i + 1;
            String name = "entries/entry-" + entryId + ".txt";
            byte[] content = LINES.get(i % LINES.size()).getBytes();
            contentMap.put(name, content);
        }
        return contentMap;
    }

    public static File generateFile(int numberOfEntries, Suffix suffix) {
        try {
            File file = File.createTempFile("testfile", suffix.get());
            file.deleteOnExit();
            Compressor.of(generateMap(numberOfEntries), file.toPath(), suffix).write();
            return file;
        } catch (IOException ex) {
            throw new CompressUtilException(ex);
        }
    }

    public static InputStream generateInputStream(int numberOfEntries, Suffix suffix) {
        try {
            return new FileInputStream(generateFile(numberOfEntries, suffix));
        } catch (FileNotFoundException ex) {
            throw new CompressUtilException(ex);
        }
    }

    public static File createTestFile(Suffix suffix) throws IOException {
        Map<String, byte[]> content = createContentMap();
        File temp = Compressor.of(content, File.createTempFile("non-ascii-entries", "." + suffix.get()).toPath()).write();
        temp.deleteOnExit();
        return temp;
    }

    public static Map<String, byte[]> createContentMap() {
        Map<String, byte[]> content = new LinkedHashMap<>();
        content.put("djur/apor.txt", "Bonobo\nChimpans\nGorilla\nOrangutang".getBytes(StandardCharsets.UTF_8));
        content.put("växter/bär.txt", "Hallon\nLingon\nBlåbär".getBytes(StandardCharsets.UTF_8));
        content.put("växter/frukter.txt", "Banan\nCitron\nMango".getBytes(StandardCharsets.UTF_8));
        content.put("växter/örter.txt", "Banan\nKamomill\nTimjan".getBytes(StandardCharsets.UTF_8));
        content.put("djur/katter.txt", "Lejon\nLeopard\nLo\nPanter\nPuma\nTiger".getBytes(StandardCharsets.UTF_8));
        return content;
    }

    private static final List<String> getLines() {
        return new BufferedReader(new InputStreamReader(Helper.class.getResourceAsStream("/plain-text-file.txt"))).lines().collect(Collectors.toList());
    }
}
