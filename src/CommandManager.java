import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class CommandManager
{
    enum Type
    {
        NUN, FUNCTION, VARIABLE, CONDITION, CYCLE, END_LINE, BRACKET, INSIDE;
    }

    private Set<Type> mTypeConditions;

    public CommandManager()
    {
        reset();
    }

    public void reset()
    {
        mTypeConditions = new HashSet<>();
    }

    public void addCondition(Type aType)
    {
        mTypeConditions.add(aType);
    }

    public void setInsideFunction()
    {
        mTypeConditions.add(Type.INSIDE);
    }

    public Type getType()
    {
        if (mTypeConditions.contains(Type.CYCLE)) return Type.CYCLE;

        else if (mTypeConditions.contains(Type.CONDITION)) return Type.CONDITION;

        else if (mTypeConditions.contains(Type.FUNCTION) &&
                mTypeConditions.contains(Type.VARIABLE) &&
                mTypeConditions.contains(Type.BRACKET) &&
                !mTypeConditions.contains(Type.END_LINE))    // singal importatnt
            return Type.FUNCTION;

        else if (mTypeConditions.contains(Type.VARIABLE) &&
                mTypeConditions.contains(Type.END_LINE))
            return Type.VARIABLE;

        else if (mTypeConditions.contains(Type.END_LINE)) return Type.END_LINE;
        else if (mTypeConditions.contains(Type.BRACKET)) return Type.BRACKET;
        else return Type.NUN;
    }
}