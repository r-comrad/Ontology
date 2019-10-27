import java.util.*;

public class ProgramDecoder {
    private MyFileReader mFileReader;
    private MyFileWriter mFileWriter;

    public ProgramDecoder() {
        mFileReader = new MyFileReader("code.cpp");
        mFileWriter = new MyFileWriter("rdf code");
    }

    public void process() {
        List list = new ArrayList();
        int type = 0;

        String str;
        while (!Objects.equals(str = mFileReader.read(), "")) {
            if (isEndSequence(str)) {
                //stackParser(list);
                if (type == 3 && list.size() > 0) funktionDecoder(list);
                else if ((type & 1) != 0) variableDeclarationDecoder(list);
                //if ((type & 4) != 0) variableDeclarationDecoder(list);
                list.clear();
                type = 0;
            } else {
                if (isTypeSequence(str) && list.size() == 0) type |= 1;
                if (isFunctionalSequence(str)) type |= 2;
                if (isAssignmentSequence(str)) type |= 4;

                if (!isUnusedSequence(str)) list.add(str);
            }
        }

        mFileWriter.close();
    }

    public boolean isEndSequence(String str) {
        return Objects.equals(str, ";") || Objects.equals(str, "{") || Objects.equals(str, "}");
    }

    public boolean isTypeSequence(String s) {
        return Objects.equals(s, "int") || Objects.equals(s, "float") || Objects.equals(s, "double")
                || Objects.equals(s, "char") || Objects.equals(s, "bool")|| Objects.equals(s, "void");
    }

    public boolean isAssignmentSequence(String s) {
        return Objects.equals(s, "=");
    }

    public boolean isFunctionalSequence(String s) {
        return Objects.equals(s, "(") || Objects.equals(s, ")");
    }

    public boolean isUnusedSequence(String s) {
        return Objects.equals(s, ",") || Objects.equals(s, "(") || Objects.equals(s, ")");
    }

    public void stackParser(List<String> aList) {
        //for (List<String> entry : node.mConnections.entrySet()) {
        //    OntologyNode nextNode = entry.getValue().getSecond();
        //}
        //aList[0] = "hh";
    }

    private String pack(String s1, String s2, String s3) {
        return s1 + " " + s2 + " " + s3 + "\n";
    }

    public void funktionDecoder(List<String> aList) {
        mFileWriter.write(pack(aList.get(1), aList.get(0), "return"));
        mFileWriter.write(pack(aList.get(1), "function", "AKO"));
        for(int i = 2; i < aList.size(); i += 2)
        {
            mFileWriter.write(pack(aList.get(1), aList.get(i + 1), "take"));
            mFileWriter.write(pack(aList.get(i + 1), aList.get(i), "has_type"));
            mFileWriter.write(pack(aList.get(i + 1), "variable", "AKO"));
        }
    }

    public void variableDeclarationDecoder(List<String> aList) {
        for(int i = 1; i < aList.size(); ++i)
        {
            mFileWriter.write(pack(aList.get(i), aList.get(0), "has_type"));
            mFileWriter.write(pack(aList.get(i), "variable", "AKO"));
        }
    }
}