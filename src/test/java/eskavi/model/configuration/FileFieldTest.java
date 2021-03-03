package eskavi.model.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.fields.FieldEnumSelector;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FileFieldTest {
    private FileField testObject;
    @BeforeEach
    void setUp() {
        testObject = new FileField("test", false, new KeyExpression("start", "end"));
        testObject.setValue("value");
    }

    @Test
    void testResolveKeyExpression() {
        assertEquals("start./valueend", testObject.resolveKeyExpression());
    }
}
