package transformation;

import model.GraphNode;
import model.InteriorNode;
import model.ModelGraph;
import model.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransformationP7 implements Transformation {

    private final Double eps = 5.0;

    @Override
    public boolean isConditionCompleted(ModelGraph graph, InteriorNode interiorNode) {
        List<Vertex> vertices = interiorNode.getAssociatedNodes();
        List<Double> zs = vertices.stream().map(GraphNode::getZCoordinate).collect(Collectors.toList());
        Double maxZ = Collections.max(zs);
        Double minZ = Collections.min(zs);
        return maxZ - minZ > eps;
    }

    @Override
    public ModelGraph transformGraph(ModelGraph graph, InteriorNode interiorNode) {
        interiorNode.setPartitionRequired(true);
        return graph;
    }
}
