import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ProgramPanel extends JPanel {
    private OntologyNode mOntology;
    private CommandTreeNode mCommandTree;

    public String fileReader(BufferedReader aReader) {
        String result = "";
        while (Objects.equals(result, "")) {
            try {
                int c;
                while (!Character.isWhitespace((char) (c = aReader.read()))) { // TODO: \f \r
                    if (c == -1) return result;
                    //for(Character obj : aBreakChars)
                    result += (char) c;
                }
            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }
        }
        return result;
    }

    public void codeParser() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "test file.txt"), StandardCharsets.UTF_8))) {
            //int count = Integer.parseInt(fileReader(reader));
            //for (int i = 0; i < count; ++i) {
            String command;
            while (!Objects.equals(command = fileReader(reader), "")) {
                //String command = fileReader(reader);

                if (command.endsWith(";")) command = command.substring(0, command.length() - 1);
                String parent = mCommandTree.getParent(command.toCharArray());
                if (parent != "") {
                    OntologyNode node = mOntology.findNode(parent);
                    OntologyNode newNode = new OntologyNode(command);
                    node.addConnection(newNode);
                }
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public void ontologyReader() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "ontology.txt"), StandardCharsets.UTF_8))) {
            int count = Integer.parseInt(fileReader(reader));
            for (int i = 0; i < count; ++i) {
                String parent = fileReader(reader);
                String concept = fileReader(reader);

                OntologyNode node = mOntology.findNode(parent);
                OntologyNode newNode = new OntologyNode(concept);
                node.addConnection(newNode);
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public void commandTreeReader() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "commands list.txt"), StandardCharsets.UTF_8))) {
            int count = Integer.parseInt(fileReader(reader));
            for (int i = 0; i < count; ++i) {

                String command = fileReader(reader);
                String parent = fileReader(reader);

                mCommandTree.add(command.toCharArray(), parent);
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public ProgramPanel() {
        mOntology = new OntologyNode("start");
        mCommandTree = new CommandTreeNode();

        /*try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "onyology.txt"), StandardCharsets.UTF_8))) {*/
            /*String command;
            while ((command = reader.readLine()) != null) {
                System.out.println(command);
                tree.add(command.toCharArray(), 0);

            }*/

            /*for (int i = 0; i < 13; ++i) {
                String vertix1 = reader.readLine(), vertix2 = reader.readLine();
                OntologyNode node = ontology.findNode(vertix1);
                OntologyNode newNode = new OntologyNode(vertix2);
                node.addConnection(newNode);
            }*/

        /*} catch (IOException e) {
            // log error
        }*/

        ontologyReader();
        commandTreeReader();
        codeParser();

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        mOntology.drawCenter(graph, parent, mOntology, 100, 100);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
    }
}