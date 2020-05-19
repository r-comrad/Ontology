import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class MyFileReader {
    public static final String BASIC_TYPES_PATH      = "types/basic_types.txt";
    public static final String CONTAINERS_PATH       = "types/containers.txt";
    public static final String METHODS_PATH          = "methods/container_methods.txt";

    private BufferedReader mFile;

    public MyFileReader() {}

    public MyFileReader(String aFilePath, String aFileName) {
        try {
            mFile = new BufferedReader(new InputStreamReader(
                    new FileInputStream(aFilePath + aFileName), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public MyFileReader(String aFileName) {
        try {
            mFile = new BufferedReader(new InputStreamReader(
                    new FileInputStream("E:/projects/Java/Ontology/res/" + aFileName), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String read() {
        String result = "";
        while (Objects.equals(result, "")) {
            try {
                int c;
                while (!Character.isWhitespace((char) (c = mFile.read()))) { // TODO: \f \r
                    if (c == -1) return result;
                    result += (char) c;
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return result;
    }

    public char charRead() {
        try {
            int c = mFile.read();
            if (c == -1) return '$';
            return (char) c;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return '$';
    }

    public String readLine() {
        try {
            String s = mFile.readLine();
            return s;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return "$";
    }

    public HashSet<String> readAllWords() {
        HashSet<String> result = new HashSet<>();
        int count = Integer.parseInt(read());
        for (int i = 0; i < count; ++i) {
            result.add(read());
        }
        return result;
    }

    public HashSet<String> readAllWords(String aFileName) {
        MyFileReader file = new MyFileReader(aFileName);
        return file.readAllWords();
    }
}