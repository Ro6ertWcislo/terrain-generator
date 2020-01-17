package transformation;

import model.*;
import org.javatuples.Triplet;

import java.util.*;

public class TransformationP7 implements Transformation {

    double EPSILON = 5;

    @Override
    public boolean isConditionCompleted(ModelGraph graph, InteriorNode interiorNode) {
        if (interiorNode.isPartitionRequired()) return false;
        Triplet<Vertex, Vertex, Vertex> triangle = interiorNode.getTriangleVertexes();
        List<Double> zs = Arrays.asList(triangle.getValue0().getZCoordinate(),triangle.getValue0().getZCoordinate(),triangle.getValue0().getZCoordinate());
        double zMin = Collections.min(zs);
        double zMax = Collections.max(zs);

        return zMax - zMin > EPSILON;
    }

    @Override
    public ModelGraph transformGraph(ModelGraph graph, InteriorNode interiorNode) {
        interiorNode.setPartitionRequired(true);

        return graph;
    }
}
