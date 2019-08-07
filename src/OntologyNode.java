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

    public void draw(mxGraph aGraph, Object aParent, Object aStartVertex, double aX, double aY, double aK) {
        if (!isDrawed) {
            isDrawed = true;
            Object vertex = aGraph.insertVertex(aParent, null, mСonceptName,
                    aX, aY, 40, 20);

            aGraph.insertEdge(aParent, null, "-", aStartVertex, vertex);

            double r = 50;
            double angel = 180. / mConnections.size();
            double x = Math.sqrt(r * r / (aK * aK + 1)) + aX;
            double y = aK * x;

            double radAngle = angel / 180 * 3.14;
            double newX = (x - aX) * Math.cos(1.57) - (y - aY) * Math.sin(1.57)+ aX;
            double newY = (x - aX) * Math.sin(1.57) + (y - aY) * Math.cos(1.57)+ aY;

            for (Map.Entry<String, OntologyNode> entry : mConnections.entrySet()) {
                double newK = (newX - aX) / (newY - aY);
                draw(aGraph, aParent, vertex, newX, newY, newK);
                x = newX;
                y = newY;
                newX = (x - aX) * Math.cos(radAngle) - (y - aY) * Math.sin(radAngle)+ aX;
                newY = (x - aX) * Math.sin(radAngle) + (y - aY) * Math.cos(radAngle)+ aY;
            }
        }
    }

    public void drawCenter(mxGraph aGraph, Object aParent, Object aStartVertex, double aX, double aY) {
        isDrawed = true;
        Object vertex = aGraph.insertVertex(aParent, null, mСonceptName,
                aX, aY, 40, 20);

        double r = 50;
        double angel = 360 / mConnections.size();
        double x = aX;
        double y = aY - r;

        double radAngle = angel / 180 * 3.14;

        for (Map.Entry<String, OntologyNode> entry : mConnections.entrySet()) {
            double k = (x - aX) / (y - aY);
            entry.getValue().draw(aGraph, aParent, vertex, x, y, k);

            double newX = (x - aX) * Math.cos(radAngle) - (y - aY) * Math.sin(radAngle) + aX;
            double newY = (x - aX) * Math.sin(radAngle) + (y - aY) * Math.cos(radAngle) + aY;
            x = newX;
            y = newY;
        }
    }

    private String getCommandName() {
        return mСonceptName;
    }
}