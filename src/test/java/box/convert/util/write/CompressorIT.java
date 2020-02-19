package box.convert.util.write;

import box.convert.util.Helper;
import box.convert.util.Suffix;
import box.convert.util.read.Decompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Test
    public void test_consistency_between_input_and_outpu_maps() {
        try {
            Map<String, byte[]> inputMap = Collections.singletonMap("test.txt", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.".getBytes());
            File temp = File.createTempFile("test-file", ".tgz");
            temp.deleteOnExit();
            File resultingTempFile = Compressor.of(inputMap, temp, Suffix.TGZ).toFile();
            Map<String, byte[]> outputMap = Decompressor.of(new FileInputStream(temp), Suffix.TGZ).read();
            assertTrue(resultingTempFile.getName().startsWith("test-file"),"Resulting file name should start with \"test-file\"");
            assertTrue(resultingTempFile.getName().endsWith(".tgz"),"Resulting file name should end with \".tgz\"");
            assertTrue(temp.exists(), "TempFile should have been created");
            assertTrue(temp.length() > 180, "TempFile length should exceed 180");
            assertTrue(outputMap.containsKey("test.txt"), "Output map should contain Entry.key \"test.txt\"");
            assertTrue(inputMap.size() == 1, "Input map should contain 1 entry");
            assertTrue(outputMap.size() == 1, "Output map should contain 1 entry");
            assertEquals(inputMap.keySet().iterator().next(), outputMap.keySet().iterator().next());
            assertTrue(Arrays.equals(inputMap.values().iterator().next(), outputMap.values().iterator().next()), "Value should be equal in input and output maps");
        } catch (IOException ex) {
            Logger.getLogger(CompressorIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
