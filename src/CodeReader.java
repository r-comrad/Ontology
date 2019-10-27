import java.util.Objects;

public class CodeReader {

    private MyFileReader mReader;
    OntologyNode mOntology;

    public CodeReader(OntologyNode aOntology, String aFileName) {
        mOntology = aOntology;
        mReader = new MyFileReader(aFileName);

        process();
    }

    public CodeReader(OntologyNode aOntology) {
        mOntology = aOntology;
        mReader = new MyFileReader("rdf code");

        process();
    }

    public void process() {
        String obj, subj, pred;
        while (!Objects.equals(obj = mReader.read(), "") &&
                !Objects.equals(subj = mReader.read(), "")&&
                !Objects.equals(pred = mReader.read(), "")) {
            OntologyNode nodeFrom = mOntology.findNode(obj);
            OntologyNode nodeTo = mOntology.findNode(subj);

            if (nodeFrom == null) nodeFrom = new OntologyNode(obj);
            if (nodeTo == null) nodeTo = new OntologyNode(subj);

            nodeFrom.addConnection(nodeTo, pred);
        }
    }

    public void funkParser() {
       /* OntologyNode parentNode = mOntology.findNode("function");
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
        }*/
    }
}
