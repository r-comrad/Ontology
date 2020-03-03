
import java.util.ArrayList;
import java.util.Objects;

public class CodeParser
{
    private MyFileReader mReader;
    private MyFileWriter mWriter;

    public CodeParser()
    {
        ArrayList<String> fileNames = new ArrayList();
        fileNames.add("code_simple.cpp");	            //0
        fileNames.add("code_cycle.cpp");	            //1
        fileNames.add("code_kawai_A.cpp");	            //2
        fileNames.add("code_bfs1.cpp");		            //3
        fileNames.add("code_furry_sudiaPasha.cpp");		//4
        fileNames.add("code_types.cpp");		        //5


        mReader = new MyFileReader(fileNames.get(5));
        mWriter = new MyFileWriter("parsed_code.cpp");
    }

    public void process()
    {
        String s;
        while ((s = mReader.readLine()) != null)
        {
            if (!(s.contains("using") || s.contains("freopen") || s.contains("include") ||
                    s.contains("cin") || s.contains("return")))
            {
                for (int i = 0; i < s.length(); ++i)
                {
                    char c = s.toCharArray()[i];

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
                mWriter.newLine();
            }
        }

        mWriter.close();
    }
}