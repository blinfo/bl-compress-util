package box.convert.util.read;

import box.convert.util.Helper;
import box.convert.util.Suffix;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hl
 */
public class DecompressorIT {

    public DecompressorIT() {
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
    public void factory_for_TGZ_should_create_a_TgzDecompressor() {
        Decompressor decompressor = Decompressor.of(getEmptyTextInputStream(), Suffix.TGZ);
        assertEquals(TgzDecompressor.class.getName(), decompressor.getClass().getName());
    }

    @Test
    public void factory_for_TAR_GZ_should_create_a_TgzDecompressor() {
        Decompressor decompressor = Decompressor.of(getEmptyTextInputStream(), Suffix.TAR_GZ);
        assertEquals(TgzDecompressor.class.getName(), decompressor.getClass().getName());
    }

    @Test
    public void factory_for_ZIP_should_create_a_ZipDecompressor() {
        Decompressor decompressor = Decompressor.of(getEmptyTextInputStream(), Suffix.ZIP);
        assertEquals(ZipDecompressor.class.getName(), decompressor.getClass().getName());
    }

    @Test
    public void factory_for_7Z_should_create_a_SevenZipDecompressor() {
        Decompressor decompressor = Decompressor.of(getEmptyTextInputStream(), Suffix.SEVEN_Z);
        assertEquals(SevenZDecompressor.class.getName(), decompressor.getClass().getName());
    }

    @Test
    public void factory_without_Suffix_should_create_a_PlainFileReader() {
        Decompressor decompressor = Decompressor.of(getEmptyTextInputStream());
        assertEquals(PlainFileReader.class.getName(), decompressor.getClass().getName());
    }

    @Test
    public void factory_with_map_for_ZIP_should_create_a_ZipDecompressor() {
        Decompressor decompressor = Decompressor.of(Helper.generateInputStream(3, Suffix.ZIP), Suffix.ZIP);
        assertEquals(ZipDecompressor.class.getName(), decompressor.getClass().getName());
    }

    @Test
    public void input_with_five_zipped_files_should_generate_five_entries() {
        Decompressor decompressor = Decompressor.of(Helper.generateInputStream(5, Suffix.ZIP), Suffix.ZIP);
        assertEquals(5, decompressor.read().entrySet().size());
    }

    @Test
    public void input_with_multiple_zipped_files_should_generate_expected_content() {
        List<String> contentList = Decompressor.of(Helper.generateInputStream(3, Suffix.ZIP), Suffix.ZIP)
                .read().values().stream().map(String::new).collect(Collectors.toList());
        Assert.assertTrue("First entry should start with \"Lorem ipsum dolor\"", contentList.get(0).startsWith("Lorem ipsum dolor"));
        Assert.assertTrue("Second entry should start with \"Pharetra vel turpis\"", contentList.get(1).startsWith("Pharetra vel turpis"));
        Assert.assertTrue("Third entry should start with \"Convallis aenean et\"", contentList.get(2).startsWith("Convallis aenean et"));
    }

    @Test
    public void input_with_multiple_zipped_files_should_generate_expected_file_names() {
        List<String> entryList = Decompressor.of(Helper.generateInputStream(3, Suffix.ZIP), Suffix.ZIP)
                .read().keySet().stream().collect(Collectors.toList());
        Assert.assertTrue("First entry should be \"entries/entry-1.txt\"", entryList.get(0).equals("entries/entry-1.txt"));
        Assert.assertTrue("Second entry should be \"entries/entry-2.txt\"", entryList.get(1).equals("entries/entry-2.txt"));
        Assert.assertTrue("Third entry should be \"entries/entry-3.txt\"", entryList.get(2).equals("entries/entry-3.txt"));
    }

    private static ByteArrayInputStream getEmptyTextInputStream() {
        return new ByteArrayInputStream("".getBytes());
    }

}
