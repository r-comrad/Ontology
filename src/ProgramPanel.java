import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ProgramPanel extends JPanel {
    public ProgramPanel() {
        OntologyNode ontology = new OntologyNode("start");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "onyology.txt"), StandardCharsets.UTF_8))) {
            /*String command;
            while ((command = reader.readLine()) != null) {
                System.out.println(command);
                tree.add(command.toCharArray(), 0);

            }*/

            for (int i = 0; i < 13; ++i) {
                String vertix1 = reader.readLine(), vertix2 = reader.readLine();
                OntologyNode node = ontology.findNode(vertix1);
                OntologyNode newNode = new OntologyNode(vertix2);
                node.addConnection(newNode);
            }

        } catch (IOException e) {
            // log error
        }

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        ontology.drawCenter(graph, parent, ontology, 100, 100);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
    }
}