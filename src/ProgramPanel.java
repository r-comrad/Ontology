import javax.swing.*;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ProgramPanel extends JPanel {
    private OntologyTree mOntology;

    public ProgramPanel() {
        CodeParser parser = new CodeParser();
        parser.process();
        ProgramDecoder programDecoder = new ProgramDecoder();
        programDecoder.process();

        mOntology = new OntologyTree();

        codeParser();

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        mOntology.draw(graph, parent);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);

        mOntology.rdfMaker();
    }

    public void codeParser() {
        CodeReader code = new CodeReader(mOntology, "code_ontology.myRDF");
    }
}