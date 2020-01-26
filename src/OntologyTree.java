import com.mxgraph.view.mxGraph;

import java.util.*;

public class OntologyTree {
    private Map<String, Set<Pair<String, String>>> mTree; // Node Name - Connection Name

    private static String getItself(String itself, String dummy) {
        return itself;
    }

    public OntologyTree() {
        mTree = new LinkedHashMap<>();
        mTree.put("start", new LinkedHashSet<>());
    }

    public void addConnection(String aFromNodeName, String aConnectionName, String aToNodeName) {
        if (!mTree.containsKey(aFromNodeName)) mTree.put(aFromNodeName, new LinkedHashSet<>());
        if (!mTree.containsKey(aToNodeName)) mTree.put(aToNodeName, new LinkedHashSet<>());

        Set<Pair<String, String>> nodeFrom = mTree.get(aFromNodeName);
        Set<Pair<String, String>> nodeTo = mTree.get(aToNodeName);

        nodeFrom.add(new Pair(aToNodeName, aConnectionName));
        nodeTo.add(new Pair(aFromNodeName, "-" + aConnectionName));
    }

    public void draw(mxGraph aGraph, Object aParent) {
        Map<String, Object> drawedVertex = new HashMap<>();

        int size = 7;
        int i = 0, j = 0;
        int x = 150, y = 300;

        for (Map.Entry<String, Set<Pair<String, String>>> entry : mTree.entrySet()) {
            String nodeName = entry.getKey();
            Object vertex = aGraph.insertVertex(aParent, null, nodeName,
                    i * x, j * y, 40, 20);
            drawedVertex.put(nodeName, vertex);

            ++i;
            if (i >= size) {
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
            for (Pair<String, String> p : currentNode) {
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

    public void rdfMaker() {
        Map<String, Map<String, String>> table;
        table = new LinkedHashMap<>();

        List<String> list = new ArrayList();
        list.add("ISA");
        list.add("AKO");
        list.add("has_part");
        list.add("has_type");
        list.add("use");
        list.add("take");
        list.add("assignment");
        list.add("stores");
        list.add("read");       //?
        list.add("write");      //?
        list.add("return");
        list.add("implement");

        List<String> nodes = new ArrayList();

        Queue<String> nodeQueue = new LinkedList<>();
        nodeQueue.offer("start");
        Set<String> painted = new HashSet<>();
        while (!nodeQueue.isEmpty()) {
            String nodeName = nodeQueue.remove();
            if (painted.contains(nodeName)) continue;
            painted.add(nodeName);
            nodes.add(nodeName);

            Map<String, String> start = new LinkedHashMap<>();
            for (String i : list) {
                start.put(i, "nun");
            }
            table.put(nodeName, start);

            for (Pair<String, String> i : mTree.get(nodeName)) {
                String obj = i.getFirst();
                if (!painted.contains(obj)) {
                    nodeQueue.offer(obj);
                }
            }
        }
        for (int k = 0; k < 2; ++k) {
            nodeQueue = new LinkedList<>();
            nodeQueue.offer("start");
            painted = new HashSet<>();
            while (!nodeQueue.isEmpty()) {
                String nodeName = nodeQueue.remove();
                painted.add(nodeName);

                for (Pair<String, String> i : mTree.get(nodeName)) {
                    String obj = i.getFirst(), subj = nodeName, pred = i.getSecond();
                    if (!painted.contains(obj)) {
                        nodeQueue.offer(obj);
                    }

                    if (!pred.startsWith("-")) {
                        continue;
                    } else pred = pred.substring(1);

                    Map<String, String> new_val = table.get(obj), parent = table.get(subj);
                    if (new_val == null) {
                        if (parent == null) new_val = new LinkedHashMap<>(table.get("start"));
                        else new_val = new LinkedHashMap<>(parent);
                    } else {
                        int num = list.indexOf(pred);

                        for (int j = num + 1; j < list.size(); ++j) {
                            String conName = list.get(j);
                            if (!Objects.equals(parent.get(conName), "nun") &&
                                    Objects.equals(new_val.get(conName), "nun"))
                                new_val.put(conName, parent.get(conName));
                        }
                    }
                    new_val.put(pred, subj);
                    table.put(obj, new_val);
                }
            }
        }

        MyFileWriter ans = new MyFileWriter("ans.txt");

        for (String i : nodes) {
            ans.write(i + ";");
        }
        ans.write("\n\n");

        ans.write("name;");

        for (String i : list) {
            ans.write(i + ";");
        }
        ans.write("\n");

        for (String i : nodes) {
            ans.write(i + ";");
            for (String j : list) {
                //String curVal = i.getValue().get(j);
                String curVal = table.get(i).get(j);
                ans.write(curVal + ";");
            }
            ans.write("\n");
        }
        ans.close();
    }
}