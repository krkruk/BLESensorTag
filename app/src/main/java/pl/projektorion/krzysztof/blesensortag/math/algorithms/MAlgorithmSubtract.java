package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmSubtract implements MAlgorithm {
    private MSignalVector data;
    private double valueToSubtract;

    public MAlgorithmSubtract(double valueToSubtract) {
        this.data = new MSignalVector();
        this.valueToSubtract = valueToSubtract;
    }

    public MAlgorithmSubtract(MSignalVector data, double valueToSubtract) {
        this.data = data;
        this.valueToSubtract = valueToSubtract;
    }

    @Override
    public void setData(MSignalVector data) {
        this.data = data;
    }

    @Override
    public boolean hasData() {
        return data != null && !data.getList().isEmpty();
    }

    @Override
    public MSignalVector compute() {
        data.subtract(valueToSubtract);
        return data;
    }
}
