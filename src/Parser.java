import java.util.Objects;

public class Parser {

    private MyFileReader mReader;
    OntologyNode mOntology;

    public Parser()
    {

    }

    public void setFile(String aFileName)
    {
        mReader = new MyFileReader(aFileName);
    }

    public void funkParser(OntologyNode aParent)
    {
        String str;

        str = mReader.read();
        OntologyNode node = mOntology.findNode("function");
        OntologyNode newNode = new OntologyNode(str);
       // node.addConnection(newNode);
    }
}
