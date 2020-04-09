package nl.codestar.kafka.connect.transformers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Timestamp;
import org.apache.kafka.connect.data.Schema.Type;

public class SchemaHelper {
    static final Map<Class<?>, Type> PRIMITIVES;

    public SchemaHelper() {
    }

    public static Schema schema(Object input) {
        return builder(input).build();
    }

    public static SchemaBuilder builder(Object input) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        SchemaBuilder builder;
        if (PRIMITIVES.containsKey(input.getClass())) {
            Type type = (Type)PRIMITIVES.get(input.getClass());
            builder = SchemaBuilder.type(type);
        } else if (input instanceof Date) {
            builder = Timestamp.builder();
        } else {
            if (!(input instanceof BigDecimal)) {
                throw new UnsupportedOperationException(String.format("Unsupported Type: %s", input.getClass()));
            }

            builder = Decimal.builder(((BigDecimal)input).scale());
        }

        return builder.optional();
    }

    static {
        Map<Class<?>, Type> primitives = new HashMap();
        primitives.put(String.class, Type.STRING);
        primitives.put(Boolean.class, Type.BOOLEAN);
        primitives.put(Byte.class, Type.INT8);
        primitives.put(Short.class, Type.INT16);
        primitives.put(Integer.class, Type.INT32);
        primitives.put(Long.class, Type.INT64);
        primitives.put(Float.class, Type.FLOAT32);
        primitives.put(Double.class, Type.FLOAT64);
        primitives.put(byte[].class, Type.BYTES);
        PRIMITIVES = ImmutableMap.copyOf(primitives);
    }
}
