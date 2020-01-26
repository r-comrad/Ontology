import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ConditionDecoder extends Decoder {
    private RDFWriter mRDFWriter;

    private int mConditionCounter;
    List<String> mConditionNames;
    List<Integer> mConditionTypes;
    List<Integer> mConditionCounters;

    private int mUsedConditions;

    //private String lastBlock;
    //private boolean mLastBlockIsCondition;

    public ConditionDecoder(RDFWriter aRDFWriter) {
        mRDFWriter = aRDFWriter;

        mConditionCounter = 0;
        mConditionNames = new ArrayList<>();
        mConditionTypes = new ArrayList<>();
        mConditionCounters = new ArrayList<>();

        mUsedConditions = 0;
    }

    public void increaseLevel() {
        conditionAdder();
    }
    public void decreaseLevel() {
       conditionCloser();
    }

    @Override
    public List<String> process(List<String> aList) {
        List<String> result = new ArrayList<>();
        new ArrayList<>();

        if (isIfSequence(aList.get(0))) {
            result = conditionOpener();
            List<String> secondResult = conditionDecoder(aList);
            result.add(secondResult.get(0));
        } else if (aList.size() > 1 && isElseIfSequence(aList.get(0), aList.get(1)) ||
                isElseSequence(aList.get(0))) {
            result = conditionDecoder(aList);
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean checkSequence(String aStr) {

        return isIfSequence(aStr) || isElseSequence(aStr);
    }

    private boolean isIfSequence(String aStr) {
        return Objects.equals(aStr, "if");
    }

    private boolean isElseSequence(String aStr) {
        return Objects.equals(aStr, "else");
    }

    private boolean isElseIfSequence(String aStr1, String aStr2) {
        return Objects.equals(aStr1, "else") && Objects.equals(aStr2, "if");
    }

    @Override
    public Type getType(){ return Type.CONDITION; }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void writePack()
    {
        if (mConditionCounter > 0)
        {
            mRDFWriter.write("conditions_types", "start", "implement");
            conditionPack();
        }
    }

    private void conditionPack() {
        if ((mUsedConditions & 1) != 0) mRDFWriter.write("if", "conditions_types", "AKO");
        if ((mUsedConditions & 2) != 0) mRDFWriter.write("if-else", "conditions_types", "AKO");
        if ((mUsedConditions & 4) != 0) mRDFWriter.write("if-else_tree", "conditions_types", "AKO");
    }

    //------------------------------------------------------------------------------------------------------------------

    private void conditionAdder() {
        mConditionNames.add("");
        mConditionTypes.add(-1);
        mConditionCounters.add(0);
    }

    private List<String> conditionOpener() {
        List<String> result = new ArrayList<>();
        String conditionName = "condition" + "_" + ++mConditionCounter;

        //int conditionNumber = mConditionNames.size() - 1;
        //mConditionNames.set(conditionNumber, conditionName);
        mConditionNames.add(conditionName);
        mConditionTypes.add(0);
        mConditionCounters.add(0);

        result.add(conditionName);
        return result;
    }

    private void conditionCloser() {
        int conditionNumber = mConditionNames.size() - 1;
        String conditionName = mConditionNames.get(conditionNumber);
        mConditionNames.remove(conditionNumber);
        Integer conditionType = mConditionTypes.get(conditionNumber);
        mConditionTypes.remove(conditionNumber);

        String parent = "________PC";
        if (conditionType == -1) {
            return;
        } else if (conditionType == 1) {
            parent = "if";
            mUsedConditions |= 1;
            mConditionTypes.set(conditionNumber, 1);
        } else if (conditionType == 3) {
            parent = "if-else";
            mUsedConditions |= 2;
        } else if (conditionType > 3) {
            parent = "if-else_tree";
            mUsedConditions |= 4;
        }
        mRDFWriter.write(conditionName, parent, "ISA");
    }

    private List<String>  conditionDecoder(List<String> aList) {
        List<String> result = new ArrayList<>();
        // TODO: if else if (2 условия)
        //TODO: funktion call inside condition
        int conditionNumber = mConditionNames.size() - 1;
        String conditionName = mConditionNames.get(conditionNumber);
        int subConditionNumber = mConditionCounters.get(conditionNumber);

        String blockType = "________BT";
        if (isIfSequence(aList.get(0))) {
            blockType = "if";

            Integer curType = mConditionTypes.get(conditionNumber);
            mConditionTypes.set(conditionNumber, curType + 1);
        } else if (aList.size() > 1 && isElseIfSequence(aList.get(0), aList.get(1))) {
            blockType = "else-if_block";
            aList.remove(0);

            Integer curType = mConditionTypes.get(conditionNumber);
            mConditionTypes.set(conditionNumber, curType + 4);
        } else if (isElseSequence(aList.get(0))) {
            blockType = "else_block";

            Integer curType = mConditionTypes.get(conditionNumber);
            mConditionTypes.set(conditionNumber, curType + 2);
        }
        String blockName = blockType + "_" + mConditionCounter + "_" + subConditionNumber;
        mRDFWriter.write(blockName, conditionName, "has_part");

        ++subConditionNumber;
        mConditionCounters.set(conditionNumber, subConditionNumber);

        for (int i = 1; i < aList.size(); ++i) {
            if (aList.get(i).codePointAt(0) >= 'A' && aList.get(i).codePointAt(0) <= 'Z' ||
                    aList.get(i).codePointAt(0) >= 'a' && aList.get(i).codePointAt(0) <= 'z')
                mRDFWriter.write(blockName, aList.get(i), "take");
        }

        result.add(blockName);
        return result;
    }
}