package ch.dissem.msgpack.types;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

class Utils {
    private static final char[] BASE64_CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

    /**
     * Returns a {@link ByteBuffer} containing the next <code>count</code> bytes from the {@link InputStream}.
     */
    static ByteBuffer bytes(InputStream in, int count) throws IOException {
        byte[] result = new byte[count];
        int off = 0;
        while (off < count) {
            int read = in.read(result, off, count - off);
            if (read < 0) {
                throw new IOException("Unexpected end of stream, wanted to read " + count + " bytes but only got " + off);
            }
            off += read;
        }
        return ByteBuffer.wrap(result);
    }

    /**
     * Helper method to decide which types support extra indention (for pretty printing JSON)
     */
    static String toJson(MPType<?> type, String indent) {
        if (type instanceof MPMap) {
            return ((MPMap) type).toJson(indent);
        }
        if (type instanceof MPArray) {
            return ((MPArray) type).toJson(indent);
        }
        return type.toJson();
    }

    /**
     * Slightly improved code from https://en.wikipedia.org/wiki/Base64
     */
    static String base64(byte[] data) {
        StringBuilder result = new StringBuilder((data.length * 4) / 3 + 3);
        int b;
        for (int i = 0; i < data.length; i += 3) {
            b = (data[i] & 0xFC) >> 2;
            result.append(BASE64_CODES[b]);
            b = (data[i] & 0x03) << 4;
            if (i + 1 < data.length) {
                b |= (data[i + 1] & 0xF0) >> 4;
                result.append(BASE64_CODES[b]);
                b = (data[i + 1] & 0x0F) << 2;
                if (i + 2 < data.length) {
                    b |= (data[i + 2] & 0xC0) >> 6;
                    result.append(BASE64_CODES[b]);
                    b = data[i + 2] & 0x3F;
                    result.append(BASE64_CODES[b]);
                } else {
                    result.append(BASE64_CODES[b]);
                    result.append('=');
                }
            } else {
                result.append(BASE64_CODES[b]);
                result.append("==");
            }
        }

        return result.toString();
    }
}
