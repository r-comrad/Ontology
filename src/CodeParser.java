
import java.util.ArrayList;
import java.util.Objects;

public class CodeParser {
    private MyFileReader mReader;
    private MyFileWriter mWriter;

    public CodeParser() {
        ArrayList<String> fileNames = new ArrayList();
        fileNames.add("code_simple.cpp");                //0
        fileNames.add("code_cycle.cpp");                //1
        fileNames.add("code_kawai_A.cpp");                //2
        fileNames.add("code_bfs1.cpp");                    //3
        fileNames.add("code_furry_sudiaPasha.cpp");        //4
        fileNames.add("code_types.cpp");                //5
        fileNames.add("variableCheck.cpp");                //6
        fileNames.add("methodCheck.cpp");                //7

        mReader = new MyFileReader("code_samples/" + fileNames.get(3));
        mWriter = new MyFileWriter("parsed_code.cpp");
    }

    public void process() {
        //boolean conditionActive = false;
        int conditionCount = 0;
        int conditionBracketCount = 0;
        boolean afterCondition = false;
        boolean needColseBlock = false;
        boolean waitingForIf = false;

        String s;
        while ((s = mReader.readLine()) != null) {
            if ((s.contains("using") || s.contains("freopen") || s.contains("include") ||
                    s.contains("cin") || s.contains("return") || s.contains("//"))) {
                continue;
            }

            if (s.contains("if")) {
                mWriter.write("\n___IF__BEG {\n");
                conditionCount++;
            }

            if (s.contains("if") || s.contains("else")) {
                if (s.contains(";")) {
                    if (!s.contains("{")) {
                        s = s.replace(")", "){");
                        s = s.replace(";", ";}");
                    } else {
                        conditionBracketCount++;
                    }
                } else {
                    afterCondition = !s.contains("{");
                    if (s.contains("{"))conditionBracketCount++;
                }
            } else {
                if (waitingForIf) {
                    waitingForIf = false;
                    mWriter.write("\n___IF__END }\n");
                    conditionCount--;
                }
            }

            if (conditionBracketCount > 0 && s.contains("}")) {
                --conditionBracketCount;
                waitingForIf = true;
            }

            if (!(s.contains("if") || s.contains("else")) && conditionCount > 0 && afterCondition) {
                if (!s.contains("{")) {
                    mWriter.write("{");
                    needColseBlock = true;
                }
                afterCondition = false;
                waitingForIf = true;    //TODO if in many strings
            }

            for (int i = 0; i < s.length(); ++i) {
                char c = s.toCharArray()[i];

                if (Objects.equals(c, '{')) mWriter.write(" { ");
                else if (Objects.equals(c, '}')) mWriter.write(" } ");

                else if (Objects.equals(c, '(')) mWriter.write(" ( ");
                else if (Objects.equals(c, ')')) mWriter.write(" ) ");

                else if (Objects.equals(c, ';')) mWriter.write(" ; ");
                else if (Objects.equals(c, ',')) mWriter.write(" , ");
                else if (Objects.equals(c, '.')) mWriter.write(" . ");

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

            if (s.contains(";") && needColseBlock) {
                mWriter.write("}");
                needColseBlock = false;
                //waitingForIf = true;   //TODO if in many strings
            }

            mWriter.newLine();
        }

        mWriter.close();
    }
}