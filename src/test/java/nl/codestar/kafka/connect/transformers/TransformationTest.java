package nl.codestar.kafka.connect.transformers;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.jupiter.api.BeforeEach;

public abstract class TransformationTest {
    final boolean isKey;
    final static String TOPIC = "test";

    protected TransformationTest(boolean isKey) {
        this.isKey = isKey;
    }

    protected abstract Transformation<SourceRecord> create();

    Transformation<SourceRecord> transformation;

    @BeforeEach
    public void before() {
        this.transformation = create();
    }


}