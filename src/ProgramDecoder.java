import java.util.*;


enum BlockType {
    NUN, FUNKTION, VARIABLE, CONDITION;
}

public class ProgramDecoder {
    private MyFileReader mFileReader;

    private int mConditionCounter;
    private int mConditionPartsCounter;
    private int mUsedConditions;

    private HashSet<String> mUsedFunctions;

    private String lastBlockLabel;
    private boolean mLastBlockIsCondition;

    ArrayList<Pair<String, String>> codeLevel;

    public ProgramDecoder() {
        mFileReader = new MyFileReader("code.cpp");
        mFileWriter = new MyFileWriter("rdf code");

        mAssignmentCounter = 0;
        mConditionCounter = 0;
        mConditionPartsCounter = 0;

        mUsedConditions = 0;

        lastBlockLabel = "________bl";
        mLastBlockIsCondition =false;

        mUsedFunctions = new HashSet<>();
        mUsedTypes = new HashSet<>();
        mUsedContainers = new HashSet<>();

        codeLevel = new ArrayList<>();
        codeLevel.add(new Pair("start", "implement"));
    }

    private void startPack() {
        mFileWriter.write(pack("function", "start", "implement"));
        mFileWriter.write(pack("function", "types", "take"));
        mFileWriter.write(pack("function", "types", "return"));
        mFileWriter.write(pack("user_functions", "function", "AKO"));



    }

    private void writeLever(String str) {
        //mFileWriter.write(pack(codeLevel.get(codeLevel.size() - 1).mX, str,
        //        codeLevel.get(codeLevel.size() - 1).mY));
                mFileWriter.write(pack(str, codeLevel.get(codeLevel.size() - 1).mX,
                codeLevel.get(codeLevel.size() - 1).mY));
    }


    private void inputStreamPack() {
        mFileWriter.write(pack("data_stream", "start", "implement"));
        mFileWriter.write(pack("cin", "data_stream", "ISA"));
        mFileWriter.write(pack("cout", "data_stream", "ISA"));
    }

    private void conditionPack() {
        mFileWriter.write(pack("conditions_types", "start", "implement"));
        if ((mUsedConditions & 1) != 0) mFileWriter.write(pack("if", "conditions_types", "AKO"));
        if ((mUsedConditions & 2) != 0) mFileWriter.write(pack("if-else", "conditions_types", "AKO"));
        if ((mUsedConditions & 4) != 0) mFileWriter.write(pack("if-else_tree", "conditions_types", "AKO"));
    }


    // TODO:
    private void userFunctionsPack() {
        mFileWriter.write(pack("function", "start", "implement"));
    }

    public void process() {
        List<String> list = new ArrayList();
        int type = 0;
        boolean variablelsUsed = false;
        boolean streamUsed = false;

        startPack();

        String str;
        while (!Objects.equals(str = mFileReader.read(), "")) {
            if (isEndSequence(str)) {
                //stackParser(list);
                if (type == 3 && list.size() > 0) functionDecoder(list);
                else if ((type & 1) != 0) {
                    variablelsUsed = true;
                    variableDeclarationDecoder(list);
                }

                if ((type & 4) != 0) variableAssignmentDecoder(list);
                //if ((type & 4) != 0) variableDeclarationDecoder(list);

                if ((type & 8) != 0) streamDecoder(list);

                if ((type & 16) != 0) conditionDecoder(list);

                if ((type & 32) != 0) containerDecoder(list);

                if (isLevelIncreaser(str)) {
                    codeLevel.add(new Pair(lastBlockLabel, "has_part"));
                }

                if (isLevelDecreaser(str)) {
                    if (!mLastBlockIsCondition)
                    {
                        conditionClouser();
                    }
                    else {
                        mLastBlockIsCondition = false;
                    }
                    codeLevel.remove(codeLevel.size() - 1);
                }

                list.clear();
                type = 0;
            } else {
                if (isTypeSequence(str) && list.size() == 0) {
                    mUsedTypes.add(str);
                    type |= 1;
                }
                if (isFunctionalSequence(str)) {
                    if ((type & 1) != 1) mUsedFunctions.add(list.get(list.size() - 1));
                    type |= 2;
                }
                if (isAssignmentSequence(str)) {
                    variablelsUsed = true;
                    type |= 4;
                }
                if (isStreamSequence(str)) {
                    type |= 8;
                    streamUsed = true;
                }
                if (isIfSequence(str) || isElseSequence(str)) {
                    type |= 16;
                }
                if (isContainerSequence(str)) {
                    type |= 32;
                }

                if (!isUnusedSequence(str)) list.add(str);
            }
        }

        if (mUsedTypes.size() > 0) typesDecoder();
        if (variablelsUsed) variablePack();
        if (streamUsed) inputStreamPack();

        while (mUsedFunctions.contains("if"))mUsedFunctions.remove("if");
        while (mUsedFunctions.contains("else"))mUsedFunctions.remove("else");

        if (mUsedFunctions.size() > 0) {
            stdFunctionPack();
        }
        if (mUsedContainers.size() > 0) {
            containersPack();
        }
        if (mUsedConditions != 0) conditionPack();

        mFileWriter.close();
    }





    public boolean isLevelIncreaser(String str) {
        return Objects.equals(str, "{");
    }

    public boolean isStreamSequence(String str) {
        return Objects.equals(str, "cin") || Objects.equals(str, "cout");
    }

    public boolean isLevelDecreaser(String str) {
        return Objects.equals(str, "}");
    }

    public boolean isEndSequence(String str) {
        return Objects.equals(str, ";") || Objects.equals(str, "{") || Objects.equals(str, "}");
    }






    public boolean isIfSequence(String s) {
        return Objects.equals(s, "if");
    }

    public boolean isElseSequence(String s) {
        return Objects.equals(s, "else");
    }

    public boolean isElseIfSequence(String s1, String s2) {
        return Objects.equals(s1, "else") && Objects.equals(s2, "if");
    }

    public boolean isUnusedSequence(String s) {
        return Objects.equals(s, ",") || Objects.equals(s, "(") || Objects.equals(s, ")") ||
                Objects.equals(s, "=") || Objects.equals(s, "+") || Objects.equals(s, ">") ||
                Objects.equals(s, "<") || Objects.equals(s, "&&") || Objects.equals(s, "||") ||
                Objects.equals(s, "!=") || Objects.equals(s, "==");
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



    public void conditionClouser() {
        if(mConditionPartsCounter == 0) return;

        String conditionName = "condition" + mConditionCounter;
        String parent = "________pr";
        if (mConditionPartsCounter == 1) {
            parent = "if";
            mUsedConditions |= 1;
        } else if (mConditionPartsCounter == 2) {
            parent = "if-else";
            mUsedConditions |= 2;
        } else if (mConditionPartsCounter > 2) {
            parent = "if-else_tree";
            mUsedConditions |= 4;
        }
        writeLever(conditionName);
        mFileWriter.write(pack(conditionName, parent, "ISA"));
    }

    public void conditionDecoder(List<String> aList) {
        // TODO: if else if (2 условия)

        if (isIfSequence(aList.get(0))) {
            conditionClouser();
            ++mConditionCounter;
            mConditionPartsCounter = 0;
        }
        String conditionName = "condition" + mConditionCounter;

        int offset = 0;
        String blockName = "________bn";
        if (isIfSequence(aList.get(0))) {
            blockName = "if" + "_" + mConditionCounter + "_" + mConditionPartsCounter++;
        } else if (aList.size() > 1 && isElseIfSequence(aList.get(0), aList.get(1))) {
            blockName = "else-if_block" +"_" +  mConditionCounter + "_" + mConditionPartsCounter++;
            ++offset;
        } else if (isElseSequence(aList.get(0))) {
            blockName = "else_block" + "_" + mConditionCounter + "_" + mConditionPartsCounter++;
        }
        lastBlockLabel = blockName;
        mLastBlockIsCondition = true;

        mFileWriter.write(pack(conditionName, blockName, "has_part"));

        for (int i = 1 + offset; i < aList.size(); ++i) {
            if (aList.get(i).codePointAt(0) >= 'A' && aList.get(i).codePointAt(0) <= 'Z' ||
                    aList.get(i).codePointAt(0) >= 'a' && aList.get(i).codePointAt(0) <= 'z')
                mFileWriter.write(pack(blockName, aList.get(i), "take"));
        }
    }

    public void streamDecoder(List<String> aList) {
        //writeLever(aList.get(0));
        //mFileWriter.write(pack(aList.get(0), "data_stream", "ISA"));
        for (int i = 2; i < aList.size(); i += 2) {
            String valueName = "value" + mAssignmentCounter++;
            writeLever(valueName);
            mFileWriter.write(pack(aList.get(i), valueName, "assignment"));
            mFileWriter.write(pack(aList.get(0), valueName, "read"));
        }
    }




}