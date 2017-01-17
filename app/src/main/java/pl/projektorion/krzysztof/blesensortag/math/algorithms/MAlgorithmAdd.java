package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmAdd implements MAlgorithm {
    private MSignalVector data;
    private double valueToAdd;

    public MAlgorithmAdd(double valueToAdd) {
        this.data = new MSignalVector();
        this.valueToAdd = valueToAdd;
    }

    public MAlgorithmAdd(MSignalVector data, double valueToAdd) {
        this.data = data;
        this.valueToAdd = valueToAdd;
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
        data.add(valueToAdd);
        return data;
    }
}
