package transformation;

import model.InteriorNode;
import model.ModelGraph;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransformationP7 implements Transformation {

    private static final double EPSILON = 5.0;

    @Override
    public boolean isConditionCompleted(ModelGraph graph, InteriorNode interiorNode) {
        List<Double> zList = Arrays.asList(interiorNode.getTriangle().getValue0().getZCoordinate(),
                interiorNode.getTriangle().getValue1().getZCoordinate(),
                interiorNode.getTriangle().getValue2().getZCoordinate());

        return Collections.max(zList) - Collections.min(zList) > EPSILON;
    }

    @Override
    public ModelGraph transformGraph(ModelGraph graph, InteriorNode interiorNode) {
        interiorNode.setPartitionRequired(true);
        return graph;
    }
}
