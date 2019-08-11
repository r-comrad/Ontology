import javax.swing.*;
import java.util.Objects;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ProgramPanel extends JPanel {
    private OntologyNode mOntology;
    private CommandTreeNode mCommandTree;

    public ProgramPanel() {
        mOntology = new OntologyNode("start");
        mCommandTree = new CommandTreeNode();

        ontologyReader();
        commandTreeReader();
        codeParser();

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        //mOntology.drawCenter(graph, parent, mOntology, 100, 100);
        mOntology.draw(graph, parent, null, new CircleManager(100, 100));
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
    }

    public void codeParser() {
        MyFileReader fileReader = new MyFileReader("D:/projects/Java/Ontology/res/" + "test file.txt");
        String command;
        while (!Objects.equals(command = fileReader.read(), "")) {
            if (command.endsWith(";")) command = command.substring(0, command.length() - 1);
            String parent = mCommandTree.getParent(command);
            if (!Objects.equals(parent, "")) {
                OntologyNode node = mOntology.findNode(parent);
                OntologyNode newNode = new OntologyNode(command);
                node.addConnection(newNode);
            }
        }
    }

    public void ontologyReader() {
        MyFileReader fileReader = new MyFileReader("D:/projects/Java/Ontology/res/" + "ontology.txt");
        int count = Integer.parseInt(fileReader.read());
        for (int i = 0; i < count; ++i) {
            String parent = fileReader.read();
            String concept = fileReader.read();

            OntologyNode node = mOntology.findNode(parent);
            OntologyNode newNode = new OntologyNode(concept);
            node.addConnection(newNode);
        }
    }

    public void commandTreeReader() {
        MyFileReader fileReader = new MyFileReader("D:/projects/Java/Ontology/res/" + "commands list.txt");
        int count = Integer.parseInt(fileReader.read());
        for (int i = 0; i < count; ++i) {
            String command = fileReader.read();
            String parent = fileReader.read();
            mCommandTree.add(command, parent);
        }
    }
}