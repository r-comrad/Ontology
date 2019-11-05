import java.io.FileReader;
import java.util.*;

public class VariableDecoder {
    private RDFWriter mRDFWriter;

    private int mAssignmentCounter;

    private HashSet<String> mUsedBasicTypes;
    private HashSet<String> mUsedContainers;

    private HashSet<String> mBasicTypesList;
    private HashSet<String> mContainersList;

    //------------------------------------------------------------------------------------------------------------------

    public VariableDecoder(RDFWriter aRDFWriter)
    {
        mRDFWriter = aRDFWriter;
        mAssignmentCounter = 0;

        MyFileReader typesFile = new MyFileReader("../res/words", "basic_types.txt");
        mUsedBasicTypes = typesFile.readAllWords();
        typesFile = new MyFileReader("../res/words", "containers.txt");
        mUsedContainers = typesFile.readAllWords();
    }

    public void process(List<String> aList)
    {
        //TODO: const types
        if (isBasicTypeSequence(aList.get(0))) basicVariableDeclarationDecoder(aList);
        else if(isContainerSequence(aList.get(0))) containerDeclarationDecoder(aList);
        else if(isBasicAssignmentSequence(aList.get(0))) basicVariableAssignmentDecoder(aList);
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isVariableSequence(String aStr) {
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

    //------------------------------------------------------------------------------------------------------------------

    public void writePack()
    {
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
        mRDFWriter.write("container_type", "type", "AKO");
        mRDFWriter.write("container", "variable", "AKO");

        Iterator<String> i = mUsedContainers.iterator();
        while (i.hasNext())
        {
            mRDFWriter.write(i.next(), "container_type", "ISA");
        }
    }

    private void basicVariablePack() {
        mRDFWriter.write("basic_variable", "variable", "AKO");
        for(String i : mUsedBasicTypes)
        {
            //if (mUsedTypes.contains(parent))
            mRDFWriter.write(i, "basic_type", "ISA");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private void basicVariableDeclarationDecoder(List<String> aList) {
        mUsedBasicTypes.add(aList.get(0));
        for (int i = 1; i < aList.size(); ++i) {
            //writeLever(aList.get(i));
            mRDFWriter.write(aList.get(i), aList.get(0), "has_type");
            mRDFWriter.write(aList.get(i), "basic_variable", "ISA");
        }
    }

    private void basicVariableAssignmentDecoder(List<String> aList) {
        String valueName = "value" + mAssignmentCounter++;
        //writeLever(valueName);
        mRDFWriter.write(aList.get(0), valueName, "assignment");
        for (int i = 1; i < aList.size(); ++i) {
            if (aList.get(i).codePointAt(0) >= 'A' && aList.get(i).codePointAt(0) <= 'Z' ||
                    aList.get(i).codePointAt(0) >= 'a' && aList.get(i).codePointAt(0) <= 'z')
                mRDFWriter.write(valueName, aList.get(i), "use");
            //TODO: use -> has_part
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    // TODO: ofset for map
    private void containerDeclarationDecoder(List<String> aList) {
        mUsedContainers.add(aList.get(0));
        for (int i = 2; i < aList.size(); ++i) {
            //writeLever(aList.get(i));
            mRDFWriter.write(aList.get(i), aList.get(0), "has_type");
            mRDFWriter.write(aList.get(i), aList.get(1), "stores");
            mRDFWriter.write(aList.get(i), "container", "ISA");
        }
    }

    private void containerAssignmentDecoder(List<String> aList) {}

    //------------------------------------------------------------------------------------------------------------------

    public void infunctionDeclarationDecoder(List<String> aList) {
        if (isContainerSequence(aList.get(0)))
        {
            aList.remove(2);
            // TODO: ofset for map
        }

        for (int i = 2; i < aList.size();) {
            String curStr = aList.get(i);
            if (isBasicTypeSequence(curStr))
            {
                //List<String> basicVariables = new ArrayList<>();
                //basicVariables.add(aList.get(i));
                //basicVariables.add(aList.get(i + 1));
                basicVariableDeclarationDecoder(aList.subList(i, i + 1));
                aList.remove(i);
                i++;
            }
            else if(isContainerSequence(curStr))
            {
                containerDeclarationDecoder(aList.subList(i, i + 2));
                aList.remove(i);
                aList.remove(i);
                i++;
            }
        }
    }
}
