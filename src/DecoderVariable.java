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

    private HashMap<String, Integer> mTypeNamesCounter;

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
    }

    @Override
    public List<String> process(List<String> aList, int aLevel) {
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

    private List<String> basicVariableDeclarationDecoder(List<String> aList) {
        List<String> result = new ArrayList<>();
        mUsedBasicTypes.add(aList.get(0));
        for (int i = 1; i < aList.size(); ++i) {
            String s = aList.get(i);

            if (isBasicAssignmentSequence(s))
            {
                int j = i;
                while (j < aList.size() &&
                        (!Objects.equals(aList.get(j), ",") || !Objects.equals(aList.get(j), ";")))
                {
                    ++j;
                }
                List <String> temp = aList.subList(i - 1, j);
                basicVariableAssignmentDecoder(temp);
                i = j;
            }
            else
            {
                mRDFWriter.write(s, aList.get(0), "has_type");
                mRDFWriter.write(s, "basic_variable", "ISA");
                result.add(s);
            }
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

    //------------------------------------------------------------------------------------------------------------------


    private void dec(List<String> aList)
    {
        int number = 1;
        number = Math.max(number, Math.max(Math.max(aList.lastIndexOf('>'),
                aList.lastIndexOf('*')),
                aList.lastIndexOf('&')));

        List<String> type = aList.subList(0, number);
        List<String> variables = aList.subList(number + 1, aList.size());

        String s = decRec(type);
        declr(s, variables);
    }

    private String declr(String aType, List<String> aList)
    {
        List<String> result = new ArrayList<>();
        while(aList.size() > 0)
        {
            List<String> currentVariable = aList.subList(0, aList.indexOf(','));
            aList  = aList.subList(aList.indexOf(',') + 1, aList.size());

            mRDFWriter.write(s, aList.get(0), "has_type");
            mRDFWriter.write(s, "basic_variable", "ISA");
        }



        for (int i = 1; i < aList.size(); ++i) {
            String s = aList.get(i);

            if (isBasicAssignmentSequence(s))
            {
                int j = i;
                while (j < aList.size() &&
                        (!Objects.equals(aList.get(j), ",") || !Objects.equals(aList.get(j), ";")))
                {
                    ++j;
                }
                List <String> temp = aList.subList(i - 1, j);
                basicVariableAssignmentDecoder(temp);
                i = j;
            }
            else
            {
                mRDFWriter.write(s, aList.get(0), "has_type");
                mRDFWriter.write(s, "basic_variable", "ISA");
                result.add(s);
            }
        }
        return result;
    }

    private String decRec(List<String> aList)
    {
        String result = "___VAR_REC";
        if (aList.contains('<'))
        {
            //TODO has pointer ( * / & )
            String currentType = aList.get(0);
            mUsedContainers.add(currentType);

            String currentContainer = "v_" + currentType + mTypeNamesCounter.get(currentType);
            mTypeNamesCounter.put(currentType,  mTypeNamesCounter.get(currentType) + 1);

            List<String> nextIteration = aList.subList(1, aList.size() - 1);
            String nextResult = decRec(nextIteration);

            mRDFWriter.write(currentContainer, currentType, "has_type");
            mRDFWriter.write(currentContainer, nextResult, "has_part");
            mRDFWriter.write(currentContainer, "container", "ISA");

            result = currentContainer;
        }
        else
        {
            String currentType = aList.get(0);
            mUsedBasicTypes.add(currentType);
            String currentVariable = "v_" + currentType + mTypeNamesCounter.get(currentType);
            mTypeNamesCounter.put(currentType,  mTypeNamesCounter.get(currentType) + 1);

            mRDFWriter.write(currentVariable, currentType, "has_type");
            mRDFWriter.write(currentVariable, "basic_variable", "ISA");

            result = currentVariable;
        }
        return result;
    }

    // TODO: ofset for map
    private List<String> containerDeclarationDecoder(List<String> aList)
    {
        List<String> result = new ArrayList<>();
        mUsedContainers.add(aList.get(0));
        //for (String s : aList) {
        if (Objects.equals(aList.get(0),  aList.get(1)))
        {
            aList.remove(2);
        }
        for (int i = 2; i < aList.size(); ++i)
        {
            String s = aList.get(i);
            if (!isFunctionCallSequence(s))
            {
                result.add(s);
                mRDFWriter.write(s, aList.get(0), "has_type");
                mRDFWriter.write(s, aList.get(1), "stores");
                mRDFWriter.write(s, "container", "ISA");
            }
            else
            {
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
            }
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

        //TODO: conteiner
        /*
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
        }*/
    }
}