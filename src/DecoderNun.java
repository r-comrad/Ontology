import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DecoderNun extends Decoder
{
    public DecoderNun(DecodersArray aDecodersArray, RDFWriter aRDFWriter) {
        super(aDecodersArray, aRDFWriter);
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
        return Objects.equals(aStr, "");
    }

    @Override
    public CommandManager.Type getType()
    {
        return CommandManager.Type.NUN;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void writePack()
    {

    }
}
