import java.io.FileReader;
import java.util.*;

public class VariableDecoder extends Decoder {
    private RDFWriter mRDFWriter;

    private int mAssignmentCounter;

    private HashSet<String> mUsedBasicTypes;
    private HashSet<String> mUsedContainers;

    private HashSet<String> mBasicTypesList;
    private HashSet<String> mContainersList;

    //------------------------------------------------------------------------------------------------------------------

    public VariableDecoder(RDFWriter aRDFWriter) {
        mRDFWriter = aRDFWriter;
        mAssignmentCounter = 0;

        mUsedBasicTypes = new HashSet<>();
        mUsedContainers = new HashSet<>();

        //TODO: standart path ../   ./
        MyFileReader typesFile = new MyFileReader("words/basic_types.txt");
        mBasicTypesList = typesFile.readAllWords();
        typesFile = new MyFileReader("words/containers.txt");
        mContainersList = typesFile.readAllWords();
    }

    @Override
    public List<String> process(List<String> aList) {
        List<String> result = new ArrayList<>();
        //TODO: const types
        if (isBasicTypeSequence(aList.get(0))) result = basicVariableDeclarationDecoder(aList);
        else if (isContainerSequence(aList.get(0))) result = containerDeclarationDecoder(aList);
        else if (aList.size() > 1 && isBasicAssignmentSequence(aList.get(1)))
            result = basicVariableAssignmentDecoder(aList);
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean checkSequence(String aStr) {
        return isTypeSequence(aStr) || isBasicAssignmentSequence(aStr);
    }

    private boolean isTypeSequence(String aStr) {
        return isBasicTypeSequence(aStr) || isContainerSequence(aStr);
    }

    private boolean isBasicTypeSequence(String aStr) {
        return mBasicTypesList.contains(aStr);
    }

    private boolean isContainerSequence(String aStr) {
        return mContainersList.contains(aStr);
    }

    private boolean isBasicAssignmentSequence(String aStr) {
        return Objects.equals(aStr, "=");
    }

    @Override
    public Type getType() {
        return Type.VARIABLE;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void writePack() {
        variablePack();
        containerPack();
        basicVariablePack();
    }

    private void variablePack() {
        mRDFWriter.write("type", "start", "implement");
        mRDFWriter.write("basic_type", "type", "AKO");

        //TODO: no variables
        mRDFWriter.write("variable", "start", "implement");
        mRDFWriter.write("variable", "type", "has_type");
    }

    private void containerPack() {
        if (mUsedContainers.size() == 0) return;

        mRDFWriter.write("container_type", "type", "AKO");
        mRDFWriter.write("container", "variable", "AKO");

        Iterator<String> i = mUsedContainers.iterator();
        while (i.hasNext()) {
            mRDFWriter.write(i.next(), "container_type", "ISA");
        }
    }

    private void basicVariablePack() {
        mRDFWriter.write("basic_variable", "variable", "AKO");
        for (String i : mUsedBasicTypes) {
            mRDFWriter.write(i, "basic_type", "ISA");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private List<String> basicVariableDeclarationDecoder(List<String> aList) {
        List<String> result = new ArrayList<>();
        mUsedBasicTypes.add(aList.get(0));
        for (int i = 1; i < aList.size(); ++i) {
            mRDFWriter.write(aList.get(i), aList.get(0), "has_type");
            mRDFWriter.write(aList.get(i), "basic_variable", "ISA");
            result.add(aList.get(i));
        }
        return result;
    }

    private List<String> basicVariableAssignmentDecoder(List<String> aList) {
        List<String> result = new ArrayList<>();
        String valueName = "value" + mAssignmentCounter++;
        result.add(valueName);
        mRDFWriter.write(valueName, aList.get(0), "assignment");
        for (int i = 1; i < aList.size(); ++i) {
            if (aList.get(i).codePointAt(0) >= 'A' && aList.get(i).codePointAt(0) <= 'Z' ||
                    aList.get(i).codePointAt(0) >= 'a' && aList.get(i).codePointAt(0) <= 'z')
                mRDFWriter.write(valueName, aList.get(i), "has_part");
            //TODO: use -> has_part
        }
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    // TODO: ofset for map
    private List<String> containerDeclarationDecoder(List<String> aList) {
        List<String> result = new ArrayList<>();
        mUsedContainers.add(aList.get(0));
        for (int i = 2; i < aList.size(); ++i) {
            result.add(aList.get(i));
            mRDFWriter.write(aList.get(i), aList.get(0), "has_type");
            mRDFWriter.write(aList.get(i), aList.get(1), "stores");
            mRDFWriter.write(aList.get(i), "container", "ISA");
        }
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void infunctionDeclarationDecoder(List<String> aList) {
        if (isContainerSequence(aList.get(0))) {
            aList.remove(2);
            // TODO: ofset for map
        }

        for (int i = 2; i < aList.size(); ++i) {
            String curStr = aList.get(i);
            if (isBasicTypeSequence(curStr)) {
                basicVariableDeclarationDecoder(aList.subList(i, i + 1));
                aList.remove(i);
                i++;
            } else if (isContainerSequence(curStr)) {
                containerDeclarationDecoder(aList.subList(i, i + 2));
                aList.remove(i);
                aList.remove(i);
                i++;
            }
        }
    }

    public void inCycleDeclarationDecoder(List<String> aList) {
        for (int i = 1; i < aList.size(); ++i) {
            String curStr = aList.get(i);
            if (isBasicTypeSequence(curStr)) {
                basicVariableAssignmentDecoder(aList.subList(i, i + 1));
                aList.remove(i);
                aList.remove(i + 1);
                aList.remove(i + 1);
                i += 3;
            } else if (isContainerSequence(curStr)) {
                //TODO:
            }
        }
    }
}