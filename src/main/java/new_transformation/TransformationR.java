package new_transformation;

import model.*;
import org.javatuples.Triplet;
import transformation.Transformation;

public class TransformationR implements Transformation {


    private static final double EPSILON = 5;

    @Override
    public boolean isConditionCompleted(ModelGraph graph, InteriorNode interiorNode) {
        Triplet<Vertex, Vertex, Vertex> triangle = interiorNode.getTriangle();
        final double z0 = triangle.getValue0().getZCoordinate();
        final double z1 = triangle.getValue0().getZCoordinate();
        final double z2 = triangle.getValue0().getZCoordinate();
        return (Math.abs(z1-z0) + Math.abs(z2-z0) + Math.abs(z2-z1)) / 2.0 > EPSILON;
//        return Math.max(Math.max(z0, z1), z2) - Math.min(Math.min(z0, z1), z2) > EPSILON;
    }

    public static ModelGraph graphR() {
        ModelGraph graph = new ModelGraph("testGraph");
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


        graph.insertEdge("1", v1, v2,  true);
        graph.insertEdge("2", v2, v3,  true);
        graph.insertEdge("3", v1, v4,  false);
        graph.insertEdge("4", v4, v7,  false);
        graph.insertEdge("5", v5, v2,  false);
        graph.insertEdge("6", v2, v8,  false);
        graph.insertEdge("7", v5, v7,  false);
        graph.insertEdge("8", v5, v8,  false);
        graph.insertEdge("9", v2, v9,  false);
        graph.insertEdge("10", v3, v9,  true);
        graph.insertEdge("11", v7, v8,  false);
        graph.insertEdge("12", v6, v4,  false);
        graph.insertEdge("13", v6, v7,  false);
        graph.insertEdge("14", v1, v6,  true);
        graph.insertEdge("15", v8, v9,  false);
        graph.insertEdge("16", v6, v14,  true);
        graph.insertEdge("17", v6, v10,  false);
        graph.insertEdge("18", v10, v14,  false);
        graph.insertEdge("19", v10, v15,  false);
        graph.insertEdge("20", v15, v11,  false);
        graph.insertEdge("21", v11, v7,  false);
        graph.insertEdge("22", v14, v15,  true);
        graph.insertEdge("23", v11, v12,  false);
        graph.insertEdge("24", v12, v7,  false);
        graph.insertEdge("25", v12, v15,  false);
        graph.insertEdge("26", v15, v16,  true);
        graph.insertEdge("27", v16, v12,  false);
        graph.insertEdge("28", v16, v13,  false);
        graph.insertEdge("29", v13, v9,  false);
        graph.insertEdge("30", v13, v17,  false);
        graph.insertEdge("31", v17, v16,  true);
        graph.insertEdge("32", v17, v9,  true);

        graph.insertInterior("1",v1 ,v2 ,v7, v4, v5);
        graph.insertInterior("2",v2 ,v3 ,v9);
        graph.insertInterior("3",v2 ,v8 ,v9);
        graph.insertInterior("4",v2 ,v5 ,v8);
        graph.insertInterior("5",v5 ,v7 ,v8);
        graph.insertInterior("6",v4 ,v6 ,v7);
        graph.insertInterior("7",v1 ,v4 ,v6);
        graph.insertInterior("8",v6 ,v7 ,v15, v10, v11);
        graph.insertInterior("9",v10 ,v14 ,v15);
        graph.insertInterior("10",v6 ,v10 ,v14);
        graph.insertInterior("11",v7 ,v11 ,v12);
        graph.insertInterior("12",v11 ,v12 ,v15);
        graph.insertInterior("13",v12 ,v15 ,v16);
        graph.insertInterior("14",v7 ,v9 ,v16, v8, v12, v15);
        graph.insertInterior("15",v13 ,v16 ,v17);
        graph.insertInterior("16",v9 ,v13 ,v17);
        return graph;
    }

    @Override
    public ModelGraph transformGraph(ModelGraph graph, InteriorNode interiorNode) {
        if (this.isConditionCompleted(graph, interiorNode)) {
            interiorNode.setPartitionRequired(true);
        }
        return graph;
    }
}
