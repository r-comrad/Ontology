import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class FunctionDecoder {
    private RDFWriter mRDFWriter;
    private HashSet<String> mUsedFunctions;

    //TODO: function call without asignment

    public FunctionDecoder(RDFWriter aRDFWriter)
    {
        mRDFWriter = aRDFWriter;
        mUsedFunctions= new HashSet<>();
    }

    public String process(List<String> aList)
    {
        String result = "____fncDec";
        if (aList.size() > 1)
        {
            functionDecoder(aList);
            result = aList.get(0)
        }
        //else stdFunctionPack();
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isFunctionalSequence(String aStr) {
        return Objects.equals(aStr, "(");
    }

    //------------------------------------------------------------------------------------------------------------------

    public void writePack()
    {
        functionPack();
        userFunctionPack();
        stdFunctionPack();
    }

    private void functionPack() {
        mRDFWriter.write("function", "start", "implement");
        mRDFWriter.write("function", "types", "take");
        mRDFWriter.write("function", "types", "return");
        mRDFWriter.write("user_function", "function", "AKO");
    }

    private void userFunctionPack() {
        mRDFWriter.write("user_function", "function", "AKO");
    }

    private void stdFunctionPack() {
        boolean isAnyStdFunkWrited = false;

        MyFileReader fileReader = new MyFileReader("../res/words", "std_functions.txt");
        int groupsCount = Integer.parseInt(fileReader.read());

        for (int i = 0; i < groupsCount; ++i) {
            String parent = fileReader.read();
            int groupSize = Integer.parseInt(fileReader.read());
            boolean isWrited = false;

            for (int j = 0; j < groupSize; ++j) {
                String funkName = fileReader.read();
                if (mUsedFunctions.contains(funkName)) {
                    mRDFWriter.write(funkName, parent + "_function", "ISA");
                    isWrited = true;
                }
            }

            if (isWrited) {
                mRDFWriter.write(parent + "_function", "std_function", "ISA");
                isAnyStdFunkWrited = true;
            }
        }

        if (isAnyStdFunkWrited) {
            mRDFWriter.write("std_function", "function", "AKO");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private void functionDecoder(List<String> aList) {
        //lastBlockLabel = aList.get(1);
        //writeLever(aList.get(1));

        mRDFWriter.write(aList.get(1), aList.get(0), "return");
        mRDFWriter.write(aList.get(1), "user_function", "ISA");
        for (int i = 2; i < aList.size(); ++i) {
            mRDFWriter.write(aList.get(1), aList.get(i + 1), "take");
        }
    }
}
