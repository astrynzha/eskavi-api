package eskavi.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
    }

    public static File generateJarFile(File classFile) {
        return null;
    }

    private static String format(String code) throws FormatterException {
        return new Formatter().formatSource(code);
    }
}
