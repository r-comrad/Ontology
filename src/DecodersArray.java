import java.util.*;

public class DecodersArray {
    Map<CommandManager.Type, Decoder> mDecoders;
    CommandManager mCommandManager;

    RDFWriter mRDFWriter;

    List<String> mComandList = new ArrayList();
    ArrayList<Pair<String, String>> mCodeLevel;

    DecodersArray(RDFWriter aRDFWriter)
    {
        mCommandManager = new CommandManager();
        mCommandManager.reset();

        mRDFWriter = new RDFWriter();

        mDecoders = new HashMap();
        mDecoders.put(CommandManager.Type.VARIABLE, DecoderVariable(aRDFWriterm this);
        mDecoders.put(CommandManager.Type.FUNCTION, new DecoderFunction(aRDFWriter, temp));
        mDecoders.put(CommandManager.Type.CONDITION, new DecoderCondition(aRDFWriter));
        mDecoders.put(CommandManager.Type.CYCLE, new DecoderCycle(aRDFWriter, temp));
        mDecoders.put(CommandManager.Type.BEGIN, new DecoderBracketBegin());
        mDecoders.put(CommandManager.Type.END, new DecoderBracketEnd());
        mDecoders.put(CommandManager.Type.END_LINE, new DecoderEndLine());
        mDecoders.put(CommandManager.Type.NUN, new DecoderNun());

        mCodeLevel = new ArrayList<>();
        mCodeLevel.add(new Pair("start", "include"));
    }

    private void checkDecoderConditions(String str)
    {
        for (Map.Entry<CommandManager.Type, Decoder> entry : mDecoders.entrySet())
        {
            if (entry.getValue().checkSequence(str)) mCommandManager.addCondition(entry.getValue().getType());
        }
    }

    private void finishProcessing()
    {
        for (Map.Entry<CommandManager.Type, Decoder> entry : mDecoders.entrySet())
        {
            entry.getValue().writePack();
        }

        mRDFWriter.close();
    }

    public void process(String str) {
        checkDecoderConditions(str);

        if (mCommandManager.isEndSequence()) {
            //if (mCodeLevel.size() > 0) mCommandManager.setInsideFunction();
            List<String> connections = mDecoders.get(mCommandManager.getType()).process(mComandList, mCodeLevel.size());

            for (String i : connections) {
                mRDFWriter.writeLever(i, mCodeLevel.get(mCodeLevel.size() - 1));
            }

            if (mCommandManager.isLevelDecreaser()) {
                mCodeLevel.remove(mCodeLevel.size() - 1);
            } else if (mCommandManager.isLevelIncreaser()) {
                mCodeLevel.add(new Pair(connections.get(0), "include"));
            }

            mComandList.clear();
            mCommandManager.reset();
            mRDFWriter.newLine();
        } else {
            mComandList.add(str);
        }
    }
}