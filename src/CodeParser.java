import java.util.Objects;

public class CodeParser
{
    private MyFileReader mReader;
    private MyFileWriter mWriter;

    public CodeParser()
    {
        //mReader = new MyFileReader("code_kawai_A.cpp");
        mReader = new MyFileReader("code_bfs1.cpp");
        //mReader = new MyFileReader("code_cycle.cpp");
        //mReader = new MyFileReader("code_simple.cpp");
        mWriter = new MyFileWriter("parsed_code.cpp");
    }

    public void process()
    {
        //boolean needToBeDelete = false;
        //char c;
        //while (!Objects.equals(c = mReader.charRead(), '$'))
        String s;
        //while (!Objects.equals(s = mReader.readLine(), null))
        while ((s = mReader.readLine()) != null)
        {
            if (!(s.contains("using") || s.contains("freopen") || s.contains("include") ||
                    s.contains("cin") || s.contains("return")))
            {
                for (int i = 0; i < s.length(); ++i)
                {
                    char c = s.toCharArray()[i];

                    /*if (Objects.equals(c, '#')) needToBeDelete = true;
                    else if (needToBeDelete)
                    {
                        if ((Objects.equals(c, '>') || Objects.equals(c, ';')))
                        {
                            needToBeDelete = false;
                        }
                    }
                    else */if (Objects.equals(c, '{')) mWriter.write(" { ");
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
                mWriter.newLine();
            }
        }

        mWriter.close();
    }
}