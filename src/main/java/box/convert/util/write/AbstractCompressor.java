package box.convert.util.write;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

/**
 *
 * @author hl
 */
abstract class AbstractCompressor implements Compressor {

    protected final Map<String, byte[]> content;
    protected final Path resultPath;

    public AbstractCompressor(Map<String, byte[]> content, Path path) {
        this.content = content;
        this.resultPath = path;
    }

    @Override
    public File write() {
        return process();
    }

    protected abstract File process();
}
