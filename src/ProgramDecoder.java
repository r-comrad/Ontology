import java.util.*;

public class ProgramDecoder
{
    private MyFileReader mFileReader;
    DecodersArray mDecoderArray;

    public ProgramDecoder() {
        mFileReader = new MyFileReader("parsed_code.cpp");
        mDecoderArray = new DecodersArray();
    }

    public void process()
    {
        List<String> list = new ArrayList();
        String str;

        while (!Objects.equals(str = mFileReader.read(), ""))
        {
            mDecoderArray.process(str);
        }

        mDecoderArray.finishProcessing();
    }

    public boolean isUnusedSequence(String s)
    {
        return Objects.equals(s, "+")  || Objects.equals(s, "&&") || Objects.equals(s, "||") || Objects.equals(s, "!=") || Objects.equals(s, "==");
    }
}