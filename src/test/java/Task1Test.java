import model.*;
import org.junit.Test;
import transformation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Task1Test {
    private Transformation transformation1 = new TransformationP1();
    private Transformation transformation2 = new TransformationP2();
    private Transformation transformation3 = new TransformationP3();
    private Transformation transformation4 = new TransformationP4();
    private Transformation transformation5 = new TransformationP5();
    private Transformation transformation6 = new TransformationP6();

    //Additional transformation
    private Transformation transformation7 = new TransformationP7();

    @Test
    public void breakTriangles(){
        ModelGraph graph = generateTestGraph();
        graph.display();
        sleepFor3Seconds();

        fireProductionsWhilePossible(graph, this::getTransformationsList1);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getXCoordinate() < 50).setPartitionRequired(true);
        fireProductionsWhilePossible(graph, this::getTransformationsList1);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getYCoordinate() > 50 && i.getYCoordinate() < 75)
                .setPartitionRequired(true);
        fireProductionsWhilePossible(graph, this::getTransformationsList1);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getYCoordinate() > 50 && i.getYCoordinate() < 73)
                .setPartitionRequired(true);
        fireProductionsWhilePossible(graph, this::getTransformationsList1);
        sleepFor3Seconds();

        findTriangleToBreak(graph, (i) -> i.getXCoordinate() > 25 && i.getYCoordinate() > 50 && i.getYCoordinate() < 73)
                .setPartitionRequired(true);
        fireProductionsWhilePossible(graph, this::getTransformationsList1);
        sleepFor3Seconds();

    }

    @Test
    public void breakTrianglesAutomatically(){
        ModelGraph graph = generateTestGraph2();
        graph.display();
        sleepFor3Seconds();

        fireProductionsWhilePossible(graph, this::getTransformationsList2);
    }

    private void fireProductionsWhilePossible(ModelGraph graph, Supplier<List<Transformation>> transformationSupplier){
        boolean graphChanged;
        do {
            graphChanged = onePassThroughGraph(graph, transformationSupplier);
        } while(graphChanged);
    }

    private boolean onePassThroughGraph(ModelGraph graph, Supplier<List<Transformation>> transformationSupplier){
        boolean anyTransactionFired = false;
        List<Transformation> transformations = transformationSupplier.get();

        Collection<InteriorNode> interiorNodes = new ArrayList<>(graph.getInteriors());
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

    private List<Transformation> getTransformationsList1(){
        return Arrays.asList(transformation1, transformation2, transformation3,
                transformation4, transformation5, transformation6);
    }

    private List<Transformation> getTransformationsList2(){
        return Arrays.asList(transformation7, transformation1, transformation2, transformation3,
                transformation4, transformation5, transformation6);
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

    private ModelGraph generateTestGraph2() {
        ModelGraph graph = new ModelGraph("testGraph2");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 200.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(0.0, 100.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v4 = graph.insertVertex("v4", VertexType.HANGING_NODE, new Point3d(50.0, 150.0, 5.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.SIMPLE_NODE, new Point3d(100.0, 100.0, 10.0));
        Vertex v6 = graph.insertVertex("v6", VertexType.HANGING_NODE, new Point3d(50.0, 50.0, 5.0));
        Vertex v7 = graph.insertVertex("v7", VertexType.HANGING_NODE, new Point3d(100.0, 50.0, 10.0));
        Vertex v8 = graph.insertVertex("v8", VertexType.SIMPLE_NODE, new Point3d(100.0, 0.0, 10.0));
        Vertex v9 = graph.insertVertex("v9", VertexType.HANGING_NODE, new Point3d(150.0, 150.0, 15.0));
        Vertex v10 = graph.insertVertex("v10", VertexType.HANGING_NODE, new Point3d(150.0, 50.0, 15.0));
        Vertex v11 = graph.insertVertex("v11", VertexType.SIMPLE_NODE, new Point3d(200.0, 200.0, 20.0));
        Vertex v12 = graph.insertVertex("v12", VertexType.HANGING_NODE, new Point3d(200.0, 100.0, 20.0));
        Vertex v13 = graph.insertVertex("v13", VertexType.SIMPLE_NODE, new Point3d(200.0, 0.0, 20.0));
        Vertex v14 = graph.insertVertex("v14", VertexType.HANGING_NODE, new Point3d(250.0, 50.0, 45.0));
        Vertex v15 = graph.insertVertex("v15", VertexType.SIMPLE_NODE, new Point3d(300.0, 200.0, 60.0));
        Vertex v16 = graph.insertVertex("v16", VertexType.SIMPLE_NODE, new Point3d(300.0, 100.0, 50.0));
        Vertex v17 = graph.insertVertex("v17", VertexType.SIMPLE_NODE, new Point3d(300.0, 0.0, 40.0));

        graph.insertEdge("v1v2", v1, v2, true);
        graph.insertEdge("v2v3", v2, v3, true);
        graph.insertEdge("v4v2", v4, v2, false);
        graph.insertEdge("v1v4", v1, v4, false);
        graph.insertEdge("v4v5", v4, v5, false);
        graph.insertEdge("v2v5", v2, v5, false);
        graph.insertEdge("v2v6", v2, v6, false);
        graph.insertEdge("v3v6", v3, v6, false);
        graph.insertEdge("v6v8", v6, v8, false);
        graph.insertEdge("v3v8", v3, v8, true);
        graph.insertEdge("v5v7", v5, v7, false);
        graph.insertEdge("v7v8", v7, v8, false);
        graph.insertEdge("v7v10", v7, v10, false);
        graph.insertEdge("v5v10", v5, v10, false);
        graph.insertEdge("v8v10", v8, v10, false);
        graph.insertEdge("v8v13", v8, v13, true);
        graph.insertEdge("v10v13", v10, v13, false);
        graph.insertEdge("v5v12", v5, v12, false);
        graph.insertEdge("v5v9", v5, v9, false);
        graph.insertEdge("v9v12", v9, v12, false);
        graph.insertEdge("v9v11", v9, v11, false);
        graph.insertEdge("v11v12", v11, v12, false);
        graph.insertEdge("v1v11", v1, v11, true);
        graph.insertEdge("v13v14", v13, v14, false);
        graph.insertEdge("v14v16", v14, v16, false);
        graph.insertEdge("v12v16", v12, v16, false);
        graph.insertEdge("v13v17", v13, v17, true);
        graph.insertEdge("v14v17", v14, v17, false);
        graph.insertEdge("v16v17", v16, v17, true);
        graph.insertEdge("v11v16", v11, v16, false);
        graph.insertEdge("v11v15", v11, v15, true);
        graph.insertEdge("v15v16", v15, v16, true);

        InteriorNode in1 = graph.insertInterior("v1v2v4", v1, v2, v4);
        InteriorNode in2 = graph.insertInterior("v2v4v5", v2, v4, v5);
        InteriorNode in3 = graph.insertInterior("v1v5v11", v1, v5, v11);
        InteriorNode in4 = graph.insertInterior("v2v3v6", v2, v3, v6);
        InteriorNode in5 = graph.insertInterior("v3v6v8", v3, v6, v8);
        InteriorNode in6 = graph.insertInterior("v2v5v8", v2, v5, v8);
        InteriorNode in7 = graph.insertInterior("v5v7v10", v5, v7, v10);
        InteriorNode in8 = graph.insertInterior("v7v8v10", v7, v8, v10);
        InteriorNode in9 = graph.insertInterior("v5v9v12", v5, v9, v12);
        InteriorNode in10 = graph.insertInterior("v9v11v12", v9, v11, v12);
        InteriorNode in11 = graph.insertInterior("v8v10v13", v8, v10, v13);
        InteriorNode in12 = graph.insertInterior("v5v13v16", v5, v13, v16);
        InteriorNode in13 = graph.insertInterior("v11v12v16", v11, v12, v16);
        InteriorNode in14 = graph.insertInterior("v11v15v16", v11, v15, v16);
        InteriorNode in15 = graph.insertInterior("v14v16v17", v14, v16, v17);
        InteriorNode in16 = graph.insertInterior("v13v14v17", v13, v14, v17);

        return graph;
    }
}
