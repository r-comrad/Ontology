import java.util.*;

public class DecoderVariable extends Decoder {
    private RDFWriter mRDFWriter;

    private int mAssignmentCounter;

    private HashSet<String> mUsedBasicTypes;
    private HashSet<String> mUsedContainers;

    private HashSet<String> mBasicTypesList;
    private HashSet<String> mContainersList;

    //TODO: singl function call in function
    private int mMethodCall;
    private HashSet<String> mMethodsName;

    // private HashMap<String, Integer> mTypeNamesCounter;
    private HashMap<List<String>, String> mStoredSubtypes;

    //------------------------------------------------------------------------------------------------------------------

    public DecoderVariable(RDFWriter aRDFWriter) {
        mRDFWriter = aRDFWriter;
        mAssignmentCounter = 0;

        mUsedBasicTypes = new HashSet<>();
        mUsedContainers = new HashSet<>();

        //TODO: standart path ../   ./
        MyFileReader typesFile = new MyFileReader("words/basic_types.txt");
        mBasicTypesList = typesFile.readAllWords();
        typesFile = new MyFileReader("words/containers.txt");
        mContainersList = typesFile.readAllWords();

        mMethodCall = 0;
        mMethodsName= new HashSet<>();
        mMethodsName.add("push");
        mMethodsName.add("empty");
        mMethodsName.add("front");

        mTypeNamesCounter =  new HashMap<>();
        mStoredSubtypes =  new HashMap<>();
    }

    @Override
    public List<String> process(List<String> aList, int aLevel) {
        List<String> result = new ArrayList<>();
        int number = Math.max(1, Math.max(Math.max(aList.lastIndexOf(">"),
                aList.lastIndexOf("*")),
                aList.lastIndexOf("&")));
        //TODO: remove & *

        List<String> type = aList.subList(0, number + 1);
        List<String> variables = aList.subList(number + 1, aList.size());

        String s = typeDecoder(type);
        declarationDecoder(s, variables);
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
    public CommandManager.Type getType() {
        return CommandManager.Type.VARIABLE;
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

    private String typeDecoder(List<String> aList) {
        String result = "___VAR_REC";
        if (mStoredSubtypes.containsKey(aList)) {
            result = mStoredSubtypes.get(aList);
        } else {
            if (aList.contains("<")) {
                //TODO has pointer ( * / & )
                String currentType = aList.get(0);
                mUsedContainers.add(currentType);

                if (!mTypeNamesCounter.containsKey(currentType))
                {
                    mTypeNamesCounter.put(currentType, 0);
                }

                String currentContainer = "t_" + currentType + "_" + mTypeNamesCounter.get(currentType);
                mTypeNamesCounter.put(currentType, mTypeNamesCounter.get(currentType) + 1);

                List<String> nextIteration = aList.subList(2, aList.size() - 1);
                String nextResult = typeDecoder(nextIteration);

                mRDFWriter.write(currentContainer, currentType + "_type", "ISA");
                mRDFWriter.write(currentContainer, nextResult, "has_part");
                //mRDFWriter.write(currentContainer, "container", "ISA");

                result = currentContainer;
            } else {
                String currentType = aList.get(0);
                mUsedBasicTypes.add(currentType);
                mRDFWriter.write(currentType, "basic_type", "ISA");
                result = currentType;
            }
            mStoredSubtypes.put(aList, result);
        }

        return result;
    }

    private List<String> declarationDecoder(String aType, List<String> aList) {
        List<String> result = new ArrayList<>();
        String variableType = "___VAR_TYP";
        if (isBasicTypeSequence(aType)) variableType = "basic_variable";
        else if (true)  variableType = "container_variable";

        while (aList.size() > 0) {
            //List<String> currentVariable = aList.subList(0, aList.indexOf(','));
            String currentVariable = aList.get(0);
            //aList = aList.subList(aList.indexOf(',') + 1, aList.size());
            aList = aList.subList(1, aList.size());

            //TODO: constructor (,,,),
            mRDFWriter.write(currentVariable, aType, "has_type");
            mRDFWriter.write(currentVariable, variableType, "ISA");
            result.add(currentVariable);

            /*

             if (isFunctionCallOpenerSequence(s))
                {
                    int j = i;
                    int openerCount = 0;
                    List <String> temp = new LinkedList();//(aList.subList(i + 1, j));
                    do
                    {
                        if (Objects.equals(aList.get(j), ")")) --openerCount;
                        else if (Objects.equals(aList.get(j), "(")) ++openerCount;
                        else temp.add(aList.get(j));
                        ++j;
                    }
                    while (j < aList.size() && openerCount > 0);

                    for(String ss : temp)
                    {
                        //TODO: this is kostil
                        if (!(Objects.equals(ss, "vector")) &&
                                !(Objects.equals(ss, "int")) &&
                                !( '0' <= ss.codePointAt(0)  && '9' >= ss.codePointAt(0)))
                        {
                            mRDFWriter.write(aList.get(i - 1), ss, "has_part");
                        }
                        //aList.remove(ss);
                    }
                    i = j;
                }
             */
        }

        return result;
    }


    private List<String> basicVariableAssignmentDecoder(List<String> aList) {
        //TODO: function use
        List<String> result = new ArrayList<>();
        String valueName = "value" + mAssignmentCounter++;
        result.add(valueName);
        mRDFWriter.write(valueName, aList.get(0), "assignment");
        for (int i = 1; i < aList.size(); ++i) {
            String s = aList.get(i);
            for(String ss : mMethodsName)
            {
                if (s.contains(ss))
                {
                    String methodCallName = "method_call_" + mMethodCall++;
                    mRDFWriter.write(valueName, methodCallName, "has_part");

                    String variableName = s.substring(0, s.indexOf('.'));
                    mRDFWriter.write(methodCallName, variableName, "has_part");

                    String methodName = s.substring(s.indexOf('.') + 1);
                    mRDFWriter.write(methodCallName, methodName, "has_part");
                    s = " ";
                }
            }
            if (s.codePointAt(0) >= 'A' && s.codePointAt(0) <= 'Z' ||
                    s.codePointAt(0) >= 'a' && s.codePointAt(0) <= 'z')
            {
                if (s.contains("["))
                {
                    //TODO: upgrade
                    s = s.substring(0, s.length() - 3);
                }
                mRDFWriter.write(valueName, s, "has_part");
            }
        }
        return result;
    }
}