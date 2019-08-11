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

    public OntologyNode findNode(String aСonceptName) {
        OntologyNode result = null;

        Queue<OntologyNode> queue = new LinkedList<>();
        queue.offer(this);
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

    public void draw(mxGraph aGraph, Object aParent, Object aStartVertex, double aX, double aY, Angle aAngle) {
        if (!isDrawed) {
            isDrawed = true;
            Object vertex = aGraph.insertVertex(aParent, null, mСonceptName,
                    aX, aY, 40, 20);

            aGraph.insertEdge(aParent, null, "-", aStartVertex, vertex);

            double r = 50;
            Angle dAngle = new Angle(mConnections.size() == 1 ? 0 : 180. / (mConnections.size() - 1));
            aAngle.add(-90);
            double x = (0) * aAngle.getCos() - (- r) * aAngle.getSin() + aX;
            double y = (0) * aAngle.getSin() + (- r) * aAngle.getCos() + aY;

            for (Map.Entry<String, OntologyNode> entry : mConnections.entrySet()) {
                try {
                    entry.getValue().draw(aGraph, aParent, vertex, x, y, aAngle.clone());
                } catch (CloneNotSupportedException e) {
                    System.out.println("Объект не может быть клонированным.");
                }

                //aAngle.add(dAngle);
                double newX = (x - aX) * dAngle.getCos() - (y - aY) * dAngle.getSin() + aX;
                double newY = (x - aX) * dAngle.getSin() + (y - aY) * dAngle.getCos() + aY;


                x = newX;
                y = newY;
            }
        }
    }

    public void drawCenter(mxGraph aGraph, Object aParent, Object aStartVertex, double aX, double aY) {
        isDrawed = true;
        Object vertex = aGraph.insertVertex(aParent, null, mСonceptName,
                aX, aY, 40, 20);

        double r = 50;
        double dAngle = 360 / mConnections.size();
        double x = aX;
        double y = aY - r;
        Angle angle = new Angle(0);

        for (Map.Entry<String, OntologyNode> entry : mConnections.entrySet()) {
            try {
            entry.getValue().draw(aGraph, aParent, vertex, x, y, angle.clone());
            } catch (CloneNotSupportedException e) {
                System.out.println("Объект не может быть клонированным.");
            }
            //entry.getValue().draw(aGraph, aParent, vertex, x, y, angle);

            angle.add(dAngle);
            double newX = (x - aX) * angle.getCos() - (y - aY) * angle.getSin() + aX;
            double newY = (x - aX) * angle.getSin() + (y - aY) * angle.getCos() + aY;


            x = newX;
            y = newY;
        }
     }

    private String getCommandName() {
        return mСonceptName;
    }
}