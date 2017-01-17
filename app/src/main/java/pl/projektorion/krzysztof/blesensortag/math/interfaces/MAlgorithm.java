package pl.projektorion.krzysztof.blesensortag.math.interfaces;

import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public interface MAlgorithm {

    /**
     * Set data by the executor. Data can be also set in a constructor of the child
     * @param data {@link MSignalVector} data to be computed
     */
    void setData(MSignalVector data);

    /**
     * Has data? Utilized by {@link pl.projektorion.krzysztof.blesensortag.math.MAlgorithmExecutor}
     * @return boolean
     */
    boolean hasData();

    /**
     * Compute the data by the implemented algorithm
     * @return {@link MSignalVector} computed value
     */
    MSignalVector compute();
}
