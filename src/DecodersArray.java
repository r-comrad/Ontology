import java.util.*;

public class DecodersArray {
    Map<CommandManager.Type, Decoder> mDecoders;
    CommandManager mCommandManager;

    RDFWriter mRDFWriter;

    List<String> mComandList = new ArrayList();
    ArrayList<Pair<String, String>> mCodeLevel;

    DecodersArray()
    {
        mCommandManager = new CommandManager();
        mCommandManager.reset();

        mRDFWriter = new RDFWriter();

        mDecoders = new HashMap();
        mDecoders.put(CommandManager.Type.VARIABLE,  new DecoderVariable(    this, mRDFWriter));
        mDecoders.put(CommandManager.Type.FUNCTION,  new DecoderFunction(    this, mRDFWriter));
        mDecoders.put(CommandManager.Type.CONDITION, new DecoderCondition(   this, mRDFWriter));
        mDecoders.put(CommandManager.Type.CYCLE,     new DecoderCycle(       this, mRDFWriter));
        mDecoders.put(CommandManager.Type.BEGIN,     new DecoderBracketBegin(this, mRDFWriter));
        mDecoders.put(CommandManager.Type.END,       new DecoderBracketEnd(  this, mRDFWriter));
        mDecoders.put(CommandManager.Type.END_LINE,  new DecoderEndLine(     this, mRDFWriter));
        mDecoders.put(CommandManager.Type.NUN,       new DecoderNun(         this, mRDFWriter));

        mCodeLevel = new ArrayList<>();
        mCodeLevel.add(new Pair("start", "include"));
    }

    public List<String> process(CommandManager.Type aType, List<String> aComands)
    {
        return mDecoders.get(aType).process(aComands, mCodeLevel.size());
    }

    private void checkDecoderConditions(String str)
    {
        for (Map.Entry<CommandManager.Type, Decoder> entry : mDecoders.entrySet())
        {
            if (entry.getValue().checkSequence(str)) mCommandManager.addCondition(entry.getValue().getType());
        }
    }

    public void finishProcessing()
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