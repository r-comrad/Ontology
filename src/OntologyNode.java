import com.mxgraph.view.mxGraph;

import java.util.*;

public class OntologyNode {
    private String mСonceptName;
    private Map<String, OntologyNode> mConnections;
    boolean isDrawed = false;

    public OntologyNode(String aСonceptName) {
        mСonceptName = aСonceptName;
        mConnections = new HashMap<String, OntologyNode>();
    }

    public void addConnection(ArrayList<String> aPath, OntologyNode aNode) {
        OntologyNode node = getNode(aPath);
        node.mConnections.put(aNode.getCommandName(), aNode);
    }

    public void addConnection(OntologyNode aNode) {
        mConnections.put(aNode.getCommandName(), aNode);
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

    public OntologyNode findNode(String aConceptName) {
        OntologyNode result = null;
        //aConceptName.

        Queue<OntologyNode> queue = new LinkedList<>();
        queue.offer(this);
        while (!queue.isEmpty()) {
            OntologyNode node = queue.remove();
            if (Objects.equals(node.mСonceptName, aConceptName)) {
                result = node;
                break;
            }
            for (Map.Entry<String, OntologyNode> entry : node.mConnections.entrySet()) {
                queue.offer(entry.getValue());
            }
        }

        return result;
    }

    public void draw(mxGraph aGraph, Object aParent, Object aStartVertex, CircleManager aCircle) {
        if (!isDrawed) {
            isDrawed = true;

            Point center = aCircle.getCenter();
            Object vertex = aGraph.insertVertex(aParent, null, mСonceptName,
                    center.getX(), center.getY(), 40, 20);
            if (aStartVertex != null) {
                aGraph.insertEdge(aParent, null, "-", aStartVertex, vertex);
            }

            aCircle.recalculateAngleChange(mConnections.size());
            for (Map.Entry<String, OntologyNode> entry : mConnections.entrySet()) {
                entry.getValue().draw(aGraph, aParent, vertex, new CircleManager(aCircle));
                aCircle.rotatePoint();
            }
        }
    }

    private String getCommandName() {
        return mСonceptName;
    }
}