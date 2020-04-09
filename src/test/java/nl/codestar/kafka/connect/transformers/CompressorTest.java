package nl.codestar.kafka.connect.transformers;

import nl.codestar.kafka.connect.transformers.compress.Compressor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CompressorTest {

    @Test
    public void zipUnzip() {
        final String actual =  "this is a test";

        byte[] compressed;
        try {
            compressed = Compressor.compress(actual.getBytes());
            byte[] decompressed = Compressor.decompress(compressed);
            Assertions.assertArrayEquals(actual.getBytes(), decompressed);
        } catch (IOException ignored) {
        }

    }

}
