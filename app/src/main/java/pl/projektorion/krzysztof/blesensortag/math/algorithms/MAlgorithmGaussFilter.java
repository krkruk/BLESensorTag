package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import pl.projektorion.krzysztof.blesensortag.math.MFilter;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;
import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmGaussFilter implements MAlgorithm {
    private MSignalVector data;
    private double sigma;
    private int length;

    public MAlgorithmGaussFilter(double sigma, int length) {
        this.sigma = sigma;
        this.length = length;
    }

    public MAlgorithmGaussFilter(MSignalVector data, double sigma, int length) {
        this.data = data;
        this.sigma = sigma;
        this.length = length;
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
        return new MSignalVector(data.convolve(MFilter.gaussFilter1D(sigma, length)));
    }
}
