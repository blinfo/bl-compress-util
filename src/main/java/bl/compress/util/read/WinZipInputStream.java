package bl.compress.util.read;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * 
 * Från
 * http://stackoverflow.com/questions/7924895/how-can-i-read-from-a-winzip-self-extracting-exe-zip-file-in-java
 * 
 */
class WinZipInputStream extends FilterInputStream {

    public static final byte[] ZIP_LOCAL = {0x50, 0x4b, 0x03, 0x04};
    protected int ip;
    protected int op;

    public WinZipInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public int read() throws IOException {
        while (ip < ZIP_LOCAL.length) {
            int c = super.read();
            if (c == ZIP_LOCAL[ip]) {
                ip++;
            } else {
                ip = 0;
            }
        }

        if (op < ZIP_LOCAL.length) {
            return ZIP_LOCAL[op++];
        } else {
            return super.read();
        }
    }

    @Override
    public int read(byte[] byteArray, int offset, int length) throws IOException {
        if (op == ZIP_LOCAL.length) {
            return super.read(byteArray, offset, length);
        }
        int i = 0;
        while (i < Math.min(length, ZIP_LOCAL.length)) {
            byteArray[i++] = (byte) read();
        }
        return i;
    }
}
