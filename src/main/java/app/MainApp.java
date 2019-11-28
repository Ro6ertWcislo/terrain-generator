package app;

import model.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import processor.MapProcessingUtil;
import transformation.Transformation;
import transformation.TransformationP1;
import transformation.TransformationP2;
import transformation.TransformationP7;

import java.util.ArrayList;
import java.util.List;

public class MainApp {

    private static Logger log = Logger.getLogger(MainApp.class.getName());

    private static void testTransformationP2() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(100.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(200.0, 0.0, 0.0));
        Vertex v4 = graph.insertVertex("v4", VertexType.HANGING_NODE, new Point3d(50.0, 50.0, 0.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.HANGING_NODE, new Point3d(150.0, 50.0, 0.0));
        Vertex v6 = graph.insertVertex("v6", VertexType.SIMPLE_NODE, new Point3d(0.0, 100.0, 0.0));
        Vertex v7 = graph.insertVertex("v7", VertexType.SIMPLE_NODE, new Point3d(100.0, 100.0, 0.0));
        Vertex v8 = graph.insertVertex("v8", VertexType.SIMPLE_NODE, new Point3d(200.0, 100.0, 0.0));

        graph.insertEdge("e1", v1, v2, true);
        graph.insertEdge("e2", v2, v3, true);
        graph.insertEdge("e3", v1, v4, false);
        graph.insertEdge("e4", v2, v4, false);
        graph.insertEdge("e5", v2, v7, false);
        graph.insertEdge("e6", v2, v5, false);
        graph.insertEdge("e7", v3, v8, true);
        graph.insertEdge("e8", v1, v6, true);
        graph.insertEdge("e9", v4, v6, false);
        graph.insertEdge("e10", v5, v7, false);
        graph.insertEdge("e11", v5, v8, false);
        graph.insertEdge("e12", v6, v7, true);
        graph.insertEdge("e13", v7, v8, true);

        List<InteriorNode> ints = new ArrayList<>();
        ints.add(graph.insertInterior("i1", v1, v2, v4));
        ints.add(graph.insertInterior("i2", v1, v4, v6));
        ints.add(graph.insertInterior("i3", v2, v6, v7));
        ints.get(2).setPartitionRequired(true);
        ints.add(graph.insertInterior("i4", v2, v5, v7));
        ints.add(graph.insertInterior("i5", v5, v7, v8));
        ints.add(graph.insertInterior("i6", v2, v3, v8));
        ints.get(5).setPartitionRequired(true);

        graph.display();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 0;
        for(InteriorNode _i : ints) {
            Transformation t2 = new TransformationP2();
            if (t2.isConditionCompleted(graph, _i)) {
                System.out.println("true ".concat(Integer.toString(i)));
                t2.transformGraph(graph, _i);
            }
            else {
                System.out.println("false ".concat(Integer.toString(i)));
            }
            i++;
        }
    }

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

    public static void main(String[] args) {
        BasicConfigurator.configure();

        testTransformationP2();

//        Pair<ModelGraph, InteriorNode> task = task1();
//        ModelGraph graph = task.getValue0();
//        InteriorNode interiorNode = task.getValue1();
//
//        Transformation t1 = new TransformationP1();
//        log.info(String.format("Condition state for transformation P1: %b", t1.isConditionCompleted(graph, interiorNode)));
//
//        graph.display();
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        t1.transformGraph(graph, interiorNode);

//        TerrainMap map = new TerrainMap();
//        map.fillMapWithExampleData();
//
//        ModelGraph graph = new ModelGraph("testGraph");
//        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 2.0));
//        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(5.0, 0.0, 2.0));
//        Vertex v3 = graph.insertVertex("v3", VertexType.HANGING_NODE, new Point3d(0.0, 3.0, 2.0));
//        graph.insertEdge("e1", v1, v2, true);
//        graph.insertEdge("e2", v2, v3, true);
//        graph.insertEdge("e3", v3, v1, true);
//        InteriorNode in1 = graph.insertInterior("i1", v1, v2, v3);
//
//        System.out.println(map.getAllPointsInTriangleArea(in1).size());
//        System.out.println(MapProcessingUtil.calculateTerrainApproximationError(in1, map));
    }
}
