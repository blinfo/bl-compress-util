package bl.compress.util.write;

import bl.compress.util.exception.CompressUtilException;
import bl.compress.util.Helper;
import bl.compress.util.Suffix;
import bl.compress.util.read.Decompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hl
 */
public class CompressorTest {

    public CompressorTest() {
    }

    @Test
    public void a_structur_with_non_ascii_characters_in_entry_names_should_handle_these() throws IOException {
        Suffix zip = Suffix.ZIP;
        File temp = Helper.createTestFile(zip);
        List<String> keys = new ArrayList<>(Decompressor.of(new FileInputStream(temp), zip).read().keySet());
        assertEquals("The expected value should be \"växter/örter.txt\"", "växter/örter.txt", keys.get(3));
    }

    @Test
    public void a_structur_with_non_ascii_characters_in_content_should_handle_these() throws IOException {
        Suffix zip = Suffix.ZIP;
        File temp = Helper.createTestFile(zip);
        byte[] value = Decompressor.of(new FileInputStream(temp), zip).read().get("växter/bär.txt");
        assertEquals("The expected value should be \"Hallon\nLingon\nBlåbär\"", "Hallon\nLingon\nBlåbär", new String(value));
    }

    @Test
    public void a_structur_should_be_preserved_intact_between_compression_and_decompression() throws IOException {
        Suffix zip = Suffix.ZIP;
        File temp = Helper.createTestFile(zip);
        Map<String, String> expectedMap = Helper.createContentMap().entrySet()
                .stream().collect(Collectors.toMap(e -> e.getKey(), e -> new String(e.getValue())));
        Map<String, String> resultMap = Decompressor.of(new FileInputStream(temp), zip).read().entrySet()
                .stream().collect(Collectors.toMap(e -> e.getKey(), e -> new String(e.getValue())));
        assertTrue("The map (converted to string values) should be equal to the expected map", expectedMap.equals(resultMap));
    }

    @Test
    public void empty_content_should_throw_exception() {
        File file= new File("empty-file.txt");
        String expectedMessage = "Content is empty";
        CompressUtilException ex = Assert.assertThrows("", CompressUtilException.class, () -> Compressor.of(Map.of(), file));
        assertEquals("Expected message should be " + expectedMessage, expectedMessage, ex.getMessage());
    }
    
    @Test
    public void attempt_to_write_an_exe_file_should_throw_exception() {
        File file = new File("exe-file.exe");
        String entryName = "abc.txt";
        byte[] content = "abc".getBytes();
        Suffix suffix = Suffix.EXE;
        String expectedMessage = "The file format \"exe\" is not supported";
        CompressUtilException ex = Assert.assertThrows("", CompressUtilException.class, () -> Compressor.of(Map.of(entryName, content), file, suffix));
        assertEquals("Expected message should be " + expectedMessage, expectedMessage, ex.getMessage());
    }
}
