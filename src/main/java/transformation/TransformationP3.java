package transformation;

import model.*;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransformationP3 implements Transformation {

    private static final String HANGING_ADJACENT_VERTEX_1_KEY = "hangingAdjacentVertex1";

    private static final String HANGING_ADJACENT_VERTEX_2_KEY = "hangingAdjacentVertex2";

    private static final String VERTEX_MAP_SIMPLE_VERTEX_1_KEY = "simpleVertex1";

    private Map<String, Vertex> model = null;

    private static Map<String, Vertex> mapTriangleVertexesToModel(ModelGraph graph, Triplet<Vertex, Vertex, Vertex> triangle) {
        Map<String, Vertex> triangleModel = new HashMap<>();

        Vertex v1 = triangle.getValue0();
        Vertex v2 = triangle.getValue1();
        Vertex v3 = triangle.getValue2();

        if (hasHangingVertexBetween(graph, v1, v2) == 1) {
            triangleModel.put(HANGING_ADJACENT_VERTEX_1_KEY, v1);
            triangleModel.put(HANGING_ADJACENT_VERTEX_2_KEY, v2);
            triangleModel.put(VERTEX_MAP_SIMPLE_VERTEX_1_KEY, v3);
        } else if(hasHangingVertexBetween(graph, v1, v3) == 1) {
            triangleModel.put(HANGING_ADJACENT_VERTEX_1_KEY, v1);
            triangleModel.put(HANGING_ADJACENT_VERTEX_2_KEY, v3);
            triangleModel.put(VERTEX_MAP_SIMPLE_VERTEX_1_KEY, v2);
        } else {
            triangleModel.put(HANGING_ADJACENT_VERTEX_1_KEY, v2);
            triangleModel.put(HANGING_ADJACENT_VERTEX_2_KEY, v3);
            triangleModel.put(VERTEX_MAP_SIMPLE_VERTEX_1_KEY, v1);
        }
        return triangleModel;
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

    private boolean isGraphValidForTransformation(ModelGraph graph, InteriorNode interiorNode) {
        Triplet<Vertex, Vertex, Vertex> triangle = interiorNode.getTriangleVertexes();
        if (hangingVerticesCount(interiorNode, graph) != 1) return false;
        if (!interiorNode.isPartitionRequired()) {
            return false;
        }
        return getSimpleVertexCount(triangle) == 3;
    }

    @Override
    public boolean isConditionCompleted(ModelGraph graph, InteriorNode interiorNode) {
        if (!isGraphValidForTransformation(graph, interiorNode)) {
            return false;
        }
        Map<String, Vertex> model = mapTriangleVertexesToModel(graph, interiorNode.getTriangleVertexes());
        this.model = model;

        Vertex hangingAdjacentV1 = model.get(HANGING_ADJACENT_VERTEX_1_KEY);
        Vertex hangingAdjacentV2 = model.get(HANGING_ADJACENT_VERTEX_2_KEY);
        Vertex simpleNode1 = model.get(VERTEX_MAP_SIMPLE_VERTEX_1_KEY);
        Vertex hangingNode = getHangingVertexBetween(graph, hangingAdjacentV1, hangingAdjacentV2).get();

        GraphEdge simpleEdge1 = graph.getEdge(simpleNode1, hangingAdjacentV1)
                .orElseThrow(() -> new RuntimeException("Unknown edge id"));
        GraphEdge simpleEdge2 = graph.getEdge(simpleNode1, hangingAdjacentV2)
                .orElseThrow(() -> new RuntimeException("Unknown edge id"));
        GraphEdge hangingEdge1 = graph.getEdge(hangingAdjacentV1, hangingNode)
                .orElseThrow(() -> new RuntimeException("Unknown edge id"));
        GraphEdge hangingEdge2 = graph.getEdge(hangingAdjacentV2, hangingNode)
                .orElseThrow(() -> new RuntimeException("Unknown edge id"));

        double hangingEdgeLen = hangingEdge1.getL() + hangingEdge2.getL();
        return  !(hangingEdgeLen >= simpleEdge1.getL() && hangingEdgeLen >= simpleEdge2.getL());
    }

    private static int hangingVerticesCount(InteriorNode interiorNode, ModelGraph graph){
        Triplet<Vertex, Vertex, Vertex> triangle = interiorNode.getTriangleVertexes();
        return hasHangingVertexBetween(graph, triangle.getValue0(),triangle.getValue1())
                + hasHangingVertexBetween(graph, triangle.getValue1(),triangle.getValue2())
                + hasHangingVertexBetween(graph, triangle.getValue2(),triangle.getValue0());
    }

    private static int hasHangingVertexBetween(ModelGraph graph, Vertex v1, Vertex v2) {
        List<Vertex> vertexesBetween = graph.getVertexesBetween(v1, v2);
        return vertexesBetween.stream().anyMatch(v -> v.getVertexType() == VertexType.HANGING_NODE) ? 1 : 0;
    }

    private static Optional<Vertex> getHangingVertexBetween(ModelGraph graph, Vertex v1, Vertex v2) {
        if (v1.getEdgeBetween(v2) != null) return Optional.empty();

        List<Vertex> between = graph.getVertexesBetween(v1, v2);

        return between.stream().filter(e -> e.getVertexType() == VertexType.HANGING_NODE).findAny();
    }

    private GraphEdge getEdgeForTransformation(ModelGraph graph) {
        Vertex hangingAdjacentV1 = model.get(HANGING_ADJACENT_VERTEX_1_KEY);
        Vertex hangingAdjacentV2 = model.get(HANGING_ADJACENT_VERTEX_2_KEY);
        Vertex simpleNode1 = model.get(VERTEX_MAP_SIMPLE_VERTEX_1_KEY);

        GraphEdge simpleEdge1 = graph.getEdge(simpleNode1, hangingAdjacentV1)
                .orElseThrow(() -> new RuntimeException("Unknown edge id"));
        GraphEdge simpleEdge2 = graph.getEdge(simpleNode1, hangingAdjacentV2)
                .orElseThrow(() -> new RuntimeException("Unknown edge id"));

        if (simpleEdge1.getL() == simpleEdge2.getL()) {
            return simpleEdge1.getB() ? simpleEdge1 : simpleEdge2;
        }
        return simpleEdge1.getL() > simpleEdge2.getL() ? simpleEdge1 : simpleEdge2;
    }

    @Override
    public ModelGraph transformGraph(ModelGraph graph, InteriorNode interiorNode) {
        if(!isConditionCompleted(graph, interiorNode)) {
            return graph;
        }

        Vertex hangingAdjacentV1 = model.get(HANGING_ADJACENT_VERTEX_1_KEY);
        Vertex hangingAdjacentV2 = model.get(HANGING_ADJACENT_VERTEX_2_KEY);
        Vertex simpleNode1 = model.get(VERTEX_MAP_SIMPLE_VERTEX_1_KEY);
        GraphEdge edgeForTransformation = getEdgeForTransformation(graph);

        Pair<GraphNode, GraphNode> edgeNodes = edgeForTransformation.getEdgeNodes();
        Vertex longestEdgeV1;
        Vertex longestEdgeV2;
        Vertex nonlongestEdgeV1;

        if (!hangingAdjacentV1.equals(edgeNodes.getValue0()) && !hangingAdjacentV1.equals(edgeNodes.getValue1())) {
            nonlongestEdgeV1 = hangingAdjacentV1;
            longestEdgeV1 = hangingAdjacentV2;
            longestEdgeV2 = simpleNode1;
        } else {
            nonlongestEdgeV1 = hangingAdjacentV2;
            longestEdgeV1 = hangingAdjacentV1;
            longestEdgeV2 = simpleNode1;
        }

        //transformation process
        graph.removeInterior(interiorNode.getId());
        graph.deleteEdge(edgeForTransformation.getId());

        Vertex insertedVertex = graph.insertVertex(interiorNode.getId(),
                VertexType.HANGING_NODE,
                Point3d.middlePoint(longestEdgeV1.getCoordinates(), longestEdgeV2.getCoordinates()));

        String newEdge1Id = hangingAdjacentV1.getId().concat(insertedVertex.getId());
        String newEdge2Id = hangingAdjacentV2.getId().concat(insertedVertex.getId());
        String newEdge3Id = simpleNode1.getId().concat(insertedVertex.getId());

        GraphEdge insertedEdge1 = graph.insertEdge(newEdge1Id, hangingAdjacentV1, insertedVertex);
        insertedEdge1.setB(edgeForTransformation.getB());

        GraphEdge insertedEdge2 = graph.insertEdge(newEdge2Id, hangingAdjacentV2, insertedVertex);
        insertedEdge2.setB(edgeForTransformation.getB());

        GraphEdge insertedEdge3 = graph.insertEdge(newEdge3Id, simpleNode1, insertedVertex);
        insertedEdge3.setB(false);

        String insertedInterior1Id = insertedVertex.getId().concat(hangingAdjacentV1.getId()).concat(hangingAdjacentV2.getId());
        String insertedInterior2Id = insertedVertex.getId().concat(simpleNode1.getId()).concat(nonlongestEdgeV1.getId());
        graph.insertInterior(insertedInterior1Id, insertedVertex, hangingAdjacentV1, hangingAdjacentV2);
        graph.insertInterior(insertedInterior2Id, insertedVertex, simpleNode1, nonlongestEdgeV1);
        return graph;
    }
}
