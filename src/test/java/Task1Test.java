import model.*;
import org.junit.Test;
import transformation.*;

import java.util.*;
import java.util.function.Predicate;

public class Task1Test {
    private Transformation transformation1 = new TransformationP1();
    private Transformation transformation2 = new TransformationP2();
    private Transformation transformation3 = new TransformationP3();
    private Transformation transformation4 = new TransformationP4();
    private Transformation transformation5 = new TransformationP5();
    private Transformation transformation6 = new TransformationP6();
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
            for (Transformation transformation : transformations) {
                if (transformation.isConditionCompleted(graph, i)) {
                    graph = transformation.transformGraph(graph, i);
                    anyTransactionFired = true;
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
}
