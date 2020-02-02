import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Command
{
    enum Type {
        NUN, FUNCTION, VARIABLE, CONDITION, CYCLE, ENDLINE, BRACKET;
    }

    private Type mType;
    //private int mTypeConditions;
    private Set<Type> mTypeConditions;

    public Command()
    {
        reset();
    }

    public void reset()
    {
        //mTypeConditions = 0;
        mTypeConditions = new HashSet<>();
        mType = Type.NUN;
    }

    public void addCondition(Type aType, Boolean aFlag)
    {
        //mTypeConditions.put(aType, aFlag);
        if (aFlag) mTypeConditions.add(aType);
    }

    public Type getType()
    {
        if (mTypeConditions.contains(Type.FUNCTION) &&
                mTypeConditions.contains(Type.VARIABLE) &&
                mTypeConditions.contains(Type.BRACKET)) return Type.FUNCTION;

        else if (mTypeConditions.contains(Type.CYCLE)) return Type.CYCLE;

        else if (mTypeConditions.contains(Type.VARIABLE)) return Type.VARIABLE;

        else if (mTypeConditions.contains(Type.CONDITION)) return Type.CONDITION;

        else return Type.NUN;
    }
}
