import com.mxgraph.view.mxGraph;

import java.util.*;

public class OntologyNode {
    private String mСonceptName;
    private Map<String, OntologyNode> mConnections;
    boolean isDrawed = false;

    public void addConnection(ArrayList<String> aPath, OntologyNode aNode) {
        OntologyNode node = getNode(aPath);
        node.mConnections.put(aNode.getCommandName(), aNode);
    }

    public void addConnection(OntologyNode aNode) {
        mConnections.put(aNode.getCommandName(), aNode);
    }

    public OntologyNode(String aСonceptName) {
        mСonceptName = aСonceptName;
        mConnections = new HashMap<String, OntologyNode>();
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

    public OntologyNode findNode(String aСonceptName) {
        Queue<OntologyNode> queue = new LinkedList<>();
        queue.offer(this);

        OntologyNode result = null;
        while (!queue.isEmpty()) {
            OntologyNode node = queue.remove();
            if (Objects.equals(node.mСonceptName, aСonceptName)) {
                result = node;
                break;
            }
            for (Map.Entry<String, OntologyNode> entry : node.mConnections.entrySet()) {
                queue.offer(entry.getValue());
            }
        }
        return result;
    }

    public void draw(mxGraph mGraph, Object aParent, Object aStartVertex) {
        if (!isDrawed) {
            isDrawed = true;
            Object vertex = mGraph.insertVertex(aParent, null, mСonceptName,
                    Math.random() * 200, Math.random() * 200, 40, 20);
            if (mСonceptName != "start") {
                mGraph.insertEdge(aParent, null, "-", aStartVertex, vertex);
            }

            for (Map.Entry<String, OntologyNode> entry : mConnections.entrySet()) {
                entry.getValue().draw(mGraph, aParent, vertex);
            }
        }
    }

    private String getCommandName() {
        return mСonceptName;
    }
}