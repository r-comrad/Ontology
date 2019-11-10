import com.mxgraph.view.mxGraph;

import java.util.*;

public class OntologyTree {
    //private String mConceptName;
    //private Map<String, Pair<String, OntologyTree>> mConnections;
    private Map<String, Set<Pair<String, String>>> mTree; // Node Name - Connection Name

    //public OntologyTree(String aConceptName) {
    public OntologyTree() {
        //mConceptName = aConceptName;
        //mConnections = new HashMap<>();
        mTree = new HashMap<>();
        mTree.put("start", new HashSet<>());
    }

    /*public void addConnection(ArrayList<String> aPath, OntologyTree aNode) {
        OntologyTree node = getNode(aPath);
        node.mConnections.put(aNode.getCommandName(), aNode);
    }*/

    /*public void addConnection(OntologyTree aNode, String aConnectionName) {
        mConnections.put(aNode.getCommandName(), new Pair(aConnectionName, aNode));
        aNode.mConnections.put(getCommandName(), new Pair("-" + aConnectionName, this));
    }*/


    public void addConnection(String aFromNodeName, String aConnectionName, String aToNodeName) {
        if (!mTree.containsKey(aFromNodeName)) mTree.put(aFromNodeName, new HashSet<>());
        if (!mTree.containsKey(aToNodeName)) mTree.put(aToNodeName, new HashSet<>());

        Set <Pair<String, String>> nodeFrom = mTree.get(aFromNodeName);
        Set <Pair<String, String>> nodeTo = mTree.get(aToNodeName);

        nodeFrom.add(new Pair(aToNodeName, aConnectionName));
        nodeTo.add(new Pair(aFromNodeName, "-" + aConnectionName));
    }

    /*public OntologyTree getNode(ArrayList<String> aPath) {
        if (aPath.isEmpty()) {
            return this;
        } else {
            OntologyTree node = mConnections.get(aPath.get(0));
            aPath.remove(0);
            return node.getNode(aPath);
        }
    }*/

    /*public OntologyTree findNode(String aConceptName) {
        OntologyTree result = null;
        //aConceptName.

        Queue<OntologyTree> queue = new LinkedList<>();
        queue.offer(this);
        Set<String> painted = new HashSet<>();

        while (!queue.isEmpty()) {
            OntologyTree node = queue.remove();
            painted.add(node.getCommandName());
            if (Objects.equals(node.mConceptName, aConceptName)) {
                result = node;
                break;
            }
            for (Map.Entry<String, Pair<String, OntologyTree>> entry : node.mConnections.entrySet()) {
                OntologyTree nextNode = entry.getValue().getSecond();
                if (!painted.contains(nextNode.getCommandName())) {
                    queue.offer(nextNode);
                    //painted.add(nextNode.getCommandName());
                }

            }
        }

        return result;
    }*/

    public void draw(mxGraph aGraph, Object aParent) {
        Map<String, Object> drawedVertex = new HashMap<>();

        int size = 7;
        int i = 0, j = 0;
        int x = 150, y = 300;
        //for(int i = 0; i < mTree.size() / size; ++i)
        for (Map.Entry<String, Set<Pair<String, String>>> entry : mTree.entrySet()) {
            //for(int j = 0; j < size; ++j)
            //{
            String nodeName = entry.getKey();
            Object vertex = aGraph.insertVertex(aParent, null, nodeName,
                    i * x, j * y, 40, 20);
            drawedVertex.put(nodeName, vertex);
            //}

            ++i;
            if (i >= size)
            {
                i = 0;
                ++j;
            }
        }

        Queue<String> nodeQueue = new LinkedList<>();
        nodeQueue.offer("start");
        Set<String> painted = new HashSet<>();
        while (!nodeQueue.isEmpty()) {
            String nodeName = nodeQueue.remove();
            if (painted.contains(nodeName)) continue;
            painted.add(nodeName);

            Set<Pair<String, String>> currentNode = mTree.get(nodeName);
            for(Pair<String, String> p : currentNode) {
                String neighborNodeName = p.getFirst();
                String connectionName = p.getSecond();

                if (!connectionName.startsWith("-")) {
                    Object startVertix = drawedVertex.get(nodeName);
                    Object endVertix = drawedVertex.get(neighborNodeName);
                    aGraph.insertEdge(aParent, null, connectionName, startVertix, endVertix);
                }
                if (!painted.contains(neighborNodeName)) {
                    nodeQueue.offer(neighborNodeName);
                }
            }
        }
    }

    public void rdfMaker()
    {

    }
/*
    public void draw(mxGraph aGraph, Object aParent, double aX, double aY) {
        Set<String> painted = new HashSet<>();
        Map<String, Object> drawedVertex = new HashMap<>();
        //draw(aGraph, aParent, null, new CircleManager(aX, aY), "", painted);

        Queue<OntologyTree> nodeQueue = new LinkedList<>();
        nodeQueue.offer(this);
        Queue<Object> vertexQueue = new LinkedList<>();
        vertexQueue.offer(null);
        Queue<CircleManager> circleQueue = new LinkedList<>();
        circleQueue.offer(new CircleManager(aX, aY));
        Queue<String> connectionNameQueue = new LinkedList<>();
        connectionNameQueue.offer("");



        Map<String, Map<String, String>> table;
        Map<String, String> start;
        table = new LinkedHashMap <>();
        start = new LinkedHashMap <>();

        start.put("ISA", "nun");
        start.put("AKO", "nun");
        start.put("has_part", "nun");
        start.put("has_type", "nun");
        start.put("assignment", "nun");
        start.put("read", "nun");       //?
        start.put("write", "nun");      //?
        start.put("stores", "nun");
        start.put("use", "nun");
        start.put("take", "nun");
        start.put("return", "nun");
        start.put("implement", "nun");

        table.put("start", start);

        List<String> list = new ArrayList();
        list.add("ISA");
        list.add("AKO");
        list.add("has_part");
        list.add("has_type");
        list.add("assignment");
        list.add("read");       //?
        list.add("write");      //?
        list.add("stores");
        list.add("use");
        list.add("take");
        list.add("return");
        list.add("implement");


        while (!nodeQueue.isEmpty()) {
            OntologyTree node = nodeQueue.remove();
            Object previousVertex = vertexQueue.remove();
            CircleManager circle = circleQueue.remove();
            String connectionName = connectionNameQueue.remove();

            //if (painted.contains(node.getCommandName())) {continue;}

            painted.add(node.getCommandName());
            Pair<Double, Double> center = circle.getCenter();

            Object vertex;
            if (drawedVertex.containsKey(node.mConceptName)) {
                vertex = drawedVertex.get(node.mConceptName);
            } else {
                vertex = aGraph.insertVertex(aParent, null, node.mConceptName,
                        center.getX(), center.getY(), 40, 20);
                drawedVertex.put(node.mConceptName, vertex);
            }

            if (previousVertex != null) {
                if (!connectionName.startsWith("-")) {
                    aGraph.insertEdge(aParent, null, connectionName, previousVertex, vertex);
                } else {
                    aGraph.insertEdge(aParent, null, connectionName.substring(1), vertex, previousVertex);
                }
            }

            int countConnections = 0;
            for (Map.Entry<String, Pair<String, OntologyTree>> entry : node.mConnections.entrySet()) {
                OntologyTree drawTarget = entry.getValue().getSecond();

                if (!painted.contains(drawTarget.getCommandName())) {
                    ++countConnections;
                }
            }
            circle.recalculateAngleChange(countConnections);

            for (Map.Entry<String, Pair<String, OntologyTree>> entry : node.mConnections.entrySet()) {
                String nextConnectionName = entry.getValue().getFirst();
                OntologyTree drawTarget = entry.getValue().getSecond();
                if (!painted.contains(drawTarget.getCommandName())) {
                    //drawTarget.draw(aGraph, aParent, vertex, new CircleManager(aCircle), connectionName, aPainted);
                    nodeQueue.offer(drawTarget);
                    vertexQueue.offer(vertex);
                    CircleManager cr = new CircleManager(circle);
                    circleQueue.offer(cr);
                    connectionNameQueue.offer(nextConnectionName);

                    circle.rotatePoint();
                }


                String obj = drawTarget.getCommandName(), subj = node.getCommandName(), pred = nextConnectionName;
                if (!pred.startsWith("-")) continue;
                pred = pred.substring(1);

                Map<String, String> new_val = table.get(obj), parent = table.get(subj);
                if (new_val == null ) new_val = new LinkedHashMap <>(parent);
                else
                {
                    int num = list.indexOf(pred);

                    for (int i = num + 1; i < list.size(); ++i) {
                        String conName = list.get(i);
                        new_val.put(conName, parent.get(conName));
                    }
                }
                new_val.put(pred, subj);
                table.put(obj, new_val);
            }
        }



        MyFileWriter ans = new MyFileWriter("ans.txt");
        ans.write("name;" );
        for (Map.Entry<String, String> j : table.get("start").entrySet()) {
            ans.write(j.getKey() + ";" );
        }
        ans.write("\n" );

        for (Map.Entry<String, Map<String, String>> i : table.entrySet()) {
            ans.write(i.getKey() + ";" );
            for (Map.Entry<String, String> j : i.getValue().entrySet()) {
                ans.write(j.getValue() + ";" );
            }
            ans.write("\n" );
        }
        ans.close();
    }
*/
  /*  private void draw(mxGraph aGraph, Object aParent, Object aStartVertex, CircleManager aCircle,
                      String aConnectionName, Set<String> aPainted) {
        aPainted.add(getCommandName());
        Pair<Double, Double> center = aCircle.getCenter();
        Object vertex = aGraph.insertVertex(aParent, null, mConceptName,
                center.getX(), center.getY(), 40, 20);
        if (aStartVertex != null) {
            if (!aConnectionName.startsWith("-")) {
                aGraph.insertEdge(aParent, null, aConnectionName, aStartVertex, vertex);
            } else {
                aGraph.insertEdge(aParent, null, aConnectionName.substring(1), vertex, aStartVertex);
            }
        }

        int countConnections = 0;
        for (Map.Entry<String, Pair<String, OntologyTree>> entry : mConnections.entrySet()) {
            OntologyTree drawTarget = entry.getValue().getSecond();
            if (!aPainted.contains(drawTarget.getCommandName())) {
                ++countConnections;
            }
        }
        aCircle.recalculateAngleChange(countConnections);

        for (Map.Entry<String, Pair<String, OntologyTree>> entry : mConnections.entrySet()) {
            String connectionName = entry.getValue().getFirst();
            OntologyTree drawTarget = entry.getValue().getSecond();
            if (!aPainted.contains(drawTarget.getCommandName())) {
                drawTarget.draw(aGraph, aParent, vertex, new CircleManager(aCircle), connectionName, aPainted);
                aCircle.rotatePoint();
            }
        }
    }
*/
    /*private String getCommandName() {
        return mConceptName;
    }*/
}