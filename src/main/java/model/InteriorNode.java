package model;

import org.graphstream.graph.implementations.AbstractGraph;
import org.javatuples.Triplet;

import java.util.*;
import java.util.stream.Collectors;

public class InteriorNode extends GraphNode {

    private static final String INTERIOR_SYMBOL = "I";

    private final Triplet<Vertex, Vertex, Vertex> triangle;

    private final List<Vertex> associatedNodes = new LinkedList<>();

    private boolean R;

    public InteriorNode(AbstractGraph graph, String id, Vertex v1, Vertex v2, Vertex v3) {
        super(graph, id, INTERIOR_SYMBOL, getInteriorPosition(v1, v2, v3));
        triangle = new Triplet<>(v1, v2, v3);
        R = false;
    }

    public InteriorNode(AbstractGraph graph, String id, Vertex v1, Vertex v2, Vertex v3, Vertex... associatedNodes) {
        this(graph, id, v1, v2, v3);
        this.associatedNodes.addAll(Arrays.asList(associatedNodes));
    }

    public Triplet<Vertex, Vertex, Vertex> getTriangle(){
        return triangle;
    }

    public void setPartitionRequired(boolean partitionRequired) {
        R = partitionRequired;
    }

    public boolean isPartitionRequired() {
        return R;
    }

    public Triplet<Vertex, Vertex, Vertex> getTriangleVertexes() {
        return triangle;
    }

    public List<Vertex> getAssociatedNodes() {
        final ModelGraph modelGraph = (ModelGraph) graph;
        final List<Vertex> nodes = new LinkedList<>();
        final Vertex v0 = triangle.getValue0();
        final Vertex v1 = triangle.getValue1();
        final Vertex v2 = triangle.getValue2();
        getAssociatedNode(modelGraph, v0, v1).ifPresent(nodes::add);
        getAssociatedNode(modelGraph, v1, v2).ifPresent(nodes::add);
        getAssociatedNode(modelGraph, v2, v0).ifPresent(nodes::add);
        return nodes;
    }

    private static Optional<Vertex> getAssociatedNode(ModelGraph modelGraph, Vertex v0, Vertex v1) {
        if (v0.hasEdgeBetween(v1)) return Optional.empty();
        return modelGraph
                .getVertexesBetween(v0, v1)
                .stream()
                .min((vertex1, vertex2) -> {
                    final double l1 = modelGraph.getEdgeBetweenNodes(v0, vertex1).get().getL()
                            + modelGraph.getEdgeBetweenNodes(v1, vertex1).get().getL();
                    final double l2 = modelGraph.getEdgeBetweenNodes(v0, vertex2).get().getL()
                            + modelGraph.getEdgeBetweenNodes(v1, vertex2).get().getL();

                    return Math.abs(l1 - l2) < 1e-6 ? 0 : (l1 - l2 > 0 ? 1 : -1);
                }
        );
    }

    private static Point3d getInteriorPosition(Vertex v1, Vertex v2, Vertex v3) {
        return new Point3d(getInteriorXCoordinate(v1, v2, v3), getInteriorYCoordinate(v1, v2, v3), getInteriorZCoordinate(v1, v2, v3));
    }

    private static double getInteriorXCoordinate(Vertex v1, Vertex v2, Vertex v3) {
        return (v1.getXCoordinate() + v2.getXCoordinate() + v3.getXCoordinate()) / 3d;
    }

    private static double getInteriorYCoordinate(Vertex v1, Vertex v2, Vertex v3) {
        return (v1.getYCoordinate() + v2.getYCoordinate() + v3.getYCoordinate()) / 3d;
    }

    private static double getInteriorZCoordinate(Vertex v1, Vertex v2, Vertex v3) {
        return (v1.getZCoordinate() + v2.getZCoordinate() + v3.getZCoordinate()) / 3d;
    }
}
