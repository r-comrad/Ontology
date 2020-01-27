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
        mReader = new MyFileReader("output_ontology.myRDF");

        process();
    }

    public void process() {
        String obj, subj, pred;
        while (!Objects.equals(obj = mReader.read(), "") &&
                !Objects.equals(subj = mReader.read(), "")&&
                !Objects.equals(pred = mReader.read(), "")) {
            mOntology.addConnection(obj, pred, subj);
        }
    }
}
