package eskavi.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aasstring")
public class AasClassStrings {
    private String CLASS_START;
    private String CLASS_END;
    private String AAS_BUILDER_START;
    private String AAS_BUILDER_END;

    public String getAAS_BUILDER_START() {
        return AAS_BUILDER_START;
    }

    public void setAAS_BUILDER_START(String AAS_BUILDER_START) {
        this.AAS_BUILDER_START = AAS_BUILDER_START;
    }

    public String getAAS_BUILDER_END() {
        return AAS_BUILDER_END;
    }

    public void setAAS_BUILDER_END(String AAS_BUILDER_END) {
        this.AAS_BUILDER_END = AAS_BUILDER_END;
    }

    public String getCLASS_START() {
        return CLASS_START;
    }

    public void setCLASS_START(String CLASS_START) {
        this.CLASS_START = CLASS_START;
    }

    public String getCLASS_END() {
        return CLASS_END;
    }

    public void setCLASS_END(String CLASS_END) {
        this.CLASS_END = CLASS_END;
    }
}
