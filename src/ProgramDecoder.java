import java.util.*;


enum BlockType {
    NUN, FUNKTION, VARIABLE, CONDITION;
}

public class ProgramDecoder {
    private MyFileReader mFileReader;
    RDFWriter mRDFWriter;

    BlockType mType;

    FunctionDecoder mFunctionDecoder;
    VariableDecoder mVariableDecoder;


    private int mConditionCounter;
    private int mConditionPartsCounter;
    private int mUsedConditions;

    private String lastBlockLabel;
    private boolean mLastBlockIsCondition;

    ArrayList<Pair<String, String>> codeLevel;

    public ProgramDecoder() {
        mFileReader = new MyFileReader("code.cpp");
        mRDFWriter = new RDFWriter();

        mType = BlockType.NUN;

        mFunctionDecoder = new FunctionDecoder(mRDFWriter);
        mVariableDecoder = new VariableDecoder(mRDFWriter);





        mConditionCounter = 0;
        mConditionPartsCounter = 0;

        mUsedConditions = 0;

        lastBlockLabel = "________bl";
        mLastBlockIsCondition =false;

        codeLevel = new ArrayList<>();
        codeLevel.add(new Pair("start", "implement"));
    }

    private void startPack() {




    }

    private void writeLever(String str) {
        //mFileWriter.write(pack(codeLevel.get(codeLevel.size() - 1).mX, str,
        //        codeLevel.get(codeLevel.size() - 1).mY));
       //         mFileWriter.write(pack(str, codeLevel.get(codeLevel.size() - 1).mX,
                codeLevel.get(codeLevel.size() - 1).mY));
    }


    private void inputStreamPack() {
        //mFileWriter.write(pack("data_stream", "start", "implement"));
        //mFileWriter.write(pack("cin", "data_stream", "ISA"));
        //mFileWriter.write(pack("cout", "data_stream", "ISA"));
    }

    private void conditionPack() {
        //mFileWriter.write(pack("conditions_types", "start", "implement"));
        //if ((mUsedConditions & 1) != 0) mFileWriter.write(pack("if", "conditions_types", "AKO"));
        //if ((mUsedConditions & 2) != 0) mFileWriter.write(pack("if-else", "conditions_types", "AKO"));
        //if ((mUsedConditions & 4) != 0) mFileWriter.write(pack("if-else_tree", "conditions_types", "AKO"));
    }

    public void process() {
        List<String> list = new ArrayList();
        int type = 0;
        boolean streamUsed = false;

        String str;
        while (!Objects.equals(str = mFileReader.read(), "")) {
            if (isEndSequence(str)) {
                if (mType == BlockType.VARIABLE)
                {
                    mVariableDecoder.process(list);
                    writeLever(conditionName);
                }
                else if (mType == BlockType.FUNKTION) {
                    mVariableDecoder.infunctionDeclarationDecoder(list);
                    lastBlockLabel = mFunctionDecoder.process(list);
                }

                if (isLevelIncreaser(str)) {
                    codeLevel.add(new Pair(lastBlockLabel, "has_part"));
                }

                list.clear();
                mType = BlockType.NUN;
            } else {
                if (mVariableDecoder.isVariableSequence(str)) {
                    mType = BlockType.VARIABLE;
                }
                else if (mFunctionDecoder.isFunctionalSequence(str)) {
                    mType = BlockType.FUNKTION;
                }
                if (!isUnusedSequence(str)) list.add(str);
            }
        }

        mRDFWriter.close();
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
        //mFileWriter.write(pack(conditionName, parent, "ISA"));
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

       // mFileWriter.write(pack(conditionName, blockName, "has_part"));

        for (int i = 1 + offset; i < aList.size(); ++i) {
          //  if (aList.get(i).codePointAt(0) >= 'A' && aList.get(i).codePointAt(0) <= 'Z' ||
          //          aList.get(i).codePointAt(0) >= 'a' && aList.get(i).codePointAt(0) <= 'z')
         //       mFileWriter.write(pack(blockName, aList.get(i), "take"));
        }
    }

    public void streamDecoder(List<String> aList) {
        //writeLever(aList.get(0));
        //mFileWriter.write(pack(aList.get(0), "data_stream", "ISA"));
        for (int i = 2; i < aList.size(); i += 2) {
       //     String valueName = "value" + mAssignmentCounter++;
            writeLever(valueName);
        //    mFileWriter.write(pack(aList.get(i), valueName, "assignment"));
        //    mFileWriter.write(pack(aList.get(0), valueName, "read"));
        }
    }




}