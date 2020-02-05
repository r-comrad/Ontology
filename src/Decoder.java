import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Decoder {
    public abstract List<String> process(List<String> aList, int aLevel);
    public abstract boolean checkSequence(String aStr);
    public abstract void writePack();
    public abstract CommandManager.Type getType();
    public void close(){}

    protected boolean isFunctionCallSequence(String s)
    {
        return Objects.equals(s, "(") || Objects.equals(s, ")");
    }

    protected boolean isFunctionCallOpenerSequence(String s)
    {
        return Objects.equals(s, "(");
    }

    protected boolean isFunctionCallCloserSequence(String s)
    {
        return Objects.equals(s, ")");
    }

    protected void clearFunctionCall(List<String> aList) {
        aList.remove("(");
        aList.remove(")");
    }
}
