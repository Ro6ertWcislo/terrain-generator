import model.*;
import org.graphstream.graph.Edge;
import org.junit.Test;
import transformation.Transformation;
import new_transformation.TransformationP4;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NewTransformationP4Test extends AbstractTransformationTest {
    private Transformation transformation = new TransformationP4();

    @Test
    public void testGraphShouldBeCorrectlyTransformed(){
        ModelGraph graph = createTestGraph();
        assertTrue(transformation.isConditionCompleted(graph, graph.getInterior("i2").orElseThrow(AssertionError::new)));
        List<Vertex> vertices = new LinkedList<>(graph.getVertices());
        List<Edge> edges = new LinkedList<>(graph.getEdges());
        List<InteriorNode> interiors = new LinkedList<>(graph.getInteriors());

        Vertex v3BeforeTransform = graph.getVertex("v3").orElseThrow(AssertionError::new);
        assertEquals(VertexType.HANGING_NODE, v3BeforeTransform.getVertexType());

        interiors.forEach(interior -> transformation.transformGraph(graph, interior));

        assertEquals(vertices.size(), graph.getVertices().size());
        assertEquals(interiors.size() + 1, graph.getInteriors().size());
        assertEquals(edges.size() + 4, graph.getEdges().size());

        assertFalse(graph.getInterior("i2").isPresent());
        assertTrue(graph.getInterior("i2i1").isPresent());
        assertTrue(graph.getInterior("i2i2").isPresent());
        assertTrue(graph.getEdgeById("i2e1").isPresent());

        Edge newEdge = graph.getEdgeById("i2e1").orElseThrow(AssertionError::new);
        Edge edgeInGraph = graph.getEdgeBetweenNodes(
                graph.getVertex("v4").orElseThrow(AssertionError::new),
                graph.getVertex("v3").orElseThrow(AssertionError::new)
        ).orElseThrow(AssertionError::new);

        assertEquals(newEdge, edgeInGraph);

        Vertex v3AfterTransform = graph.getVertex("v3").orElseThrow(AssertionError::new);
        assertEquals(VertexType.SIMPLE_NODE, v3AfterTransform.getVertexType());
    }

    private ModelGraph createTestGraph() {
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

    @Test
    public void testGraph2ShouldBeCorrectlyTransformed(){
        ModelGraph graph = createTestGraph2();
        assertTrue(transformation.isConditionCompleted(graph, graph.getInterior("i5").orElseThrow(AssertionError::new)));
        List<Vertex> vertices = new LinkedList<>(graph.getVertices());
        List<Edge> edges = new LinkedList<>(graph.getEdges());
        List<InteriorNode> interiors = new LinkedList<>(graph.getInteriors());

        Vertex v7BeforeTransform = graph.getVertex("v7").orElseThrow(AssertionError::new);
        assertEquals(VertexType.HANGING_NODE, v7BeforeTransform.getVertexType());

        interiors.forEach(interior -> transformation.transformGraph(graph, interior));

        assertEquals(vertices.size(), graph.getVertices().size());
        assertEquals(interiors.size() + 1, graph.getInteriors().size());
        assertEquals(edges.size() + 4, graph.getEdges().size());

        assertFalse(graph.getInterior("i5").isPresent());
        assertTrue(graph.getInterior("i5i1").isPresent());
        assertTrue(graph.getInterior("i5i2").isPresent());
        assertTrue(graph.getEdgeById("i5e1").isPresent());

        Edge newEdge = graph.getEdgeById("i5e1").orElseThrow(AssertionError::new);
        Edge edgeInGraph = graph.getEdgeBetweenNodes(
                graph.getVertex("v1").orElseThrow(AssertionError::new),
                graph.getVertex("v7").orElseThrow(AssertionError::new)
        ).orElseThrow(AssertionError::new);

        assertEquals(newEdge, edgeInGraph);

        Vertex v7AfterTransform = graph.getVertex("v7").orElseThrow(AssertionError::new);
        assertEquals(VertexType.SIMPLE_NODE, v7AfterTransform.getVertexType());
    }

    private ModelGraph createTestGraph2() {
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
}
