import java.util.*;

public class ProgramDecoder {
    private MyFileReader mFileReader;
    RDFWriter mRDFWriter;

    Decoder.Type mType;

    List<Decoder> mDecoders;

    ArrayList<Pair<String, String>> mCodeLevel;

    public ProgramDecoder() {
        mFileReader = new MyFileReader("parsed_code.cpp");
        mRDFWriter = new RDFWriter();

        mType = Decoder.Type.NUN;

        VariableDecoder temp = new VariableDecoder(mRDFWriter);
        mDecoders = new ArrayList();
        mDecoders.add(temp);
        mDecoders.add(new FunctionDecoder(mRDFWriter, temp));
        mDecoders.add(new ConditionDecoder(mRDFWriter));

        mCodeLevel = new ArrayList<>();
        //mCodeLevel.add(new Pair("start", "implement"));
    }

    public void process() {
        List<String> list = new ArrayList();
        String str;
        while (!Objects.equals(str = mFileReader.read(), "")) {
            if (isEndSequence(str)) {
                List<String> connections = new ArrayList<>();
                /*for(Decoder i : mDecoders)
                    if (i.checkSequence(str)) mType = i.getType();*/

                for(Decoder i : mDecoders)
                    if (mType == i.getType())
                    {
                        connections = i.process(list);
                    }
                /*if (mType == BlockType.VARIABLE)
                {
                    connections = mVariableDecoder.process(list);
                }
                else if (mType == BlockType.FUNKTION) {
                    mVariableDecoder.infunctionDeclarationDecoder(list);
                    connections = mFunctionDecoder.process(list);
                }
                else if(mType == BlockType.CONDITION)
                {
                    connections = mConditionDecoder.process(list);
                }*/

                /*if(mType == BlockType.CONDITION)
                {
                    String curStr = connections.get(0);
                    if (curStr.startsWith("condition"))
                    {
                        mRDFWriter.writeLever(curStr, mCodeLevel.get(mCodeLevel.size() - 1));
                        connections.remove(0);
                    }
                }
                else*/ {
                    for(String i : connections)
                    {
                        if (mCodeLevel.size() > 0) mRDFWriter.writeLever(i, mCodeLevel.get(mCodeLevel.size() - 1));
                    }
                }


                if (isLevelIncreaser(str)) {
                    if (connections.size() > 0)
                    {
                        String lastBlockLabel = connections.get(0);
                        mCodeLevel.add(new Pair(lastBlockLabel, "has_part"));
                    }
                    //mConditionDecoder.increaseLevel();
                }
                else if(isLevelDecreaser(str))
                {
                    //mConditionDecoder.decreaseLevel();
                    mCodeLevel.remove(mCodeLevel.size() - 1);
                }

                list.clear();
                mType = Decoder.Type.NUN;
            } else {
                //if ( mType == Decoder.Type.NUN)
                {
                    for(Decoder i : mDecoders)
                        if (i.checkSequence(str)) mType = i.getType();
                }

                /*if (mVariableDecoder.isVariableSequence(str)) {
                    mType = BlockType.VARIABLE;
                }
                else if (mFunctionDecoder.isFunctionalSequence(str)) {
                    if (mType != BlockType.CONDITION) mType = BlockType.FUNKTION;
                }
                else if(mConditionDecoder.isConditionSequence(str))
                {
                    mType = BlockType.CONDITION;
                }
                else*/ if(isLevelIncreaser(str))
                {
                    //mConditionDecoder.increaseLevel();
                }
                else if(isLevelDecreaser(str))
                {
                    //mConditionDecoder.decreaseLevel();
                }
                if (!isUnusedSequence(str)) list.add(str);
            }
        }

        for(Decoder i : mDecoders) i.writePack();

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
                /*Objects.equals(s, "=") ||*/ Objects.equals(s, "+") || Objects.equals(s, ">") ||
                Objects.equals(s, "<") || Objects.equals(s, "&&") || Objects.equals(s, "||") ||
                Objects.equals(s, "!=") || Objects.equals(s, "==");
    }

    public void streamDecoder(List<String> aList) {
        //writeLever(aList.get(0));
        //mFileWriter.write(pack(aList.get(0), "data_stream", "ISA"));
        for (int i = 2; i < aList.size(); i += 2) {
       //     String valueName = "value" + mAssignmentCounter++;
          //  writeLever(valueName);
        //    mFileWriter.write(pack(aList.get(i), valueName, "assignment"));
        //    mFileWriter.write(pack(aList.get(0), valueName, "read"));
        }
    }




}