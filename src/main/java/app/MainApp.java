package app;

import model.*;
import new_transformation.TransformationP4;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import transformation.*;

import java.util.*;

public class MainApp {

    private static final double EPSILON = 5.0;
    private static Logger log = Logger.getLogger(MainApp.class.getName());

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

    private static ModelGraph prepareTestGraph() {
        ModelGraph graph = new ModelGraph("testGraph");

        Vertex v00 = graph.insertVertex("00", VertexType.SIMPLE_NODE, new Point3d(0., 200., 0.));
        Vertex v01 = graph.insertVertex("01", VertexType.SIMPLE_NODE, new Point3d(200., 200., 20.));
        Vertex v02 = graph.insertVertex("02", VertexType.SIMPLE_NODE, new Point3d(300., 200., 60.));
        Vertex v10 = graph.insertVertex("10", VertexType.HANGING_NODE, new Point3d(60., 150., 5.));
        Vertex v11 = graph.insertVertex("11", VertexType.HANGING_NODE, new Point3d(150., 150., 15.));
        Vertex v20 = graph.insertVertex("20", VertexType.SIMPLE_NODE, new Point3d(0., 100., 0.));
        Vertex v21 = graph.insertVertex("21", VertexType.SIMPLE_NODE, new Point3d(100., 100., 0.));
        Vertex v22 = graph.insertVertex("22", VertexType.HANGING_NODE, new Point3d(200., 100., 20.));
        Vertex v23 = graph.insertVertex("23", VertexType.SIMPLE_NODE, new Point3d(300., 100., 50.));
        Vertex v30 = graph.insertVertex("30", VertexType.HANGING_NODE, new Point3d(50., 50., 5.));
        Vertex v31 = graph.insertVertex("31", VertexType.HANGING_NODE, new Point3d(100., 50., 10.));
        Vertex v32 = graph.insertVertex("32", VertexType.HANGING_NODE, new Point3d(150., 50., 15.));
        Vertex v33 = graph.insertVertex("33", VertexType.HANGING_NODE, new Point3d(250., 50., 45.));
        Vertex v40 = graph.insertVertex("40", VertexType.SIMPLE_NODE, new Point3d(0., 0., 0.));
        Vertex v41 = graph.insertVertex("41", VertexType.SIMPLE_NODE, new Point3d(100., 0., 10.));
        Vertex v42 = graph.insertVertex("42", VertexType.SIMPLE_NODE, new Point3d(200., 0., 20.));
        Vertex v43 = graph.insertVertex("43", VertexType.SIMPLE_NODE, new Point3d(300., 0., 40.));

        graph.insertEdge("0", v00, v01);
        graph.insertEdge("1", v01, v02);
        graph.insertEdge("2", v00, v10);
        graph.insertEdge("3", v11, v01);
        graph.insertEdge("4", v00, v20);
        graph.insertEdge("5", v20, v10);
        graph.insertEdge("6", v21, v20);
        graph.insertEdge("7", v21, v10);
        graph.insertEdge("8", v21, v11);
        graph.insertEdge("9", v22, v21);
        graph.insertEdge("10", v22, v11);
        graph.insertEdge("11", v22, v01);
        graph.insertEdge("12", v23, v22);
        graph.insertEdge("13", v23, v01);
        graph.insertEdge("14", v23, v02);
        graph.insertEdge("15", v30, v20);
        graph.insertEdge("16", v31, v21);
        graph.insertEdge("17", v32, v31);
        graph.insertEdge("18", v32, v21);
        graph.insertEdge("19", v33, v23);
        graph.insertEdge("20", v40, v20);
        graph.insertEdge("21", v40, v30);
        graph.insertEdge("22", v41, v40);
        graph.insertEdge("23", v41, v30);
        graph.insertEdge("24", v41, v31);
        graph.insertEdge("25", v41, v32);
        graph.insertEdge("26", v42, v41);
        graph.insertEdge("27", v42, v32);
        graph.insertEdge("28", v42, v33);
        graph.insertEdge("29", v43, v42);
        graph.insertEdge("30", v43, v33);
        graph.insertEdge("31", v43, v23);

        graph.insertInterior("i0", v00, v10, v20);
        graph.insertInterior("i1", v00, v21, v01);
        graph.insertInterior("i2", v01, v11, v22);
        graph.insertInterior("i3", v01, v22, v23);
        graph.insertInterior("i4", v01, v23, v02);
        graph.insertInterior("i5", v10, v20, v21);
        graph.insertInterior("i6", v11, v21, v22);
        graph.insertInterior("i7", v20, v40, v30);
        graph.insertInterior("i8", v20, v41, v21);
        graph.insertInterior("i9", v21, v31, v32);
        graph.insertInterior("i10", v21, v42, v23);
        graph.insertInterior("i11", v23, v33, v43);
        graph.insertInterior("i12", v30, v40, v41);
        graph.insertInterior("i13", v31, v41, v32);
        graph.insertInterior("i14", v32, v41, v42);
        graph.insertInterior("i15", v33, v42, v43);

        return graph;
    }

    private static void manyProds() {
        ModelGraph startingGraph = prepareTestGraph();
//        startingGraph.display();
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        List<Transformation> transformations = Arrays.asList(new TransformationP1(), new TransformationP2(),
                new TransformationP3(), new TransformationP4(), new TransformationP5(), new TransformationP6());

        for (int i = 0; i < 1; i++) {
            for (InteriorNode in : startingGraph.getInteriors()) {
                if (getDifference(in) > EPSILON) {
                    in.setPartitionRequired(true);
                }
            }

            for (Transformation t : transformations) {
                for (InteriorNode in : startingGraph.getInteriors()) {
                    if (t.isConditionCompleted(startingGraph, in)) {
                        startingGraph = t.transformGraph(startingGraph, in);
                    }
                }
            }
            startingGraph.display();
        }
    }

    private static double getDifference(InteriorNode in) {
        List<Double> zList = Arrays.asList(in.getTriangle().getValue0().getZCoordinate(),
                in.getTriangle().getValue1().getZCoordinate(),
                in.getTriangle().getValue2().getZCoordinate());

        return Collections.max(zList) - Collections.min(zList);
    }

    public static void main(String[] args) {
        manyProds();
    }
}
