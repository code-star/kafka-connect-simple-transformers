package nl.codestar.kafka.connect.transformers.compress;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class ZipConfig extends AbstractConfig {

    public ZipConfig(Map<?, ?> originals) {
        super(config(), originals);
    }

    public static ConfigDef config() {
        return new ConfigDef();
    }

}
