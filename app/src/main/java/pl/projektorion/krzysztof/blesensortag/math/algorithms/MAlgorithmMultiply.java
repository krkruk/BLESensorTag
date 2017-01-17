package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmMultiply implements MAlgorithm {
    private MSignalVector data;
    private double multiplier;

    public MAlgorithmMultiply(double multiplier) {
        this.data = new MSignalVector();
        this.multiplier = multiplier;
    }

    public MAlgorithmMultiply(MSignalVector data, double multiplier) {
        this.data = data;
        this.multiplier = multiplier;
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
        data.power(multiplier);
        return data;
    }
}
