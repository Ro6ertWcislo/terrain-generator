import model.*;
import org.graphstream.graph.Node;
import org.junit.Test;
import transformation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Task1Test {
    private Transformation transformation1 = new TransformationP1();
    private Transformation transformation2 = new TransformationP2();
    private Transformation transformation3 = new TransformationP3();
    private Transformation transformation4 = new TransformationP4();
    private Transformation transformation5 = new TransformationP5();
    private Transformation transformation6 = new TransformationP6();
    private Transformation transformation7 = new TransformationP7();
    List<Transformation> transformations =
            Arrays.asList(transformation1, transformation2, transformation3,
                    transformation4, transformation5, transformation6);

    @Test
    public void breakTriangles(){
        ModelGraph graph = generateTestGraph();
        graph.display();
        sleepFor3Seconds();

        fireProductionsWhilePossible(graph);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getXCoordinate() < 50).setPartitionRequired(true);
        fireProductionsWhilePossible(graph);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getYCoordinate() > 50 && i.getYCoordinate() < 75)
                .setPartitionRequired(true);
        fireProductionsWhilePossible(graph);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getYCoordinate() > 50 && i.getYCoordinate() < 73)
                .setPartitionRequired(true);
        fireProductionsWhilePossible(graph);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getXCoordinate() > 25 && i.getYCoordinate() > 50 && i.getYCoordinate() < 73)
                .setPartitionRequired(true);
        fireProductionsWhilePossible(graph);
        sleepFor3Seconds();

    }

    @Test
    public void breakTrianglesAutomatically() throws InterruptedException {
        ModelGraph graph = generateTest2Graph();
        showGraph(graph);
        sleepFor3Seconds();

        fireProductionsWhilePossible(graph);
    }

    private void showGraph(ModelGraph graph) throws InterruptedException {
        // add node labels for better visualization
        for (Node node : graph) node.addAttribute("ui.label", node.getId());
        graph.display();
        TimeUnit.SECONDS.sleep(5);
    }

    private void fireProductionsWhilePossible(ModelGraph graph){
        boolean graphChanged;
        do {
            graphChanged = onePassThroughGraph(graph);
        } while(graphChanged);
    }

    private boolean onePassThroughGraph(ModelGraph graph){
        boolean anyTransactionFired = false;

        Collection<InteriorNode> interiorNodes = new ArrayList<>(graph.getInteriors());
        for (InteriorNode i : interiorNodes) {
            if (!new HashSet<>(graph.getInteriors()).contains(i)) break;
            try {
                if (transformation7.isConditionCompleted(graph, i)) {
                    graph = transformation7.transformGraph(graph, i);
                    anyTransactionFired = true;
                    sleepFor3Seconds();
                }
            } catch(Throwable t){
                System.err.println("Error running transformation " + transformation7.toString() + ": " + t.getMessage());
            }
        }
        for (InteriorNode i : interiorNodes) {
            if (!new HashSet<>(graph.getInteriors()).contains(i)) break;
            for (Transformation transformation : transformations) {
                try {
                    if (transformation.isConditionCompleted(graph, i)) {
                        graph = transformation.transformGraph(graph, i);
                        anyTransactionFired = true;
                        sleepFor3Seconds();
                    }
                } catch(Throwable t){
                    System.err.println("Error running transformation " + transformation.toString() + ": " + t.getMessage());
                }
            }
        }

        return anyTransactionFired;
    }

    private InteriorNode findTriangleToBreak(ModelGraph graph, Predicate<InteriorNode> predicate){
        InteriorNode interior = null;
        for (InteriorNode i: graph.getInteriors()){
            if (predicate.test(i)){
                interior = i;
                break;
            }
        }
        if (interior == null){
            throw new AssertionError("Specified interiors must exist if previous steps of the tests where successful.");
        }
        return interior;
    }

    private void sleepFor3Seconds(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ModelGraph generateTestGraph() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 100.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(100.0, 100.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v4 = graph.insertVertex("v4", VertexType.SIMPLE_NODE, new Point3d(100.0, 0.0, 0.0));

        graph.insertEdge("v1v2", v1, v2, true);
        graph.insertEdge("v1v3", v1, v3, true);
        graph.insertEdge("v2v3", v2, v3, false);
        graph.insertEdge("v2v4", v2, v4, true);
        graph.insertEdge("v3v4", v3, v4, true);

        InteriorNode in1 = graph.insertInterior("v1v2v3", v1, v2, v3);
        InteriorNode in2 = graph.insertInterior("v2v3v4", v2, v3, v4);

        in1.setPartitionRequired(true);
        in2.setPartitionRequired(false);

        return graph;
    }

    private ModelGraph generateTest2Graph() {
        ModelGraph graph = new ModelGraph("test2Graph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 200.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(200.0, 200.0, 20.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(300.0, 200.0, 60.0));
        Vertex v4 = graph.insertVertex("v4", VertexType.HANGING_NODE, new Point3d(50.0, 150.0, 5.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.HANGING_NODE, new Point3d(150.0, 150.0, 15.0));
        Vertex v6 = graph.insertVertex("v6", VertexType.SIMPLE_NODE, new Point3d(0.0, 100.0, 0.0));
        Vertex v7 = graph.insertVertex("v7", VertexType.SIMPLE_NODE, new Point3d(100.0, 100.0, 10.0));
        Vertex v8 = graph.insertVertex("v8", VertexType.HANGING_NODE, new Point3d(200.0, 100.0, 20.0));
        Vertex v9 = graph.insertVertex("v9", VertexType.SIMPLE_NODE, new Point3d(300.0, 100.0, 50.0));
        Vertex v10 = graph.insertVertex("v10", VertexType.HANGING_NODE, new Point3d(50.0, 50.0, 5.0));
        Vertex v11 = graph.insertVertex("v11", VertexType.HANGING_NODE, new Point3d(100.0, 50.0, 10.0));
        Vertex v12 = graph.insertVertex("v12", VertexType.HANGING_NODE, new Point3d(150.0, 50.0, 15.0));
        Vertex v13 = graph.insertVertex("v13", VertexType.HANGING_NODE, new Point3d(250.0, 50.0, 45.0));
        Vertex v14 = graph.insertVertex("v14", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v15 = graph.insertVertex("v15", VertexType.SIMPLE_NODE, new Point3d(100.0, 0.0, 10.0));
        Vertex v16 = graph.insertVertex("v16", VertexType.SIMPLE_NODE, new Point3d(200.0, 0.0, 20.0));
        Vertex v17 = graph.insertVertex("v17", VertexType.SIMPLE_NODE, new Point3d(300.0, 0.0, 40.0));

        graph.insertEdge("v1v2", v1, v2, true);
        graph.insertEdge("v1v4", v1, v4, false);
        graph.insertEdge("v1v6", v1, v6, true);
        graph.insertEdge("v2v5", v2, v5, false);
        graph.insertEdge("v2v8", v2, v8, false);
        graph.insertEdge("v2v3", v2, v3, true);
        graph.insertEdge("v2v9", v2, v9, true);
        graph.insertEdge("v3v9", v3, v9, true);
        graph.insertEdge("v4v6", v4, v6, false);
        graph.insertEdge("v4v7", v4, v7, false);
        graph.insertEdge("v5v7", v5, v7, false);
        graph.insertEdge("v5v8", v5, v8, false);
        graph.insertEdge("v6v7", v6, v7, false);
        graph.insertEdge("v6v10", v6, v10, false);
        graph.insertEdge("v6v14", v6, v14, true);
        graph.insertEdge("v7v11", v7, v11, false);
        graph.insertEdge("v7v12", v7, v12, false);
        graph.insertEdge("v7v8", v7, v8, false);
        graph.insertEdge("v8v9", v8, v9, false);
        graph.insertEdge("v9v13", v9, v13, false);
        graph.insertEdge("v9v17", v9, v17, true);
        graph.insertEdge("v10v14", v10, v14, false);
        graph.insertEdge("v10v15", v10, v15, false);
        graph.insertEdge("v11v15", v11, v15, false);
        graph.insertEdge("v11v12", v11, v12, false);
        graph.insertEdge("v12v15", v12, v15, false);
        graph.insertEdge("v12v16", v12, v16, false);
        graph.insertEdge("v13v16", v13, v16, false);
        graph.insertEdge("v13v17", v13, v17, false);
        graph.insertEdge("v14v15", v14, v15, true);
        graph.insertEdge("v15v16", v15, v16, true);
        graph.insertEdge("v16v17", v16, v17, true);

        InteriorNode in1 = graph.insertInterior("v1v2v7", v1, v2, v7);
        InteriorNode in2 = graph.insertInterior("v2v3v9", v2, v3, v9);
        InteriorNode in3 = graph.insertInterior("v1v4v6", v1, v4, v6);
        InteriorNode in4 = graph.insertInterior("v4v7v6", v4, v7, v6);
        InteriorNode in5 = graph.insertInterior("v5v8v7", v5, v8, v7);
        InteriorNode in6 = graph.insertInterior("v2v8v5", v2, v8, v5);
        InteriorNode in7 = graph.insertInterior("v2v9v8", v2, v9, v8);
        InteriorNode in8 = graph.insertInterior("v6v7v15", v6, v7, v15);
        InteriorNode in9 = graph.insertInterior("v7v9v16", v7, v9, v16);
        InteriorNode in10 = graph.insertInterior("v7v12v11", v7, v12, v11);
        InteriorNode in11 = graph.insertInterior("v6v10v14", v6, v10, v14);
        InteriorNode in12 = graph.insertInterior("v10v15v14", v10, v15, v14);
        InteriorNode in13 = graph.insertInterior("v11v12v15", v11, v12, v15);
        InteriorNode in14 = graph.insertInterior("v12v16v15", v12, v16, v15);
        InteriorNode in15 = graph.insertInterior("v13v17v16", v13, v17, v16);
        InteriorNode in16 = graph.insertInterior("v9v17v13", v9, v17, v13);

        return graph;
    }
}