package box.convert.util.write;

import box.convert.util.Helper;
import box.convert.util.Suffix;
import box.convert.util.read.Decompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hl
 */
public class CompressorIT {

    public CompressorIT() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void a_structur_with_non_ascii_characters_in_entry_names_should_handle_these() throws IOException {
        Suffix zip = Suffix.ZIP;
        File temp = Helper.createTestFile(zip);
        List<String> keys = new ArrayList<>(Decompressor.of(new FileInputStream(temp), zip).read().keySet());
        assertEquals("växter/örter.txt", keys.get(3), "The expected value should be \"växter/örter.txt\"");
    }

    @Test
    public void a_structur_with_non_ascii_characters_in_content_should_handle_these() throws IOException {
        Suffix zip = Suffix.ZIP;
        File temp = Helper.createTestFile(zip);
        byte[] value = Decompressor.of(new FileInputStream(temp), zip).read().get("växter/bär.txt");
        assertEquals("Hallon\nLingon\nBlåbär", new String(value), "The expected value should be \"Hallon\nLingon\nBlåbär\"");
    }

    @Test
    public void a_structur_should_be_preserved_intact_between_compression_and_decompression() throws IOException {
        Suffix zip = Suffix.ZIP;
        File temp = Helper.createTestFile(zip);
        Map<String, String> expectedMap = Helper.createContentMap().entrySet()
                .stream().collect(Collectors.toMap(e -> e.getKey(), e -> new String(e.getValue())));
        Map<String, String> resultMap = Decompressor.of(new FileInputStream(temp), zip).read().entrySet()
                .stream().collect(Collectors.toMap(e -> e.getKey(), e -> new String(e.getValue())));
        assertTrue(expectedMap.equals(resultMap), "The map (converted to string values) should be equal to the expected map");
    }
}
