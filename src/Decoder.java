import java.util.List;

public abstract class Decoder {
    public abstract List<String> process(List<String> aList, int aLevel);
    public abstract boolean checkSequence(String aStr);
    public abstract void writePack();
    public abstract CommandManager.Type getType();
    public void close(){}
}
