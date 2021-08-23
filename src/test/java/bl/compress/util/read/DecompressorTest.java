package bl.compress.util.read;

import bl.compress.util.Helper;
import bl.compress.util.Suffix;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hl
 */
public class DecompressorTest {

    public DecompressorTest() {
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
        assertTrue("First entry should start with \"Lorem ipsum dolor\"", contentList.get(0).startsWith("Lorem ipsum dolor"));
        assertTrue("Second entry should start with \"Pharetra vel turpis\"", contentList.get(1).startsWith("Pharetra vel turpis"));
        assertTrue("Third entry should start with \"Convallis aenean et\"", contentList.get(2).startsWith("Convallis aenean et"));
    }

    @Test
    public void input_with_multiple_zipped_files_should_generate_expected_file_names() {
        List<String> entryList = Decompressor.of(Helper.generateInputStream(3, Suffix.ZIP), Suffix.ZIP)
                .read().keySet().stream().collect(Collectors.toList());
        assertTrue("First entry should be \"entries/entry-1.txt\"", entryList.get(0).equals("entries/entry-1.txt"));
        assertTrue("Second entry should be \"entries/entry-2.txt\"", entryList.get(1).equals("entries/entry-2.txt"));
        assertTrue("Third entry should be \"entries/entry-3.txt\"", entryList.get(2).equals("entries/entry-3.txt"));
    }
    
    @Test
    public void input_with_two_zipped_files_() {
        InputStream input = getClass().getResourceAsStream("/two-files.zip");
        Suffix suffix = Suffix.ZIP;
        Map<String, byte[]> result = Decompressor.of(input, suffix).read();
        List<String> entryList = result.keySet().stream().collect(Collectors.toList());
        String textFileName = "plain-text-file.txt";
        long textFileSize = 18011l;
        String imageFileName = "sample-image.jpg";
        long imageFileSize = 85276l;
        assertTrue("Entry list should contain filename " + textFileName, entryList.contains(textFileName));
        assertTrue("Entry list should contain filename " + imageFileName, entryList.contains(imageFileName));
        assertTrue("text-file should have content", result.get(textFileName).length > 0);
        assertEquals("text-file should be " + textFileSize + " bytes", textFileSize, result.get(textFileName).length);
        assertTrue("image-file should have content", result.get(imageFileName).length > 0);
        assertEquals("image-file should be " + imageFileSize + " bytes", imageFileSize, result.get(imageFileName).length);
    }

    @Test
    public void input_with_two_tar_gz_files_() {
        InputStream input = getClass().getResourceAsStream("/two-files.tar.gz");
        Suffix suffix = Suffix.TAR_GZ;
        Map<String, byte[]> result = Decompressor.of(input, suffix).read();
        List<String> entryList = result.keySet().stream().collect(Collectors.toList());
        String textFileName = "plain-text-file.txt";
        long textFileSize = 18011l;
        String imageFileName = "sample-image.jpg";
        long imageFileSize = 85276l;
        assertTrue("Entry list should contain filename " + textFileName, entryList.contains(textFileName));
        assertTrue("Entry list should contain filename " + imageFileName, entryList.contains(imageFileName));
        assertTrue("text-file should have content", result.get(textFileName).length > 0);
        assertEquals("text-file should be " + textFileSize + " bytes", textFileSize, result.get(textFileName).length);
        assertTrue("image-file should have content", result.get(imageFileName).length > 0);
        assertEquals("image-file should be " + imageFileSize + " bytes", imageFileSize, result.get(imageFileName).length);
    }

    @Test
    public void input_with_two_7z_files_() {
        InputStream input = getClass().getResourceAsStream("/two-files.7z");
        Suffix suffix = Suffix.SEVEN_Z;
        Map<String, byte[]> result = Decompressor.of(input, suffix).read();
        List<String> entryList = result.keySet().stream().collect(Collectors.toList());
        String textFileName = "plain-text-file.txt";
        long textFileSize = 18011l;
        String imageFileName = "sample-image.jpg";
        long imageFileSize = 85276l;
        assertTrue("Entry list should contain filename " + textFileName, entryList.contains(textFileName));
        assertTrue("Entry list should contain filename " + imageFileName, entryList.contains(imageFileName));
        assertTrue("text-file should have content", result.get(textFileName).length > 0);
        assertEquals("text-file should be " + textFileSize + " bytes", textFileSize, result.get(textFileName).length);
        assertTrue("image-file should have content", result.get(imageFileName).length > 0);
        assertEquals("image-file should be " + imageFileSize + " bytes", imageFileSize, result.get(imageFileName).length);
    }

    private static ByteArrayInputStream getEmptyTextInputStream() {
        return new ByteArrayInputStream("".getBytes());
    }

}
