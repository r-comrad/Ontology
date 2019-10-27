import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyFileWriter {
    private BufferedWriter mFile;

    public MyFileWriter(String aFileName) {

        try {
            mFile = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(aFileName), StandardCharsets.UTF_8));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void write(String result) {
        try {
            mFile.write(result);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void close() {
        try {
            mFile.close();
        } catch (Exception ex) {
            /*ignore?*/
            System.out.println(ex.getMessage());
        }
    }
}