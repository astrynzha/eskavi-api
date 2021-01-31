package eskavi.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaClassGeneratorTest {

    String _rawCode;
    String _formattedCode;

    @BeforeEach
    void before() {
        _formattedCode = """
                public class JavaClassGenerator {
                    public static File generateClassFile(String code) {
                        try {
                            PrintWriter out = new PrintWriter("aas.java");
                            out.println(format(code));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (FormatterException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }""";
        _rawCode = "public class JavaClassGenerator {public static File generateClassFile(String code) {try {PrintWriter out = new PrintWriter(\"aas.java\");out.println(format(code));} catch (FileNotFoundException e) {e.printStackTrace();} catch (FormatterException e) {e.printStackTrace();}return null;}";
    }

    @Test
    void generateClassFile() {

    }

    @Test
    void formatCode() throws FormatterException {
        String afterFormatting = new Formatter().formatSource(_rawCode);
        assertTrue(afterFormatting.equals(_formattedCode));
    }
}
