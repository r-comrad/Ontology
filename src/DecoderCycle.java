import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DecoderCycle extends Decoder {
    DecoderVariable mDecoderVariable;
    private int mCycleCounter;

    private HashSet<String> mUsedCycles;

    public DecoderCycle(DecodersArray aDecodersArray, RDFWriter aRDFWriter) {
        super(aDecodersArray, aRDFWriter);
        mCycleCounter = 0;

        mUsedCycles = new HashSet<>();
    }

    @Override
    public List<String> process(List<String> aList, int aLevel) {
        List<String> result = new ArrayList<>();
        //TODO: for without breakets {}
        //TODO: conditions in cycle{}
        if (isForSequence(aList.get(0)))
        {
            String cycleName = "for" + "_" + ++mCycleCounter;

            List <String> temp = aList.subList(2, aList.indexOf(";"));
            //temp = temp.subList(0, temp.indexOf("="));
            //mDecoderVariable.inCycleDeclarationDecoder(temp);
            temp = super.mDecodersArray.process(CommandManager.Type.VARIABLE ,temp, aLevel + 1);
            for(String s : temp)
            {
                super.mRDFWriter.write(cycleName, s, "has_part");
            }

            //mRDFWriter.write(cycleName, aList.get(1), "has_part");
            super.mRDFWriter.write(cycleName, "cycle_for", "ISA");

            result.add(cycleName);

            mUsedCycles.add("for");
        }else {
            String cycleName = "while" + "_" + ++mCycleCounter;
            //TODO: while without breakets {}
            //TODO: conditions in cycle{}
            //mRDFWriter.write(cycleName, aList.get(1), "has_part");
            super.mRDFWriter.write(cycleName, "cycle_while", "ISA");

            result.add(cycleName);

            mUsedCycles.add("while");
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean checkSequence(String aStr) {
        return isForSequence(aStr) || isWhileSequence(aStr);
    }

    private boolean isForSequence(String aStr) {
        return Objects.equals(aStr, "for");
    }

    private boolean isWhileSequence(String aStr) {
        return Objects.equals(aStr, "while");
    }

    @Override
    public CommandManager.Type getType() {
        return CommandManager.Type.CYCLE;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void writePack() {
        if (mUsedCycles.size() > 0)
        {
            super.mRDFWriter.write("cycle", "start", "implement");

            if (mUsedCycles.contains("for"))
                super.mRDFWriter.write("cycle_for", "cycle", "AKO");


            if (mUsedCycles.contains("while"))
                super.mRDFWriter.write("cycle_while", "cycle", "AKO");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

}