import java.util.Objects;

public class CodeReader {

    private MyFileReader mReader;
    OntologyTree mOntology;

    public CodeReader(OntologyTree aOntology, String aFileName) {
        mOntology = aOntology;
        mReader = new MyFileReader(aFileName);

        process();
    }

    public CodeReader(OntologyTree aOntology) {
        mOntology = aOntology;
        mReader = new MyFileReader("rdf code");

        process();
    }

    public void process() {


        String obj, subj, pred;
        while (!Objects.equals(obj = mReader.read(), "") &&
                !Objects.equals(subj = mReader.read(), "")&&
                !Objects.equals(pred = mReader.read(), "")) {
            //OntologyTree nodeFrom = mOntology.findNode(obj);
            //OntologyTree nodeTo = mOntology.findNode(subj);

            //if (nodeFrom == null) nodeFrom = new OntologyTree(obj);
            //if (nodeTo == null) nodeTo = new OntologyTree(subj);

            //nodeFrom.addConnection(nodeTo, pred);
            mOntology.addConnection(obj, pred, subj);
        }
    }

    public void funkParser() {
       /* OntologyTree parentNode = mOntology.findNode("function");
        String funkName = mReader.read();
        OntologyTree funkNode = new OntologyTree(funkName);
        parentNode.addConnection(funkNode, "AKA");

        String returnType = mReader.read();
        OntologyTree returnTypeNode = mOntology.findNode(returnType);
        funkNode.addConnection(returnTypeNode, "return");

        int argsCount = Integer.parseInt(mReader.read());
        for (int i = 0; i < argsCount; ++i) {
            String argType = mReader.read();
            OntologyTree argTypeNode = mOntology.findNode(argType);
            funkNode.addConnection(argTypeNode, "take");
        }*/
    }
}
