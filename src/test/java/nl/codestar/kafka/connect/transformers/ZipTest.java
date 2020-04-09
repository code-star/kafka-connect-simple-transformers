package nl.codestar.kafka.connect.transformers;

import com.google.common.collect.ImmutableMap;
import nl.codestar.kafka.connect.transformers.compress.Compressor;
import nl.codestar.kafka.connect.transformers.compress.Zip;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public abstract class ZipTest extends TransformationTest {
    protected ZipTest(boolean isKey) {
        super(isKey);
    }

    @Test
    public void zipping() {
        // given
        this.transformation.configure(
            ImmutableMap.<String, Object>of()
        );

        final String value =  "this is a test";
        byte[] compressed;
        try {
            compressed = Compressor.compress(value.getBytes());
        } catch (IOException e) {
            compressed = value.getBytes();
        }

        final SourceRecord inputRecord = new SourceRecord(
                ImmutableMap.of("partition", "1"),
                ImmutableMap.of("offset", "1"),
                "topic",
                null,
                null,
                null,
                Schema.STRING_SCHEMA,
                value,
                1L
        );

        // when
        SourceRecord outputRecord = this.transformation.apply(inputRecord);

        // then
        Assertions.assertArrayEquals(compressed, (byte[]) outputRecord.value());
    }

    public static class ValueTest<R extends ConnectRecord<R>> extends ZipTest {
        protected ValueTest() {
            super(false);
        }

        @Override
        protected Transformation<SourceRecord> create() {
            return new Zip.Value<>();
        }
    }

}