import javax.swing.*;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ProgramPanel extends JPanel {
    private OntologyTree mOntology;
    //private CommandTreeNode mCommandTree;

    public ProgramPanel() {
        CodeParser parser = new CodeParser();
        parser.process();
        ProgramDecoder programDecoder = new ProgramDecoder();
        programDecoder.process();

        mOntology = new OntologyTree();
        //mCommandTree = new CommandTreeNode();

        //ontologyReader();
        //commandTreeReader();
        codeParser();

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        //mOntology.drawCenter(graph, parent, mOntology, 100, 100);
        mOntology.draw(graph, parent);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);

        mOntology.rdfMaker();
    }

    public void codeParser() {
        /*MyFileReader fileReader = new MyFileReader("test file2.txt");
        String command;
        while (!Objects.equals(command = fileReader.read(), "")) {
            if (command.endsWith(";")) command = command.substring(0, command.length() - 1);
            String parent = mCommandTree.getParent(command);
            if (!Objects.equals(parent, "")) {
                OntologyTree node = mOntology.findNode(parent);
                OntologyTree newNode = new OntologyTree(command);
                node.addConnection(newNode, "include");
            }
        }*/


        CodeReader code = new CodeReader(mOntology, "code_ontology.myRDF");
    }

    /*public void ontologyReader() {
        MyFileReader fileReader = new MyFileReader("ontology.txt");
        int count = Integer.parseInt(fileReader.read());
        for (int i = 0; i < count; ++i) {
            String parent = fileReader.read();
            String concept = fileReader.read();
            String connection = fileReader.read();

            OntologyTree nodeFrom = mOntology.findNode(parent);
            OntologyTree nodeTo = mOntology.findNode(concept);

            if (nodeFrom == null) nodeFrom = new OntologyTree(parent);
            if (nodeTo == null) nodeTo = new OntologyTree(concept);

            nodeFrom.addConnection(nodeTo, connection);
        }
    }*/

    /*public void commandTreeReader() {
        MyFileReader fileReader = new MyFileReader("commands list.txt");
        int count = Integer.parseInt(fileReader.read());
        for (int i = 0; i < count; ++i) {
            String command = fileReader.read();
            String parent = fileReader.read();
            mCommandTree.add(command, parent);
        }
    }*/
}