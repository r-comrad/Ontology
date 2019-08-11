import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MyFileReader {
    private BufferedReader mFile;

    public MyFileReader(String aFileName) {
        try {
            mFile = new BufferedReader(new InputStreamReader(
                            new FileInputStream(aFileName), StandardCharsets.UTF_8));
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
}