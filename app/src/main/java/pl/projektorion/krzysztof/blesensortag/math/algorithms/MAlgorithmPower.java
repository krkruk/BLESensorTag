package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmPower implements MAlgorithm {
    private MSignalVector data;
    private double exponent;

    public MAlgorithmPower(double exponent) {
        this.data = new MSignalVector();
        this.exponent = exponent;
    }

    public MAlgorithmPower(MSignalVector data, double exponent) {
        this.data = data;
        this.exponent = exponent;
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
        data.power(exponent);
        return data;
    }
}
