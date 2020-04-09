package nl.codestar.kafka.connect.transformers;

import com.google.common.collect.ImmutableMap;
import nl.codestar.kafka.connect.transformers.compress.Compressor;
import nl.codestar.kafka.connect.transformers.compress.Unzip;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public abstract class UnzipTest extends TransformationTest {
    protected UnzipTest(boolean isKey) {
        super(isKey);
    }

    @Test
    public void unzipping() {
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
                Schema.BYTES_SCHEMA,
                compressed,
                1L
        );

        // when
        SourceRecord outputRecord = this.transformation.apply(inputRecord);

        // then
        Assertions.assertEquals(value, outputRecord.value());
    }

    public static class ValueTest<R extends ConnectRecord<R>> extends UnzipTest {
        protected ValueTest() {
            super(false);
        }

        @Override
        protected Transformation<SourceRecord> create() {
            return new Unzip.Value<>();
        }
    }

}