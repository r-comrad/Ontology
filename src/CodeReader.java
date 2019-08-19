import java.util.Objects;

public class CodeReader {

    private MyFileReader mReader;
    OntologyNode mOntology;

    public CodeReader(OntologyNode aOntology, String aFileName) {
        mOntology = aOntology;
        mReader = new MyFileReader(aFileName);
        process();
    }

    /*public void setFile(String aFileName)
    {
        mReader = new MyFileReader(aFileName);
    }*/

    public void process() {
        String str;
        while (!Objects.equals(str = mReader.read(), "")) {
            if (Objects.equals(str, "FUNK")) {
                funkParser();
            }
        }
    }

    public void funkParser() {
        OntologyNode parentNode = mOntology.findNode("function");
        String funkName = mReader.read();
        OntologyNode funkNode = new OntologyNode(funkName);
        parentNode.addConnection(funkNode, "AKA");

        String returnType = mReader.read();
        OntologyNode returnTypeNode = mOntology.findNode(returnType);
        funkNode.addConnection(returnTypeNode, "return");

        int argsCount = Integer.parseInt(mReader.read());
        for (int i = 0; i < argsCount; ++i) {
            String argType = mReader.read();
            OntologyNode argTypeNode = mOntology.findNode(argType);
            funkNode.addConnection(argTypeNode, "take");
        }
    }
}
