package bl.compress.util.read;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author hl
 */
abstract class AbstractDecompressor implements Decompressor {

    protected final InputStream input;
    protected final Map<String, byte[]> result;

    public AbstractDecompressor(InputStream input) {
        this.input = input;
        result = new LinkedHashMap<>();
    }

    @Override
    public Map<String, byte[]> read() {
        if (result.isEmpty()) {
            populateResultMap();
        }
        return result;
    }

    abstract void populateResultMap();

}
