package nl.codestar.kafka.connect.transformers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Timestamp;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTransformation<R extends ConnectRecord<R>> implements Transformation<R> {
    protected final boolean isKey;
    private static final Logger log = LoggerFactory.getLogger(BaseTransformation.class);
    private static final Schema OPTIONAL_TIMESTAMP = Timestamp.builder().optional().build();

    protected BaseTransformation(boolean isKey) {
        this.isKey = isKey;
    }

    protected SchemaAndValue processMap(R record, Map<String, Object> input) {
        throw new UnsupportedOperationException("MAP is not a supported type.");
    }

    protected SchemaAndValue processStruct(R record, Schema inputSchema, Struct input) {
        throw new UnsupportedOperationException("STRUCT is not a supported type.");
    }

    protected SchemaAndValue processString(R record, Schema inputSchema, String input) {
        throw new UnsupportedOperationException("STRING is not a supported type.");
    }

    protected SchemaAndValue processBytes(R record, Schema inputSchema, byte[] input) {
        throw new UnsupportedOperationException("BYTES is not a supported type.");
    }

    protected SchemaAndValue processInt8(R record, Schema inputSchema, byte input) {
        throw new UnsupportedOperationException("INT8 is not a supported type.");
    }

    protected SchemaAndValue processInt16(R record, Schema inputSchema, short input) {
        throw new UnsupportedOperationException("INT16 is not a supported type.");
    }

    protected SchemaAndValue processInt32(R record, Schema inputSchema, int input) {
        throw new UnsupportedOperationException("INT32 is not a supported type.");
    }

    protected SchemaAndValue processInt64(R record, Schema inputSchema, long input) {
        throw new UnsupportedOperationException("INT64 is not a supported type.");
    }

    protected SchemaAndValue processBoolean(R record, Schema inputSchema, boolean input) {
        throw new UnsupportedOperationException("BOOLEAN is not a supported type.");
    }

    protected SchemaAndValue processTimestamp(R record, Schema inputSchema, Date input) {
        throw new UnsupportedOperationException("Timestamp is not a supported type.");
    }

    protected SchemaAndValue processDate(R record, Schema inputSchema, Date input) {
        throw new UnsupportedOperationException("Date is not a supported type.");
    }

    protected SchemaAndValue processTime(R record, Schema inputSchema, Date input) {
        throw new UnsupportedOperationException("Time is not a supported type.");
    }

    protected SchemaAndValue processDecimal(R record, Schema inputSchema, BigDecimal input) {
        throw new UnsupportedOperationException("Decimal is not a supported type.");
    }

    protected SchemaAndValue processFloat64(R record, Schema inputSchema, double input) {
        throw new UnsupportedOperationException("FLOAT64 is not a supported type.");
    }

    protected SchemaAndValue processFloat32(R record, Schema inputSchema, float input) {
        throw new UnsupportedOperationException("FLOAT32 is not a supported type.");
    }

    protected SchemaAndValue processArray(R record, Schema inputSchema, List<Object> input) {
        throw new UnsupportedOperationException("ARRAY is not a supported type.");
    }

    protected SchemaAndValue processMap(R record, Schema inputSchema, Map<Object, Object> input) {
        throw new UnsupportedOperationException("MAP is not a supported type.");
    }

    protected SchemaAndValue process(R record, SchemaAndValue input) {
        if (null == input.schema() && null == input.value()) {
            return new SchemaAndValue((Schema)null, (Object)null);
        } else {
            SchemaAndValue result;
            if (input.value() instanceof Map) {
                log.trace("process() - Processing as map");
                result = this.processMap(record, (Map)input.value());
                return result;
            } else if (null == input.schema()) {
                log.trace("process() - Determining schema");
                Schema schema = SchemaHelper.schema(input.value());
                return this.process(record, new SchemaAndValue(schema, input.value()));
            } else {
                log.trace("process() - input.value() has as schema. schema = {}", input.schema());
                if (Type.STRUCT == input.schema().type()) {
                    result = this.processStruct(record, input.schema(), (Struct)input.value());
                } else if ("org.apache.kafka.connect.data.Timestamp".equals(input.schema().name())) {
                    result = this.processTimestamp(record, input.schema(), (Date)input.value());
                } else if ("org.apache.kafka.connect.data.Date".equals(input.schema().name())) {
                    result = this.processDate(record, input.schema(), (Date)input.value());
                } else if ("org.apache.kafka.connect.data.Time".equals(input.schema().name())) {
                    result = this.processTime(record, input.schema(), (Date)input.value());
                } else if ("org.apache.kafka.connect.data.Decimal".equals(input.schema().name())) {
                    result = this.processDecimal(record, input.schema(), (BigDecimal)input.value());
                } else if (Type.STRING == input.schema().type()) {
                    result = this.processString(record, input.schema(), (String)input.value());
                } else if (Type.BYTES == input.schema().type()) {
                    result = this.processBytes(record, input.schema(), (byte[])((byte[])input.value()));
                } else if (Type.INT8 == input.schema().type()) {
                    result = this.processInt8(record, input.schema(), (Byte)input.value());
                } else if (Type.INT16 == input.schema().type()) {
                    result = this.processInt16(record, input.schema(), (Short)input.value());
                } else if (Type.INT32 == input.schema().type()) {
                    result = this.processInt32(record, input.schema(), (Integer)input.value());
                } else if (Type.INT64 == input.schema().type()) {
                    result = this.processInt64(record, input.schema(), (Long)input.value());
                } else if (Type.FLOAT32 == input.schema().type()) {
                    result = this.processFloat32(record, input.schema(), (Float)input.value());
                } else if (Type.FLOAT64 == input.schema().type()) {
                    result = this.processFloat64(record, input.schema(), (Double)input.value());
                } else if (Type.ARRAY == input.schema().type()) {
                    result = this.processArray(record, input.schema(), (List)input.value());
                } else if (Type.MAP == input.schema().type()) {
                    result = this.processMap(record, input.schema(), (Map)input.value());
                } else {
                    if (Type.BOOLEAN != input.schema().type()) {
                        throw new UnsupportedOperationException(String.format("Schema is not supported. type='%s' name='%s'", input.schema().type(), input.schema().name()));
                    }

                    result = this.processBoolean(record, input.schema(), (Boolean)input.value());
                }

                return result;
            }
        }
    }

    public R apply(R record) {
        SchemaAndValue key = new SchemaAndValue(record.keySchema(), record.key());
        SchemaAndValue value = new SchemaAndValue(record.valueSchema(), record.value());
        SchemaAndValue input = this.isKey ? key : value;
        SchemaAndValue result = this.process(record, input);
        if (this.isKey) {
            key = result;
        } else {
            value = result;
        }

        return record.newRecord(record.topic(), record.kafkaPartition(), key.schema(), key.value(), value.schema(), value.value(), record.timestamp(), record.headers());
    }
}