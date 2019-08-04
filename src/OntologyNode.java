import com.mxgraph.view.mxGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OntologyNode {
    private String mСonceptName;
    private Map<String, OntologyNode> mConnections = new HashMap<String, OntologyNode>();
    //List<OntologyNode>
    boolean isDrawed = false;

    public void addConnection(ArrayList<String> aPath, OntologyNode aNode) {
        OntologyNode node = getNode(aPath);
        node.mConnections.put(aNode.getCommandName(), aNode);
    }

    public OntologyNode getNode(ArrayList<String> aPath) {
        if (aPath.isEmpty()) {
            return this;
        } else {
            OntologyNode node = mConnections.get(aPath.get(0));
            aPath.remove(0);
            return node.getNode(aPath);
        }
    }

    public void draw(mxGraph mGraph, Object aParent, Object aStartVertex)
    {
        if (!isDrawed)
        {
            isDrawed = true;
            Object vertex = mGraph.insertVertex(aParent, null, mСonceptName, 20, 20, 40, 20);
            mGraph.insertEdge(aParent, null, "-", aStartVertex, vertex);
        }
    }

    private String getCommandName() {
        return mСonceptName;
    }
}