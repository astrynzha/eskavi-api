package eskavi.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaClassGenerator {
    public static File generateClassFile(String code) {
        try {
            File file = File.createTempFile("ass", ".java");
            Files.writeString(Path.of(file.getAbsolutePath()), format(code));
            file.deleteOnExit();
            return file;
        } catch (IOException | FormatterException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static String format(String code) throws FormatterException {
        return new Formatter().formatSource(code);
    }
}
