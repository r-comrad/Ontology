import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class CycleDecoder extends Decoder {
    private RDFWriter mRDFWriter;
    VariableDecoder mVariableDecoder;
    private int mCycleCounter;

    private HashSet<String> mUsedCycles;

    public CycleDecoder(RDFWriter aRDFWriter, VariableDecoder aVariableDecoder) {
        mRDFWriter = aRDFWriter;
        mVariableDecoder = aVariableDecoder;
        mCycleCounter = 0;

        mUsedCycles = new HashSet<>();
    }

    @Override
    public List<String> process(List<String> aList) {
        List<String> result = new ArrayList<>();

        if (isForSequence(aList.get(0)))
        {
            mVariableDecoder.inCycleDeclarationDecoder(aList);
            String cycleName = "for" + "_" + ++mCycleCounter;
            mRDFWriter.write(cycleName, aList.get(1), "has_part");
            mRDFWriter.write(cycleName, "cycle_for", "ISA");

            result.add(cycleName);

            mUsedCycles.add("for");
        }else {
            String cycleName = "while" + "_" + ++mCycleCounter;
            mRDFWriter.write(cycleName, aList.get(1), "has_part");
            mRDFWriter.write(cycleName, "cycle_while", "ISA");

            result.add(cycleName);

            mUsedCycles.add("while");
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean checkSequence(String aStr) {
        return isForSequence(aStr) || isWhileTypeSequence(aStr);
    }

    private boolean isForSequence(String aStr) {
        return Objects.equals(aStr, "for");
    }

    private boolean isWhileTypeSequence(String aStr) {
        return Objects.equals(aStr, "while");
    }

    @Override
    public Type getType() {
        return Type.CYCLE;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void writePack() {
        if (mUsedCycles.size() > 0)
        {
            mRDFWriter.write("cycle", "start", "implement");

            if (mUsedCycles.contains("for"))
                mRDFWriter.write("cycle_for", "cycle", "AKO");


            if (mUsedCycles.contains("while"))
                mRDFWriter.write("cycle_while", "cycle", "AKO");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

}
