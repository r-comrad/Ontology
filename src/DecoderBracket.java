import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DecoderBracket extends Decoder
{
    public DecoderBracket()
    {
    }

    @Override
    public List<String> process(List<String> aList, int aLevel)
    {
        List<String> result = new ArrayList<>();
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean checkSequence(String aStr)
    {
        return Objects.equals(aStr, "{") || Objects.equals(aStr, "}");
    }

    @Override
    public CommandManager.Type getType()
    {
        return CommandManager.Type.BRACKET;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void writePack()
    {

    }
}