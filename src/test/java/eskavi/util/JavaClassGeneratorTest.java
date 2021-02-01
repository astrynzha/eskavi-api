package eskavi.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaClassGeneratorTest {

    String _rawCode;
    String _formattedCode;

    @BeforeEach
    void before() {
        _formattedCode = "class HelloWorld {\n" +
                "  public static void main(String[] args) {\n" +
                "    System.out.println(\"Hello, World!\");\n" +
                "  }\n" +
                "}\n";
        _rawCode = "class HelloWorld {public static void main(String[] args){System.out.println(\"Hello, World!\");}}";
    }

    @Test
    void generateClassFile() {
        File generatedFile = JavaClassGenerator.generateClassFile(_rawCode);
        File expectedFile = new File("src/test/resources/HelloWorldTestClass.java");
        assertTrue(isFileEqual(generatedFile, expectedFile));
    }

    @Test
    void formatCode() throws FormatterException {
        String afterFormatting = new Formatter().formatSource(_rawCode);
        assertEquals(_formattedCode, afterFormatting);
    }

    private boolean isFileEqual(File f1, File f2) {
        try {
            byte[] first = Files.readAllBytes(f1.toPath());
            byte[] second = Files.readAllBytes(f2.toPath());
            return Arrays.equals(first, second);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
