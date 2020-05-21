import java.util.*;

public class DecoderFunction extends Decoder {
    private RDFWriter mRDFWriter;
    private HashSet<String> mUsedFunctions;

    DecoderVariable mDecoderVariable;

    //TODO: function call without asignment

    public DecoderFunction(DecodersArray aDecodersArray, RDFWriter aRDFWriter) {
        super(aDecodersArray, aRDFWriter);
        mUsedFunctions = new HashSet<>();
    }

    @Override
    public List<String> process(List<String> aList, int aLevel) {
        clearFunctionCall(aList);

        List<String> result = new ArrayList<>();
        if (aList.size() > 1) {
            functionDecoder(aList);
            result.add(aList.get(1));
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean checkSequence(String aStr) {
        return Objects.equals(aStr, "(");
    }

    @Override
    public CommandManager.Type getType() {
        return CommandManager.Type.FUNCTION;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void writePack() {
        functionPack();
        userFunctionPack();
        stdFunctionPack();
    }

    private void functionPack() {
        super.mRDFWriter.write("function", "start", "implement");
        super.mRDFWriter.write("function", "type", "has_part");
        super.mRDFWriter.write("function", "type", "return");
    }

    private void userFunctionPack() {
        super.mRDFWriter.write("user_function", "function", "AKO");
    }

    private void stdFunctionPack() {
        boolean isAnyStdFunkWrited = false;

        MyFileReader fileReader = new MyFileReader("std_functions.txt");
        int groupsCount = Integer.parseInt(fileReader.read());

        for (int i = 0; i < groupsCount; ++i) {
            String parent = fileReader.read();
            int groupSize = Integer.parseInt(fileReader.read());
            boolean isWrited = false;

            for (int j = 0; j < groupSize; ++j) {
                String funkName = fileReader.read();
                if (mUsedFunctions.contains(funkName)) {
                    super.mRDFWriter.write(funkName, parent + "_function", "ISA");
                    isWrited = true;
                }
            }

            if (isWrited) {
                super.mRDFWriter.write(parent + "_function", "std_function", "ISA");
                isAnyStdFunkWrited = true;
            }
        }

        if (isAnyStdFunkWrited) {
            super.mRDFWriter.write("std_function", "function", "AKO");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private void functionDecoder(List<String> aList) {
        super.mRDFWriter.write(aList.get(1), aList.get(0), "return");
        super.mRDFWriter.write(aList.get(1), "user_function", "ISA");
        for (int i = 2; i < aList.size(); ++i) {
            super.mRDFWriter.write(aList.get(1), aList.get(i + 1), "has_part");
        }
    }
}