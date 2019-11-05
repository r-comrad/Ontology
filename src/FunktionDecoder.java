import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class FunktionDecoder {
    private RDFWriter mRDFWriter;
    private HashSet<String> mUsedFunctions;

    public FunktionDecoder(RDFWriter aRDFWriter)
    {
        mRDFWriter = aRDFWriter;
        mUsedFunctions= new HashSet<>();
    }

    public boolean isFunctionalSequence(String aStr) {
        return Objects.equals(aStr, "(");
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
                    mRDFWriter.write(funkName, parent + "_functions", "ISA");
                    isWrited = true;
                }
            }

            if (isWrited) {
                mRDFWriter.write(parent + "_functions", "std_functions", "ISA");
                isAnyStdFunkWrited = true;
            }
        }

        if (isAnyStdFunkWrited) {
            mRDFWriter.write("std_functions", "function", "AKO");
        }
    }
    public void functionDecoder(List<String> aList) {
        //lastBlockLabel = aList.get(1);
        //writeLever(aList.get(1));

        mRDFWriter.write(aList.get(1), aList.get(0), "return");
        mRDFWriter.write(aList.get(1), "user_functions", "ISA");
        for (int i = 2; i < aList.size(); ++i) {
            mRDFWriter.write(aList.get(1), aList.get(i + 1), "take");
        }
    }
}
