package new_transformation;

import model.*;
import org.apache.log4j.BasicConfigurator;
import org.javatuples.Triplet;
import transformation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TransformationP4 implements Transformation {

    @Override
    public boolean isConditionCompleted(ModelGraph graph, InteriorNode interiorNode){
        List<Vertex> associatedNodes = interiorNode.getAssociatedNodes();

        int associatedNodesCount = associatedNodes.size();
        if (associatedNodesCount != 2) {
            return false;
        }

        Triplet<Vertex, Vertex, Vertex> vertices = interiorNode.getTriangle();

        final Vertex v0 = vertices.getValue0();
        final Vertex v1 = vertices.getValue1();
        final Vertex v2 = vertices.getValue2();
        final Vertex hanging01 = getVertexBetween(v0, v1, associatedNodes).orElse(null);
        final Vertex hanging12 = getVertexBetween(v1, v2, associatedNodes).orElse(null);
        final Vertex hanging20 = getVertexBetween(v2, v0, associatedNodes).orElse(null);



        double e5 = 0.0;
        double e1_sum = 0.0;
        double e2_sum = 0.0;
        double e3_sum = 0.0;
        if(hanging01 == null)
            e5 = graph.getEdgeBetweenNodes(vertices.getValue0(), vertices.getValue1()).get().getL();
        else
            e1_sum = graph.getEdgeBetweenNodes(hanging01, v0).get().getL() +
                    graph.getEdgeBetweenNodes(hanging01, v1).get().getL();
        if(hanging12 == null) {
            assert e5 == 0.0;
            e5 = graph.getEdgeBetweenNodes(v1, v2).get().getL();
        }
        else
            e2_sum = graph.getEdgeBetweenNodes(hanging12, v1).get().getL() +
                    graph.getEdgeBetweenNodes(hanging12, v2).get().getL();
        if(hanging20 == null) {
            assert e5 == 0.0;
            e5 = graph.getEdgeBetweenNodes(v2, v0).get().getL();
        }
        else
            e3_sum = graph.getEdgeBetweenNodes(hanging20, v0).get().getL() +
                    graph.getEdgeBetweenNodes(hanging20, v2).get().getL();


        return (e1_sum >= e5 || e2_sum >= e5 || e3_sum >= e5);
    }

    private static Optional<Vertex> getVertexBetween(Vertex v1, Vertex v2, List<Vertex> associatedNodes) {
        return associatedNodes.stream().filter(v -> v.hasEdgeBetween(v1) && v.hasEdgeBetween(v2)).findFirst();
    }

    private double getEdgeSum(ModelGraph graph, InteriorNode interiorNode, Vertex vertex){
        double sum = 0;
        Triplet<Vertex, Vertex, Vertex> triangle = interiorNode.getTriangle();
        Vertex[] nodes = new Vertex[] {triangle.getValue0(), triangle.getValue1(), triangle.getValue2() };
        for(Vertex node: nodes){
            if(node.hasEdgeBetween(vertex)) {
                GraphEdge edge = graph.getEdgeBetweenNodes(node, vertex).orElse(null);
                if (edge != null)
                    sum += edge.getL();
            }
        }
        return sum;
    }

    private Vertex getSplittableNode(ModelGraph graph, InteriorNode interiorNode){
        List<Vertex> candidates = interiorNode.getAssociatedNodes();
        Vertex best_fit = null;
        double best_fit_sum = 0.0;
        for(Vertex candidate: candidates){
            double edge = getEdgeSum(graph, interiorNode, candidate);
            if(edge > best_fit_sum){
                best_fit = candidate;
                best_fit_sum = edge;
            }
        }
        return best_fit;
    }

    private Vertex getNotSplittableNode(ModelGraph graph, InteriorNode interiorNode){
        List<Vertex> candidates = interiorNode.getAssociatedNodes();
        Vertex best_fit = getSplittableNode(graph, interiorNode);
        for(Vertex vertex: candidates){
            if(vertex != best_fit)
                return vertex;
        }
        throw new RuntimeException();
    }

    private Vertex[] triangleToList(Triplet<Vertex, Vertex, Vertex> triangle){
        return new Vertex[] {triangle.getValue0(), triangle.getValue1(), triangle.getValue2() };
    }


    @Override
    public ModelGraph transformGraph(ModelGraph graph, InteriorNode interiorNode) {


        if(this.isConditionCompleted(graph, interiorNode)){


            Vertex v2 = getSplittableNode(graph, interiorNode);
            Vertex v4 = getNotSplittableNode(graph, interiorNode);

            Vertex v3 = graph.getVertexBetween(v2, v4).orElse(null);
            Vertex[] vertex_candidates = triangleToList(interiorNode.getTriangle());
            Vertex v1 = null;
            Vertex v5 = null;
            for(Vertex vertex: vertex_candidates){
                if(vertex != v3 && graph.getEdgeBetweenNodes(v2, vertex).isPresent())
                    v1 = vertex;
                if(vertex != v3 && v4.hasEdgeBetween(vertex))
                    v5 = vertex;
            }

            graph.removeInterior(interiorNode.getId());
            graph.insertEdge(interiorNode.getId() + "e1", v5, v2, false);
            InteriorNode i1 = graph.insertInterior(interiorNode.getId() + "i1", v1, v2, v5);
            InteriorNode i2 = graph.insertInterior(interiorNode.getId() + "i2", v3, v2, v5, v4);
            v2.setVertexType(VertexType.SIMPLE_NODE);
            //change type of node 2
            i1.setPartitionRequired(false);
            i2.setPartitionRequired(false);
        }

        return graph;
    }

    private static int getSimpleVertexCount(Triplet<Vertex, Vertex, Vertex> triangle) {
        int count = 0;
        for (Object o : triangle) {
            Vertex v = (Vertex) o;
            if (v.getVertexType() == VertexType.SIMPLE_NODE) {
                count++;
            }
        }
        return count;
    }


    private static ModelGraph graph() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 100.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.HANGING_NODE, new Point3d(50.0, 50.0, 0.0));
        Vertex v4 = graph.insertVertex("v4", VertexType.SIMPLE_NODE, new Point3d(100.0, 100.0, 0.0));
        Vertex v5 = graph.insertVertex("v5", VertexType.HANGING_NODE, new Point3d(50.0, 100.0, 0.0));
        Vertex v6 = graph.insertVertex("v6", VertexType.SIMPLE_NODE, new Point3d(100.0, 0.0, 0.0));
        Vertex v7 = graph.insertVertex("v7", VertexType.HANGING_NODE, new Point3d(150.0, 50.0, 0.0));
        Vertex v8 = graph.insertVertex("v8", VertexType.SIMPLE_NODE, new Point3d(200.0, 0.0, 0.0));
        Vertex v9 = graph.insertVertex("v9", VertexType.HANGING_NODE, new Point3d(250.0, 50.0, 0.0));
        Vertex v10 = graph.insertVertex("v10", VertexType.SIMPLE_NODE, new Point3d(300.0, 100.0, 0.0));
        Vertex v11 = graph.insertVertex("v11", VertexType.SIMPLE_NODE, new Point3d(300.0, 0.0, 0.0));
        Vertex v12 = graph.insertVertex("v12", VertexType.SIMPLE_NODE, new Point3d(50.0, 150.0, 0.0));

        GraphEdge v8_v11 = graph.insertEdge("e1", v8, v11,  true);
        GraphEdge v4_v10 = graph.insertEdge("e2", v4, v10,  true);
        GraphEdge v11_v10 = graph.insertEdge("e3", v10, v11,  true);
        GraphEdge v6_v8 = graph.insertEdge("e4", v8, v6,  true);
        GraphEdge v2_v6 = graph.insertEdge("e5", v6, v2,  true);
        GraphEdge v1_v2 = graph.insertEdge("e6", v2, v1,  true);
        GraphEdge v1_v12 = graph.insertEdge("e20", v1, v12,  true);
        GraphEdge v4_v12 = graph.insertEdge("e21", v4, v12,  true);

        GraphEdge v1_v3 = graph.insertEdge("e8", v1, v3,  false);
        GraphEdge v3_v6 = graph.insertEdge("e9", v3, v6,  false);
        GraphEdge v2_v3 = graph.insertEdge("e10", v2, v3,  false);
        GraphEdge v4_v7 = graph.insertEdge("e11", v4, v7,  false);
        GraphEdge v1_v5 = graph.insertEdge("e12", v1, v5,  false);
        GraphEdge v6_v7 = graph.insertEdge("e13", v6, v7,  false);
        GraphEdge v7_v8 = graph.insertEdge("e14", v7, v8,  false);
        GraphEdge v8_v9 = graph.insertEdge("e15", v8, v9,  false);
        GraphEdge v9_v10 = graph.insertEdge("e16", v9, v10,  false);
        GraphEdge v9_v11 = graph.insertEdge("e17", v9, v11,  false);
        GraphEdge v5_v4 = graph.insertEdge("e18", v5, v4,  false);
        GraphEdge v5_v12 = graph.insertEdge("e19", v5, v12,  false);
        GraphEdge v6_v4 = graph.insertEdge("e22", v6, v4,  false);

        InteriorNode in1 = graph.insertInterior("i1", v1, v2, v3);
        InteriorNode in2 = graph.insertInterior("i2", v1, v4, v6, v3, v5);
        InteriorNode in3 = graph.insertInterior("i3", v2, v3, v6);
        InteriorNode in6 = graph.insertInterior("i6", v6, v7, v8);
        InteriorNode in7 = graph.insertInterior("i7", v8, v9, v11);
        InteriorNode in8 = graph.insertInterior("i8", v9, v10, v11);
        InteriorNode in9 = graph.insertInterior("i9", v4, v8, v10, v7, v9);
        InteriorNode in10 = graph.insertInterior("i10", v1, v5, v12);
        InteriorNode in11 = graph.insertInterior("i11", v4, v5, v12);
        InteriorNode in12 = graph.insertInterior("i12", v6, v4, v7);
        return graph;
    }

    private static ModelGraph testGraph2() {
        ModelGraph graph = new ModelGraph("testGraph2");

        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0, 0, 0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(50, -50, 0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(100, 0, 0));
        Vertex v4 = graph.insertVertex("v4", VertexType.SIMPLE_NODE, new Point3d(100, 200, 0));
        Vertex v5 = graph.insertVertex("v5", VertexType.SIMPLE_NODE, new Point3d(50, 200, 0));
        Vertex v6 = graph.insertVertex("v6", VertexType.HANGING_NODE, new Point3d(50, 0, 0));
        Vertex v7 = graph.insertVertex("v7", VertexType.HANGING_NODE, new Point3d(75, 100, 0));

        graph.insertEdge("e1", v1, v2, true);
        graph.insertEdge("e2", v2, v3, true);
        graph.insertEdge("e3", v3, v4, true);
        graph.insertEdge("e4", v4, v5, true);
        graph.insertEdge("e5", v5, v1, true);

        graph.insertEdge("e6", v1, v6, false);
        graph.insertEdge("e7", v2, v6, false);
        graph.insertEdge("e8", v3, v6, false);
        graph.insertEdge("e9", v3, v7, false);
        graph.insertEdge("e10", v4, v7, false);
        graph.insertEdge("e11", v5, v7, false);

        graph.insertInterior("i1", v1, v2, v6);
        graph.insertInterior("i2", v2, v3, v6);
        graph.insertInterior("i3", v3, v4, v7);
        graph.insertInterior("i4", v4, v5, v7);
        graph.insertInterior("i5", v3, v5, v1, v6, v7);

        return graph;
    }


    private static ModelGraph graph2() {
        ModelGraph graph = new ModelGraph("testGraph");
        Vertex v1 = graph.insertVertex("v1", VertexType.SIMPLE_NODE, new Point3d(0.0, 100.0, 0.0));
        Vertex v2 = graph.insertVertex("v2", VertexType.SIMPLE_NODE, new Point3d(0.0, 0.0, 0.0));
        Vertex v3 = graph.insertVertex("v3", VertexType.SIMPLE_NODE, new Point3d(100.0, 100.0, 0.0));
        Vertex v4 = graph.insertVertex("v4", VertexType.SIMPLE_NODE, new Point3d(100.0, 0.0, 0.0));

        GraphEdge v2_v3 = graph.insertEdge("v2v3", v2, v3,  false);
        GraphEdge v1_v2 = graph.insertEdge("v1v2", v1, v2,  true);
        GraphEdge v2_v4 = graph.insertEdge("v2v4", v2, v4,  true);
        GraphEdge v3_v4 = graph.insertEdge("v3v4", v3, v4,  true);
        GraphEdge v1_v3 = graph.insertEdge("v1v3", v1, v3,  true);

        InteriorNode in1 = graph.insertInterior("i1", v1, v2, v3);
        InteriorNode in2 = graph.insertInterior("i2", v2, v3, v4);
        in1.setPartitionRequired(true);
        in2.setPartitionRequired(true);
        return graph;
    }

    public static void all_transformation_test(){
        BasicConfigurator.configure();

        ModelGraph graph = graph2();
        List<Transformation> transformations = Arrays.asList(
                new TransformationP1(),
                new TransformationP2(),
                new TransformationP3(),
                new TransformationP4(),
                new TransformationP5(),
                new TransformationP6()
        );
        graph.display();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 4; i++){
            transformations.forEach(t -> {
                List<InteriorNode> interiors = new LinkedList<>(graph.getInteriors());
                interiors.forEach(node -> {
                    if(t.isConditionCompleted(graph, node)) {
                        System.out.println("Available for split " + node.getId() + " " + t.toString());
                        t.transformGraph(graph, node);
                    }
                });
                graph.getInteriors().forEach(node -> {
                    node.setPartitionRequired(true);
                });
            });
        }
    }

    public static void transformation_t4_test() {
        BasicConfigurator.configure();

        ModelGraph graph = graph();
        Transformation t4 = new TransformationP4();

        graph.display();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<InteriorNode> interiors = new LinkedList<>(graph.getInteriors());

        interiors.forEach(node -> {
            if(t4.isConditionCompleted(graph, node))
                System.out.println("Available for split " + node.getId());
            t4.transformGraph(graph, node);
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ModelGraph graph2 = testGraph2();

        graph2.display();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        interiors = new LinkedList<>(graph2.getInteriors());

        interiors.forEach(node -> {
            if(t4.isConditionCompleted(graph2, node))
                System.out.println("Available for split " + node.getId());
            t4.transformGraph(graph2, node);
        });
    }

    public static void main(String[] args) {
        all_transformation_test();
//        transformation_t4_test();

    }
}