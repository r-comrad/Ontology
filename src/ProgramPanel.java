import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ProgramPanel extends JPanel {
    public ProgramPanel() {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        Object v1;
        graph.getModel().beginUpdate();
        try
        {
            v1 = graph.insertVertex(parent, null, "Habrahhhhhhhhhhhhhhhhhhhhhh", 20, 20, 80, 30);
            Object v2 = graph.insertVertex(parent, null, "Habr", 240, 150, 80, 30);
            graph.insertEdge(parent, null, "Дуга", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        Object v3 = graph.insertVertex(parent, null, "Ha", 240, 20, 80, 30);
        graph.insertEdge(parent, null, "Дуга", v1, v3);

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
        

        String s = new String("hjhh");
        String ss = s;
        ss += "11";
        System.out.println(s);
        System.out.println(ss);


        CommandTreeNode tree = new CommandTreeNode();
        CommandTreeNode tree2 = tree;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "commands list.txt"), StandardCharsets.UTF_8))){
            String command;
            while ((command = reader.readLine()) != null) {
                System.out.println(command);
                tree.add(command.toCharArray(), 0);
            }
        } catch (IOException e) {
            // log error
        }
    }
}
