import java.util.List;

public abstract class Decoder {
    enum Type {
        NUN, FUNKTION, VARIABLE, CONDITION;
    }

    public abstract List<String> process(List<String> aList);
    public abstract boolean checkSequence(String aStr);
    public abstract void writePack();
    public abstract Type getType();
    public void close(){}
}
