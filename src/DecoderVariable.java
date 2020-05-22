import javax.swing.*;
import java.util.*;

public class DecoderVariable extends Decoder {
    // File for outputing graph
    private RDFWriter mRDFWriter;

    // Counter for assignment in program (int n = 5 | f = { 9, 0})
    private int mAssignmentCounter;

    // Arrays for types used in current program
    private HashSet<String> mUsedBasicTypes;   // basic types like int, double
    private HashSet<String> mUsedContainers;   // container types like vector, queue

    // Arrays for types used in C++
    private HashSet<String> mBasicTypesList;    // basic types like int, double
    private HashSet<String> mContainersList;    // container types like vector, queue

    private Integer mMethodCount = 0;

    //TODO: singl function call in function
    private int mMethodCall;
    private HashSet<String> mMethodsName;

    private HashMap<List<String>, String> mStoredSubtypes;

    private Stack<HashMap<String, String>> mVariableNamesStack;
    HashMap<String, Integer> mVariableNamesCounet;

    //------------------------------------------------------------------------------------------------------------------

    public DecoderVariable(DecodersArray aDecodersArray, RDFWriter aRDFWriter) {
        super(aDecodersArray, aRDFWriter);
        mAssignmentCounter = 0;

        mUsedBasicTypes = new HashSet<>();
        mUsedContainers = new HashSet<>();

        MyFileReader reader = new MyFileReader();
        mBasicTypesList = reader.readAllWords(MyFileReader.BASIC_TYPES_PATH);
        mContainersList = reader.readAllWords(MyFileReader.CONTAINERS_PATH);
        mMethodsName = reader.readAllWords(MyFileReader.METHODS_PATH);

        mMethodCall = 0;
        //mMethodsName= new HashSet<>();
        //mMethodsName.add("push");
        //mMethodsName.add("empty");
        //mMethodsName.add("front");

        mStoredSubtypes =  new HashMap<>();

        mVariableNamesStack = new Stack<>();
        mVariableNamesCounet = new HashMap<>();
    }

    String findVariableName(String aName) {
        String result = "";

        for (int i = 1; i < mVariableNamesStack.size(); ++i) {
            HashMap<String, String> currentNames = mVariableNamesStack.get(mVariableNamesStack.size() - i);

            if (currentNames.containsKey(aName)) {
                result = currentNames.get(aName);
            }
        }

        return result;
    }

    String addVariableName(String aName, String aType) {
        String result = "";

        if (!mVariableNamesCounet.containsKey(aType)) {
            mVariableNamesCounet.put(aType, 0);
        } else {
            mVariableNamesCounet.put(aType, mVariableNamesCounet.get(aType) + 1);
        }

        HashMap<String, String> currentNames = mVariableNamesStack.get(mVariableNamesStack.size() - 1);
        String newName = aType + "_var_" + mVariableNamesCounet.get(aType).toString();
        currentNames.put(aName, newName);
        result = newName;

        return result;
    }

    @Override
    public List<String> process(List<String> aList, int aLevel) {
        List<String> result = new ArrayList<>();
        leavelController(aLevel);
        if (aList.size() == 0) return result;


        if (aList.contains(".")) result = methodProcess(aList);
        else if (aList.get(0).equals("___FUN_VAR"))
        {
            aList = aList.subList(1, aList.size());
            String s = aList.get(0);
            if (isBasicTypeSequence(s) || isContainerSequence(s))
            {
                while(aList.size() > 0)
                {
                    int num = aList.indexOf(",");
                    result.addAll(process(aList.subList(0, num), aLevel));
                    aList = aList.subList(num + 1, aList.size());
                }
            }
            else result = getVariablesNames(aList, "___NUN_TYP");
        }
        else result = preDeclarationDecoder(aList);
        return result;
    }

    private List<String> getVariablesNames(List<String> aVariables, String aTypeName) {
        if (!aVariables.get(aVariables.size() - 1).equals(","))
            aVariables.add(","); // for proper slising by char ','

        List<String> result = new ArrayList<>();
        while (aVariables.size() > 0) {
            String currentVariableName = aVariables.get(0);
            String name = findVariableName(currentVariableName);
            if (name.equals("")) name = addVariableName(currentVariableName, aTypeName);
            result.add(name);
            aVariables = aVariables.subList(aVariables.indexOf(",") + 1, aVariables.size());
        }

        return result;
    }

    private List<String> preDeclarationDecoder(List<String> aList) {
        aList.add(","); // for proper slising by char ','

        int typeEndNumber = Math.max(0, Math.max(Math.max(aList.lastIndexOf(">"),
                aList.lastIndexOf("*")),
                aList.lastIndexOf("&")));
        //TODO: remove & *

        List<String> type = aList.subList(0, typeEndNumber + 1);
        String typeName = typeDecoder(type);

        List<String> variables = aList.subList(typeEndNumber + 1, aList.size());
        List<String> variablesNames = getVariablesNames(variables, typeName);

        List<String> result = declarationDecoder(typeName, variablesNames);
        return result;
    }


    public List<String> methodProcess(List<String> aList) {
        String variableName = aList.get(0);
        String methodName = aList.get(2);
        List<String> arguments = aList.subList(4, aList.size() - 1);
        arguments.add(","); // for proper slising by char ','

        if (variableName.indexOf('[') != -1)
            variableName = variableName.substring(0, variableName.indexOf('[')); //TODO: kostil for array
        variableName = findVariableName(variableName);
        String nodeName = "method_call_" + mMethodCount.toString() + "_" + methodName;
        ++mMethodCount;
        super.mRDFWriter.write(nodeName, variableName, "call");

        String typeCoreName = variableName.substring(0, variableName.indexOf('_', 2));
        super.mRDFWriter.write(nodeName, typeCoreName + "_" + methodName, "ISA");

        while (arguments.size() > 0) {
            String currentVariableName = arguments.get(0);
            currentVariableName = findVariableName(currentVariableName);
            super.mRDFWriter.write(currentVariableName, nodeName, "has_part");
            arguments = arguments.subList(arguments.indexOf(",") + 1, arguments.size());
        }

        List<String> result = new ArrayList<String>();
        result.add(nodeName);
        return result;
    }

    public void leavelController(int aLevel)
    {
        while (aLevel > mVariableNamesStack.size()) mVariableNamesStack.push(new HashMap<>());
        while (aLevel < mVariableNamesStack.size()) mVariableNamesStack.pop();
        //if (1 > mVariableNamesStack.size()) mVariableNamesStack.push(new HashMap<>());
    }

    private HashMap<String, String> getCurrentVariablePack()
    {
        return mVariableNamesStack.get(mVariableNamesStack.size() - 1);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean checkSequence(String aStr) {
        return isTypeSequence(aStr) || isBasicAssignmentSequence(aStr) || isMethodSequnce(aStr);
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

    private boolean isMethodSequnce(String aStr) {
        return isPointSequnce(aStr) || isVariableNameSequnce(aStr);
    }

    private boolean isPointSequnce(String aStr) {
        return Objects.equals(aStr, ".");
    }

    private boolean isVariableNameSequnce(String aStr) {
        return !findVariableName(aStr).equals("");
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
        super.mRDFWriter.write("type", "start", "implement");
        super.mRDFWriter.write("basic_type", "type", "AKO");

        //TODO: no variables
        super.mRDFWriter.write("variable", "start", "implement");
        super.mRDFWriter.write("variable", "type", "has_type");
    }

    private void containerPack() {
        if (mUsedContainers.size() == 0) return;

        super.mRDFWriter.write("container_type", "type", "AKO");
        super.mRDFWriter.write("container", "variable", "AKO");

        Iterator<String> i = mUsedContainers.iterator();
        while (i.hasNext()) {
            super.mRDFWriter.write(i.next(), "container_type", "ISA");
        }
    }

    private void basicVariablePack() {
        super.mRDFWriter.write("basic_variable", "variable", "AKO");
        for (String i : mUsedBasicTypes) {
            super.mRDFWriter.write(i, "basic_type", "ISA");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private int typeCounter(String aType)
    {
        int result = 0;
        for(Map.Entry<List<String>, String> entry : mStoredSubtypes.entrySet()) {
            if(entry.getValue().contains(aType)) ++result;
        }
        return result;
    }

    private String typeDecoder(List<String> aList) {
        String result = "___VAR_REC";
        if (mStoredSubtypes.containsKey(aList)) {
            result = mStoredSubtypes.get(aList);
        } else {
            if (aList.contains("<")) {
                //TODO has pointer ( * / & )
                String currentType = aList.get(0);
                mUsedContainers.add(currentType);

                String currentContainer = "t_" + currentType + "_" + typeCounter(currentType);
                mStoredSubtypes.put(new ArrayList<> (aList), currentContainer);

                List<String> nextIteration = aList.subList(2, aList.size() - 1);
                String nextResult = typeDecoder(nextIteration);

                super.mRDFWriter.write(currentContainer, currentType + "_type", "ISA");
                super.mRDFWriter.write(currentContainer, nextResult, "has_part");
                //mRDFWriter.write(currentContainer, "container", "ISA");

                result = currentContainer;
            } else {
                String currentType = aList.get(0);
                mUsedBasicTypes.add(currentType);
                super.mRDFWriter.write(currentType, "basic_type", "ISA");
                result = currentType;
                mStoredSubtypes.put(new ArrayList<> (aList), currentType);
            }

        }

        return result;
    }

    private List<String> declarationDecoder(String aType, List<String> aList) {
        //aList.add(",");
        List<String> result = new ArrayList<>();
        String variableType = "___VAR_TYP";
        if (isBasicTypeSequence(aType)) variableType = "basic_variable";
        else if (true) variableType = "container_variable";

        while (aList.size() > 0) {
            //List<String> currentVariable = aList.subList(0, aList.indexOf(','));
            String currentVariable = aList.get(0);
            //aList = aList.subList(aList.indexOf(',') + 1, aList.size());
            do {
                aList.remove(0);
            }
            while (aList.size() > 0 && (aList.get(0).contains("=") ||
                    ('0' <= aList.get(0).codePointAt(0) && aList.get(0).codePointAt(0) <= '9')));


            //TODO: constructor (,,,),
            super.mRDFWriter.write(currentVariable, aType, "has_type");
            super.mRDFWriter.write(currentVariable, variableType, "ISA");
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
        super.mRDFWriter.write(valueName, aList.get(0), "assignment");
        for (int i = 1; i < aList.size(); ++i) {
            String s = aList.get(i);
            for(String ss : mMethodsName)
            {
                if (s.contains(ss))
                {
                    String methodCallName = "method_call_" + mMethodCall++;
                    super.mRDFWriter.write(valueName, methodCallName, "has_part");

                    String variableName = s.substring(0, s.indexOf('.'));
                    super.mRDFWriter.write(methodCallName, variableName, "has_part");

                    String methodName = s.substring(s.indexOf('.') + 1);
                    super.mRDFWriter.write(methodCallName, methodName, "has_part");
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
                super.mRDFWriter.write(valueName, s, "has_part");
            }
        }
        return result;
    }
}