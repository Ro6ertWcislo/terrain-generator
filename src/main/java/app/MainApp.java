package app;

import model.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import transformation.Transformation;
import transformation.TransformationP1;
import transformation.TransformationP6;

import java.util.HashMap;
import java.util.Map;

public class MainApp {

    private static Logger log = Logger.getLogger(MainApp.class.getName());

    private static Pair<ModelGraph, InteriorNode> task1() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(2.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(1.0, 1.0, 0.0));
        graph.insertEdge("e1", v1, v2, true);
        graph.insertEdge("e2", v2, v3, true);
        graph.insertEdge("e3", v3, v1, true);
        InteriorNode in1 = graph.insertInterior("i1", v1, v2, v3);
        in1.setPartitionRequired(true);

        return new Pair<>(graph, in1);
    }

    private static Pair<ModelGraph, InteriorNode> task5() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(2.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(1.0, 1.0, 0.0));
        Vertex h4 = graph.insertVertex("h4", VertexType.HANGING_NODE, new Point3d(1.0, 0.0, 0.0));

        GraphEdge v1_h4 = graph.insertEdge("e1", v1, h4, true);
        GraphEdge v1_v3 = graph.insertEdge("e2", v1, v3, true);
        GraphEdge h4_v2 = graph.insertEdge("e3", h4, v2, true);
        GraphEdge v2_v3 = graph.insertEdge("e4", v2, v3, true);

        InteriorNode in1 = graph.insertInterior("i1", v1, v2, v3);
        return new Pair<>(graph, in1);
    }

    private static Pair<ModelGraph, InteriorNode> task11() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex h2 = graph.insertVertex("h2", VertexType.HANGING_NODE, new Point3d(1.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(2.0, 0.0, 0.0));
        Vertex h4 = graph.insertVertex("h4", VertexType.HANGING_NODE, new Point3d(1.5, 0.5, 0.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.SIMPLE_NODE, new Point3d(1.0, 1.0, 0.0));

        GraphEdge v1_h2 = graph.insertEdge("e1", v1, h2, true);
        GraphEdge h2_v3 = graph.insertEdge("e2", h2, v3, true);
        GraphEdge v3_h4 = graph.insertEdge("e3", v3, h4, true);
        GraphEdge h3_v5 = graph.insertEdge("e4", h4, v5, true);
        GraphEdge v1_h5 = graph.insertEdge("e5", v1, v5, true);

        InteriorNode in1 = graph.insertInterior("i1", v1, v3, v5, h2, h4);
        return new Pair<>(graph, in1);
    }

    private static Pair<ModelGraph, InteriorNode> task15(){
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex h2 = graph.insertVertex("h2", VertexType.HANGING_NODE, new Point3d(1.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(2.0, 0.0, 0.0));
        Vertex h4 = graph.insertVertex("h4", VertexType.HANGING_NODE, new Point3d(1.75, 1.0, 0.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.SIMPLE_NODE, new Point3d(1.5, 2.0, 0.0));

        GraphEdge v1_h2 = graph.insertEdge("e1", v1, h2, false);
        GraphEdge h2_v3 = graph.insertEdge("e2", h2, v3, false);
        GraphEdge v3_h4 = graph.insertEdge("e3", v3, h4, false);
        GraphEdge h3_v5 = graph.insertEdge("e4", h4, v5, false);
        GraphEdge v1_h5 = graph.insertEdge("e5", v1, v5, false);

        InteriorNode in1 = graph.insertInterior("i1", v1, v3, v5, h2, h4);
        return new Pair<>(graph, in1);
    }

    private static Pair<ModelGraph, InteriorNode> task17() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex h2 = graph.insertVertex("h2", VertexType.HANGING_NODE, new Point3d(1.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(2.0, 0.0, 0.0));
        Vertex h4 = graph.insertVertex("h4", VertexType.HANGING_NODE, new Point3d(1.5, 0.5, 0.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.SIMPLE_NODE, new Point3d(1.0, 1.0, 0.0));
        Vertex h6 = graph.insertVertex("h6", VertexType.HANGING_NODE, new Point3d(0.5, 0.5, 0.0));

        GraphEdge v1_h2 = graph.insertEdge("e1", v1, h2, true);
        GraphEdge h2_v3 = graph.insertEdge("e2", h2, v3, true);
        GraphEdge v3_h4 = graph.insertEdge("e3", v3, h4, true);
        GraphEdge h4_v5 = graph.insertEdge("e4", h4, v5, true);
        GraphEdge v5_h6 = graph.insertEdge("e5", v5, h6, true);
        GraphEdge h6_v1 = graph.insertEdge("e6", h6, v1, true);

        InteriorNode in1 = graph.insertInterior("i1", v1, v3, v5);
        return new Pair<>(graph, in1);
    }

    private static Pair<ModelGraph, InteriorNode> task13() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(1.0, 0.0, 0.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.SIMPLE_NODE, new Point3d(2.0, 1.0, 0.0));
        Vertex h2 = graph.insertVertex("h2", VertexType.HANGING_NODE, Point3d.middlePoint(v1.getCoordinates(), v3.getCoordinates()));
        Vertex h4 = graph.insertVertex("h4", VertexType.HANGING_NODE, Point3d.middlePoint(v3.getCoordinates(), v5.getCoordinates()));

        GraphEdge v1_h2 = graph.insertEdge("e1", v1, h2, false);
        GraphEdge h2_v3 = graph.insertEdge("e2", h2, v3, false);
        GraphEdge v3_h4 = graph.insertEdge("e3", v3, h4, false);
        GraphEdge h4_v5 = graph.insertEdge("e4", h4, v5, false);
        GraphEdge v1_v5 = graph.insertEdge("e6", v1, v5, true);

        InteriorNode in1 = graph.insertInterior("i1", v1, v3, v5, h2, h4);
        return new Pair<>(graph, in1);
    }

        private static Pair<ModelGraph, Map<InteriorNode, Boolean>> createEnvelopeGraph() {
        ModelGraph graph = new ModelGraph("envelopeGraphTest");

        // vertices top -> down; in the same level: left -> right
        Vertex v0 = graph.insertVertex("v0", VertexType.SIMPLE_NODE, new Point3d(150., 150., 0.));
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(100., 100., 0.));
        Vertex v2 = graph.insertVertex("v2", VertexType.HANGING_NODE, new Point3d(150., 100., 0.));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(250., 100., 0.));
        Vertex v4 = graph.insertVertex("v4", VertexType.SIMPLE_NODE, new Point3d(100., 50., 0.));
        Vertex v5 = graph.insertVertex("v5", VertexType.HANGING_NODE, new Point3d(150., 50., 0.));
        Vertex v6 = graph.insertVertex("v6", VertexType.HANGING_NODE, new Point3d(225., 50., 0.));
        Vertex v7 = graph.insertVertex("v7", VertexType.SIMPLE_NODE, new Point3d(100., 0., 0.));
        Vertex v8 = graph.insertVertex("v8", VertexType.SIMPLE_NODE, new Point3d(200., 0., 0.));
        Vertex v9 = graph.insertVertex("v9", VertexType.SIMPLE_NODE, new Point3d(250., 0., 0.));

        //edges
        graph.insertEdge("e0", v0, v1);
        graph.insertEdge("e1", v0, v2);
        graph.insertEdge("e2", v0, v3);
        graph.insertEdge("e3", v1, v2);
        graph.insertEdge("e4", v1, v4);
        graph.insertEdge("e5", v1, v5);
        graph.insertEdge("e6", v2, v3);
        graph.insertEdge("e7", v3, v6);
        graph.insertEdge("e8", v3, v9);
        graph.insertEdge("e9", v4, v7);
        graph.insertEdge("e10", v5, v7);
        graph.insertEdge("e11", v5, v8);
        graph.insertEdge("e12", v6, v8);
        graph.insertEdge("e13", v6, v9);
        graph.insertEdge("e14", v7, v8);
        graph.insertEdge("e15", v8, v9);

        // i-nodes
        Map<InteriorNode, Boolean> nodesWithFlag = new HashMap<>();
        nodesWithFlag.put(graph.insertInterior("i0", v0, v1, v2), false);
        nodesWithFlag.put(graph.insertInterior("i1", v0, v2, v3), false);
        nodesWithFlag.put(graph.insertInterior("i2", v1, v5, v7), false);
        nodesWithFlag.put(graph.insertInterior("i3", v1, v3, v8), true);  // <-- correct :D
        nodesWithFlag.put(graph.insertInterior("i4", v3, v6, v9), false);
        nodesWithFlag.put(graph.insertInterior("i5", v5, v7, v8), false);
        nodesWithFlag.put(graph.insertInterior("i6", v6, v8, v9), false);

        return Pair.with(graph, nodesWithFlag);
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();

        Transformation t6 = new TransformationP6();

        Pair<ModelGraph, Map<InteriorNode, Boolean>> a = createEnvelopeGraph();
        ModelGraph graph = a.getValue0();
        InteriorNode interiorNode = a.getValue1().entrySet().stream().filter(Map.Entry::getValue).findAny().get().getKey();

        graph.display();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t6.transformGraph(graph, interiorNode);
        graph.display();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
