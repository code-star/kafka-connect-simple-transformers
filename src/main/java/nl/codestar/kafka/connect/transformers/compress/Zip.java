package nl.codestar.kafka.connect.transformers.compress;

import nl.codestar.kafka.connect.transformers.BaseTransformation;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class Zip<R extends ConnectRecord<R>> extends BaseTransformation<R> {

    private static final Logger log = LoggerFactory.getLogger(Zip.class);

    ZipConfig config;

    protected Zip(boolean isKey) {
        super(isKey);
    }

    @Override
    public ConfigDef config() {
        return ZipConfig.config();
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> settings) {
        this.config = new ZipConfig(settings);
    }

    @Override
    protected SchemaAndValue processBytes(R record, Schema inputSchema, byte[] input) {
        final Schema outputSchema = inputSchema.isOptional() ? Schema.OPTIONAL_BYTES_SCHEMA : Schema.BYTES_SCHEMA;
        try {
            final byte[] output = Compressor.compress(input);
            return new SchemaAndValue(outputSchema, output);
        } catch (IOException e) {
            log.warn("Could not compress input", e);
            return new SchemaAndValue(outputSchema, input);
        }
    }

    @Override
    protected SchemaAndValue processString(R record, Schema inputSchema, String input) {
        return processBytes(record, inputSchema, input.getBytes());
    }

    public static class Value<R extends ConnectRecord<R>> extends Zip<R> {
        public Value() {
            super(false);
        }

        @Override
        public R apply(R r) {
            final SchemaAndValue transformed = process(r, new SchemaAndValue(r.valueSchema(), r.value()));

            return r.newRecord(
                    r.topic(),
                    r.kafkaPartition(),
                    r.keySchema(),
                    r.key(),
                    transformed.schema(),
                    transformed.value(),
                    r.timestamp()
            );
        }
    }
}
