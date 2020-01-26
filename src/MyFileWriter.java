import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyFileWriter {
    private BufferedWriter mFile;

    public MyFileWriter(String aFilePath, String aFileName) {

        try {
            mFile = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(aFilePath + aFileName), StandardCharsets.UTF_8));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public MyFileWriter(String aFileName) {

        try {
            mFile = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("E:/projects/Java/Ontology/res/" + aFileName), StandardCharsets.UTF_8));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void write(String result) {
        try {
            mFile.write(result);
            //System.out.println(result);
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