package app;

import model.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import transformation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static Pair<ModelGraph, InteriorNode> task15() {
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

    private static ModelGraph fooGraph() {
        ModelGraph graph = new ModelGraph("graph");
        Vertex v01 = graph.insertVertex("v01", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v02 = graph.insertVertex("v02", VertexType.SIMPLE_NODE, new Point3d(100.0, 0.0, 10.0));
        Vertex v03 = graph.insertVertex("v03", VertexType.SIMPLE_NODE, new Point3d(200.0, 0.0, 20.0));
        Vertex v04 = graph.insertVertex("v04", VertexType.SIMPLE_NODE, new Point3d(300.0, 0.0, 40.0));
        Vertex v05 = graph.insertVertex("v05", VertexType.SIMPLE_NODE, new Point3d(50.0, 50.0, 5.0));
        Vertex v06 = graph.insertVertex("v06", VertexType.SIMPLE_NODE, new Point3d(100.0, 50.0, 10.0));
        Vertex v07 = graph.insertVertex("v07", VertexType.SIMPLE_NODE, new Point3d(150.0, 50.0, 15.0));
        Vertex v08 = graph.insertVertex("v08", VertexType.SIMPLE_NODE, new Point3d(250.0, 50.0, 45.0));
        Vertex v09 = graph.insertVertex("v09", VertexType.SIMPLE_NODE, new Point3d(0.0, 100.0, 0.0));
        Vertex v10 = graph.insertVertex("v10", VertexType.SIMPLE_NODE, new Point3d(100.0, 100.0, 10.0));
        Vertex v11 = graph.insertVertex("v11", VertexType.SIMPLE_NODE, new Point3d(200.0, 100.0, 20.0));
        Vertex v12 = graph.insertVertex("v12", VertexType.SIMPLE_NODE, new Point3d(300.0, 100.0, 50.0));
        Vertex v13 = graph.insertVertex("v13", VertexType.SIMPLE_NODE, new Point3d(50.0, 150.0, 5.0));
        Vertex v14 = graph.insertVertex("v14", VertexType.SIMPLE_NODE, new Point3d(150.0, 150.0, 15.0));
        Vertex v15 = graph.insertVertex("v15", VertexType.SIMPLE_NODE, new Point3d(0.0, 200.0, 0.0));
        Vertex v16 = graph.insertVertex("v16", VertexType.SIMPLE_NODE, new Point3d(200.0, 200.0, 20.0));
        Vertex v17 = graph.insertVertex("v17", VertexType.SIMPLE_NODE, new Point3d(300.0, 200.0, 60.0));

        GraphEdge e01 = graph.insertEdge("v01v02", v01, v02);
        GraphEdge e02 = graph.insertEdge("v02v03", v02, v03);
        GraphEdge e03 = graph.insertEdge("v03v04", v03, v04);
        GraphEdge e04 = graph.insertEdge("v01v09", v01, v09);
        GraphEdge e05 = graph.insertEdge("v01v05", v01, v05);
        GraphEdge e06 = graph.insertEdge("v05v09", v05, v09);
        GraphEdge e07 = graph.insertEdge("v02v05", v02, v05);
        GraphEdge e08 = graph.insertEdge("v06v10", v06, v10);
        GraphEdge e09 = graph.insertEdge("v02v06", v02, v06);
        GraphEdge e10 = graph.insertEdge("v06v07", v06, v07);
        GraphEdge e11 = graph.insertEdge("v10v07", v10, v07);
        GraphEdge e12 = graph.insertEdge("v03v07", v03, v07);
        GraphEdge e13 = graph.insertEdge("v03v11", v03, v11);
        GraphEdge e14 = graph.insertEdge("v03v08", v03, v08);
        GraphEdge e15 = graph.insertEdge("v08v12", v08, v12);
        GraphEdge e16 = graph.insertEdge("v04v08", v04, v08);
        GraphEdge e17 = graph.insertEdge("v04v12", v04, v12);
        GraphEdge e18 = graph.insertEdge("v09v10", v09, v10);
        GraphEdge e19 = graph.insertEdge("v10v11", v10, v11);
        GraphEdge e20 = graph.insertEdge("v11v12", v11, v12);
        GraphEdge e21 = graph.insertEdge("v09v15", v09, v15);
        GraphEdge e22 = graph.insertEdge("v09v13", v09, v13);
        GraphEdge e23 = graph.insertEdge("v15v13", v15, v13);
        GraphEdge e24 = graph.insertEdge("v13v10", v13, v10);
        GraphEdge e25 = graph.insertEdge("v10v14", v10, v14);
        GraphEdge e26 = graph.insertEdge("v14v16", v14, v16);
        GraphEdge e27 = graph.insertEdge("v14v11", v14, v11);
        GraphEdge e28 = graph.insertEdge("v11v16", v11, v16);
        GraphEdge e29 = graph.insertEdge("v16v12", v16, v12);
        GraphEdge e30 = graph.insertEdge("v12v17", v12, v17);
        GraphEdge e31 = graph.insertEdge("v15v16", v15, v16);
        GraphEdge e32 = graph.insertEdge("v16v17", v16, v17);
        GraphEdge e33 = graph.insertEdge("v02v07", v02, v07);

        graph.insertInterior("i01", v01, v05, v09);
        graph.insertInterior("i02", v01, v02, v05);
        graph.insertInterior("i03", v09, v02, v10);
        graph.insertInterior("i04", v06, v07, v10);
        graph.insertInterior("i05", v02, v07, v06);
        graph.insertInterior("i06", v02, v03, v07);
        graph.insertInterior("i07", v10, v03, v11);
        graph.insertInterior("i08", v03, v12, v11);
        graph.insertInterior("i09", v03, v04, v08);
        graph.insertInterior("i10", v08, v04, v12);
        graph.insertInterior("i11", v15, v09, v13);
        graph.insertInterior("i12", v09, v10, v13);
        graph.insertInterior("i13", v15, v10, v16);
        graph.insertInterior("i14", v10, v11, v14);
        graph.insertInterior("i15", v14, v11, v16);
        graph.insertInterior("i16", v11, v12, v16);
        graph.insertInterior("i17", v16, v12, v17);
        return graph;
    }

    private static void foo() {
        BasicConfigurator.configure();
        List<Transformation> transformations = Arrays.asList(new TransformationP1(), new TransformationP2(), new TransformationP3(),
                new TransformationP4(), new TransformationP5(), new TransformationP6(), new TransformationP7());

        Transformation T7 = new TransformationP7();
        ModelGraph graph = fooGraph();
        List<InteriorNode> ints = new ArrayList<>();

        final int iters = 7;
        graph.display();

        for (int i = 0; i < iters; i++) {

            for (Transformation tr : transformations) {
                List<InteriorNode> interiors = new ArrayList<>(graph.getInteriors());
                for (int k = 0; k < interiors.size(); k++) {
                    if (tr.isConditionCompleted(graph, interiors.get(k))) {
                        graph = tr.transformGraph(graph, interiors.get(k));
                    }
                }
            }

            System.out.println(" After " + (i+1) + "iter ");
        }

    }

    public static void main(String[] args) {
        foo();
//        BasicConfigurator.configure();
//
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
