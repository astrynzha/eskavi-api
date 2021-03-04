package eskavi.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

public class JavaClassConstants {
    private static String CLASS_START = "class App{ public static void main(String[] args) throws IOException {";
    private static String CLASS_END = "AasServiceManager.Instance.setAasService(service);service.start();}}";
    private static String AAS_BUILDER_START = "AasService service = AasService.builder()";
    private static String AAS_BUILDER_END = ".build();";
    private static String REGISTER_START = "service.register(new URL(\"";
    private static String REGISTER_END = "\"), new JsonRegistrationWriter());";

    public static String getRegisterStart() {
        return REGISTER_START;
    }

    public static String getRegisterEnd() {
        return REGISTER_END;
    }

    public static String getClassStart() {
        return CLASS_START;
    }

    public static String getClassEnd() {
        return CLASS_END;
    }

    public static String getAasBuilderStart() {
        return AAS_BUILDER_START;
    }

    public static String getAasBuilderEnd() {
        return AAS_BUILDER_END;
    }
}
