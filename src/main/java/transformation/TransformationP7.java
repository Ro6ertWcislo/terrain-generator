package transformation;

import model.InteriorNode;
import model.ModelGraph;
import model.Vertex;
import org.javatuples.Triplet;

public class TransformationP7 implements Transformation {

    private static final double epsilon = 5.0;

    @Override
    public boolean isConditionCompleted(ModelGraph graph, InteriorNode interiorNode) {
        if (interiorNode.isPartitionRequired()) {
            return false;
        }
        Triplet<Vertex, Vertex, Vertex> triangle = interiorNode.getTriangleVertexes();
        double z0 = triangle.getValue0().getZCoordinate();
        double z1 = triangle.getValue1().getZCoordinate();
        double z2 = triangle.getValue2().getZCoordinate();

        double zMax = Math.max(z0, Math.max(z1, z2));
        double zMin = Math.min(z0, Math.min(z1, z2));

        return (zMax - zMin) > epsilon;
    }

    @Override
    public ModelGraph transformGraph(ModelGraph graph, InteriorNode interiorNode) {
        if(!isConditionCompleted(graph, interiorNode)) {
            return graph;
        }
        interiorNode.setPartitionRequired(true);
        return graph;
    }
}
