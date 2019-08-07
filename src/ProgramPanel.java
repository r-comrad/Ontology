import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ProgramPanel extends JPanel {
    private OntologyNode mOntology;

    public String fileReader(BufferedReader aReader)
    {
        String result = "";
        try
        {
            int c;
            while((c=aReader.read())!=-1 && ! Character.isWhitespace((char)c)){ // TODO: \f \r
                //for(Character obj : aBreakChars)
                result += (char)c;
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        return result;
    }

    public void fileParser()
    {
        try(FileReader reader = new FileReader("aaa.txt"))
        {
            int c;
            while((c=reader.read())!=-1){

                System.out.print((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public void ontologyReader()
    {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "onyology.txt"), StandardCharsets.UTF_8))) {
            int count = Integer.parseInt(fileReader(reader));
            for(int i = 0; i < count; ++i)
            {
                String concept, parent;
                while ((parent = fileReader(reader)) == "");
                while ((concept = fileReader(reader)) == "");

                OntologyNode node = mOntology.findNode(parent);
                OntologyNode newNode = new OntologyNode(concept);
                node.addConnection(newNode);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    public ProgramPanel() {
        mOntology = new OntologyNode("start");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("D:/projects/Java/Ontology/res/" + "onyology.txt"), StandardCharsets.UTF_8))) {
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

        } catch (IOException e) {
            // log error
        }

        ontologyReader();

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        mOntology.drawCenter(graph, parent, mOntology, 100, 100);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        add(graphComponent);
    }
}