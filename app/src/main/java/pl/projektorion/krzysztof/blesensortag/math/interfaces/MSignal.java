package pl.projektorion.krzysztof.blesensortag.math.interfaces;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by krzysztof on 16.01.17.
 */

public interface MSignal extends Parcelable {

    /**
     * Add a value to each element of the list
     * @param value {@link Number} double, value to be added
     */
    void add(Number value);

    /**
     * Subtract a value from each of the elements in the list
     * @param value {@link Number} double, value to be subtracted from each element of the list
     */
    void subtract(Number value);

    /**
     * Multiply all values in the list by a multiplier
     * @param multiplier {@link Number} multiplier
     */
    void multiply(Number multiplier);

    /**
     * Power each value of the list to an exponent
     * @param exponent {@link Number} exponent
     */
    void power(Number exponent);

    /**
     * Convolve the data with a kernel
     * @param kernel {@link List<Double>} kernel
     * @return {@link List} of {@link Double} with a convolved array
     */
    List<Double> convolve(List<Double> kernel);
}
