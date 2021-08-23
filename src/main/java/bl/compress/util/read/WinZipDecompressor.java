package bl.compress.util.read;

import bl.compress.util.CompressUtilException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author hl
 */
class WinZipDecompressor extends AbstractDecompressor {

    private WinZipDecompressor(InputStream input) {
        super(input);
    }

    public static WinZipDecompressor from(InputStream input) {
        return new WinZipDecompressor(input);
    }

    @Override
    protected void populateResultMap() {
        try (ZipInputStream zipInputStream = new ZipInputStream(new WinZipInputStream(input))) {
            byte[] buffer = new byte[1024];
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    int length;
                    while ((length = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    result.put(zipEntry.getName(), outputStream.toByteArray());
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException ex) {
            throw new CompressUtilException("Could not read source", ex);
        }
    }
}
