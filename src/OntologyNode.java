import com.mxgraph.view.mxGraph;

import java.util.*;

public class OntologyNode {
    private String mConceptName;
    private Map<String, Pair<String, OntologyNode>> mConnections;
    boolean isDrawed = false;

    public OntologyNode(String aConceptName) {
        mConceptName = aConceptName;
        mConnections = new HashMap<>();
    }

    /*public void addConnection(ArrayList<String> aPath, OntologyNode aNode) {
        OntologyNode node = getNode(aPath);
        node.mConnections.put(aNode.getCommandName(), aNode);
    }*/

    public void addConnection(OntologyNode aNode, String aConnectionName) {
        mConnections.put(aNode.getCommandName(), new Pair(aConnectionName, aNode));
    }

    /*public OntologyNode getNode(ArrayList<String> aPath) {
        if (aPath.isEmpty()) {
            return this;
        } else {
            OntologyNode node = mConnections.get(aPath.get(0));
            aPath.remove(0);
            return node.getNode(aPath);
        }
    }*/

    public OntologyNode findNode(String aConceptName) {
        OntologyNode result = null;
        //aConceptName.

        Queue<OntologyNode> queue = new LinkedList<>();
        queue.offer(this);
        while (!queue.isEmpty()) {
            OntologyNode node = queue.remove();
            if (Objects.equals(node.mConceptName, aConceptName)) {
                result = node;
                break;
            }
            for (Map.Entry<String, Pair<String, OntologyNode>> entry : node.mConnections.entrySet()) {
                queue.offer(entry.getValue().getSecond());
            }
        }

        return result;
    }

    public void draw(mxGraph aGraph, Object aParent, Object aStartVertex, CircleManager aCircle, String aConnectionName) {
        if (!isDrawed) {
            isDrawed = true;

            Pair<Double, Double> center = aCircle.getCenter();
            Object vertex = aGraph.insertVertex(aParent, null, mConceptName,
                    center.getX(), center.getY(), 40, 20);
            if (aStartVertex != null) {
                aGraph.insertEdge(aParent, null, aConnectionName, aStartVertex, vertex);
            }

            aCircle.recalculateAngleChange(mConnections.size());
            for (Map.Entry<String, Pair<String, OntologyNode>> entry : mConnections.entrySet()) {
                String connectionName = entry.getValue().getFirst();
                OntologyNode drawTarget = entry.getValue().getSecond();
                drawTarget.draw(aGraph, aParent, vertex, new CircleManager(aCircle), connectionName);
                aCircle.rotatePoint();
            }
        }
    }

    private String getCommandName() {
        return mConceptName;
    }
}