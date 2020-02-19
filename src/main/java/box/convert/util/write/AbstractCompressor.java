package box.convert.util.write;

import java.io.File;
import java.util.Map;

/**
 *
 * @author hl
 */
abstract class AbstractCompressor implements Compressor {

    protected final Map<String, byte[]> content;
    protected final File result;

    public AbstractCompressor(Map<String, byte[]> content, File result) {
        this.content = content;
        this.result = result;
    }

    @Override
    public File toFile() {
        return process();
    }

    protected abstract File process();
}
