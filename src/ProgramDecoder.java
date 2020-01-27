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
        mDecoders.add(new CycleDecoder(mRDFWriter, temp));

        mCodeLevel = new ArrayList<>();
    }

    public void process() {
        List<String> list = new ArrayList();
        String str;
        Decoder.Type prevType = Decoder.Type.NUN;
        while (!Objects.equals(str = mFileReader.read(), "")) {
            if (isEndSequence(str)) {
                List<String> connections = new ArrayList<>();

                for (Decoder i : mDecoders)
                    if (mType == i.getType()) {
                        connections = i.process(list);
                        //i.close();
                    }
                {
                    if (mCodeLevel.size() > 0 && connections.size() > 0) {
                        mRDFWriter.writeLever(connections.get(0), mCodeLevel.get(mCodeLevel.size() - 1));
                    }

                    for (String i : connections) {
                        if (isLevelIncreaser(str)) {
                            // TODO: need has_part?
                            // если все - часть, то вынести в метод write
                            mCodeLevel.add(new Pair(i, "include"));
                        }
                    }
                }
                if (isLevelDecreaser(str)) {
                    mCodeLevel.remove(mCodeLevel.size() - 1);
                }

                if (prevType == Decoder.Type.NUN && mType != Decoder.Type.CONDITION) {
                    for (Decoder i : mDecoders)
                        if (Decoder.Type.CONDITION == i.getType()) {
                            i.close();
                        }
                }
                prevType = mType;

                list.clear();
                mType = Decoder.Type.NUN;
            } else {
                {
                    if (mType == Decoder.Type.NUN || mType == Decoder.Type.VARIABLE)
                        for (Decoder i : mDecoders)
                            if (i.checkSequence(str)) mType = i.getType();
                }

                if (!isUnusedSequence(str)) list.add(str);
            }
        }

        for (Decoder i : mDecoders) i.writePack();

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

    public boolean isUnusedSequence(String s) {
        return Objects.equals(s, ",") || Objects.equals(s, "(") || Objects.equals(s, ")") ||
                /*Objects.equals(s, "=") ||*/ Objects.equals(s, "+") || Objects.equals(s, ">") ||
                Objects.equals(s, "<") || Objects.equals(s, "&&") || Objects.equals(s, "||") ||
                Objects.equals(s, "!=") || Objects.equals(s, "==");
    }
}