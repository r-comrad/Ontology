import java.util.*;

public class ProgramDecoder
{
    private MyFileReader mFileReader;
    RDFWriter mRDFWriter;

    CommandManager mCommandManager;

    Map<CommandManager.Type, Decoder> mDecoders;

    ArrayList<Pair<String, String>> mCodeLevel;

    int mLevel;

    public ProgramDecoder()
    {
        mFileReader = new MyFileReader("parsed_code.cpp");
        mRDFWriter = new RDFWriter();

        mCommandManager = new CommandManager();
        mCommandManager.reset();

        mDecoders = new HashMap();
        DecoderVariable temp = new DecoderVariable(mRDFWriter);
        mDecoders.put(CommandManager.Type.VARIABLE, temp);
        mDecoders.put(CommandManager.Type.FUNCTION, new DecoderFunction(mRDFWriter, temp));
        mDecoders.put(CommandManager.Type.CONDITION, new DecoderCondition(mRDFWriter));
        mDecoders.put(CommandManager.Type.CYCLE, new DecoderCycle(mRDFWriter, temp));
        mDecoders.put(CommandManager.Type.BRACKET, new DecoderCycle(mRDFWriter, temp));
        mDecoders.put(CommandManager.Type.END_LINE, new DecoderCycle(mRDFWriter, temp));
        mDecoders.put(CommandManager.Type.NUN, new DecoderCycle(mRDFWriter, temp));

        mCodeLevel = new ArrayList<>();

        mLevel= 0;
    }

    public void process()
    {
        List<String> list = new ArrayList();
        String str;

        while (!Objects.equals(str = mFileReader.read(), ""))
        {
            for (Map.Entry<CommandManager.Type, Decoder> entry : mDecoders.entrySet())
            {
                if (entry.getValue().checkSequence(str))
                    mCommandManager.addCondition(entry.getValue().getType());
            }

            if (isEndSequence(str))
            {
                List<String> connections = mDecoders.get(mCommandManager.getType()).process(list, mLevel);

                if (mCodeLevel.size() > 0 && connections.size() > 0)
                {
                    mRDFWriter.writeLever(connections.get(0), mCodeLevel.get(mCodeLevel.size() - 1));
                }

                if (isLevelDecreaser(str))
                {
                    --mLevel;
                    mCodeLevel.remove(mCodeLevel.size() - 1);
                }
                else if (isLevelIncreaser(str))
                {
                    ++mLevel;
                    for (String i : connections)
                    {
                        // TODO: need has_part?
                        // если все - часть, то вынести в метод write
                        mCodeLevel.add(new Pair(i, "include"));
                    }
                }

                list.clear();
                mCommandManager.reset();
            }
            else
            {
                list.add(str);
            }

        }

        for (Map.Entry<CommandManager.Type, Decoder> entry : mDecoders.entrySet())
        {
            entry.getValue().writePack();
        }

        mRDFWriter.close();
    }

    // TODO: int Bracket Decoder
    public boolean isLevelIncreaser(String str)
    {
        return Objects.equals(str, "{");
    }

    public boolean isStreamSequence(String str)
    {
        return Objects.equals(str, "cin") || Objects.equals(str, "cout");
    }

    // TODO: int Bracket Decoder
    public boolean isLevelDecreaser(String str)
    {
        return Objects.equals(str, "}");
    }

    public boolean isEndSequence(String str)
    {
        return mDecoders.get(CommandManager.Type.END_LINE).checkSequence(str) ||
                mDecoders.get(CommandManager.Type.BRACKET).checkSequence(str);
    }

    public boolean isUnusedSequence(String s)
    {
        return Objects.equals(s, ",") || Objects.equals(s, "(") || Objects.equals(s, ")") ||
                /*Objects.equals(s, "=") ||*/ Objects.equals(s, "+") || Objects.equals(s, ">") || Objects.equals(s, "<") || Objects.equals(s, "&&") || Objects.equals(s, "||") || Objects.equals(s, "!=") || Objects.equals(s, "==");
    }
}