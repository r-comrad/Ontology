import java.util.Objects;

public class CodeParser {
    private MyFileReader mReader;
    private MyFileWriter mWriter;

    public CodeParser() {
        mReader = new MyFileReader("code.cpp");
        mWriter = new MyFileWriter("parsed_code.cpp");
    }

    public void process() {
        char c;
        while (!Objects.equals(c = mReader.charRead(), '$')) {
            if (Objects.equals(c, '{')) mWriter.write(" { ");
            else if (Objects.equals(c, '}')) mWriter.write(" } ");

            else if (Objects.equals(c, '(')) mWriter.write(" ( ");
            else if (Objects.equals(c, ')')) mWriter.write(" ) ");

            else if (Objects.equals(c, ';')) mWriter.write(" ; ");
            else if (Objects.equals(c, ',')) mWriter.write(" , ");

            else if (Objects.equals(c, '=')) mWriter.write(" = ");

            else if (Objects.equals(c, '-')) mWriter.write(" - ");
            else if (Objects.equals(c, '+')) mWriter.write(" + ");
            else if (Objects.equals(c, '*')) mWriter.write(" * ");
            else if (Objects.equals(c, '/')) mWriter.write(" / ");
            else if (Objects.equals(c, '%')) mWriter.write(" % ");

            else if (Objects.equals(c, '>')) mWriter.write(" > ");
            else if (Objects.equals(c, '<')) mWriter.write(" < ");

            else if (Objects.equals(c, '&')) mWriter.write(" & ");
            else if (Objects.equals(c, '|')) mWriter.write(" | ");

            else if (Objects.equals(c, '^')) mWriter.write(" ^ ");

            else mWriter.write("" + c);
        }

        mWriter.close();
    }
}