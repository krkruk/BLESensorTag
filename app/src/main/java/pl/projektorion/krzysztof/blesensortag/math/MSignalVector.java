package pl.projektorion.krzysztof.blesensortag.math;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MSignal;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MSignalVector implements MSignal {
    private List<Double> dataVector;

    public MSignalVector() {
        this.dataVector = new ArrayList<>();
    }

    public MSignalVector(List<Double> dataVector) {
        this.dataVector = dataVector;
    }

    public MSignalVector(Collection<Double> dataVector) {
        this.dataVector = new ArrayList<>(dataVector);
    }

    public MSignalVector(Parcel in)
    {
        final int array_size = in.readInt();
        double[] array = new double[array_size];
        in.readDoubleArray(array);
        Double[] d_array = new Double[array_size];
        int index = 0;
        for( double elem : array ) d_array[index++] = elem;
        dataVector = new ArrayList<>(Arrays.asList(d_array));
    }

    @Override
    public List<Double> convolve(List<Double> kernel) {
        return MConvolve.convolve(dataVector, kernel);
    }

    public List<Double> convolve(MSignalVector kernel)
    {
        return MConvolve.convolve(dataVector, kernel.dataVector);
    }

    public int size()
    {
        return dataVector.size();
    }

    @Override
    public void add(Number value) {
        for (int i = 0; i < dataVector.size(); i++) {
            Double n = dataVector.get(i);
            n += value.doubleValue();
            dataVector.set(i, n);
        }
    }

    @Override
    public void subtract(Number value) {
        for (int i = 0; i < dataVector.size(); i++) {
            Double n = dataVector.get(i);
            n -= value.doubleValue();
            dataVector.set(i, n);
        }
    }

    @Override
    public void multiply(Number multiplier) {
        for (int i = 0; i < dataVector.size(); i++) {
            Double n = dataVector.get(i);
            n *= multiplier.doubleValue();
            dataVector.set(i, n);
        }
    }

    @Override
    public void power(Number exponent) {
        for (int i = 0; i < dataVector.size(); i++) {
            Double n = dataVector.get(i);
            n = Math.pow(n, exponent.doubleValue());
            dataVector.set(i, n);
        }
    }

    public boolean isEmpty() { return dataVector.isEmpty(); }

    public List<Double> toList()
    {
        return dataVector;
    }

    public List<Integer> toInteger()
    {
        List<Integer> integers = new ArrayList<>();
        for(double value : dataVector)
            integers.add( (int) value );
        return integers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dataVector.size());
        double[] array = new double[dataVector.size()];
        int index = 0;
        for(Object obj : dataVector.toArray())
            array[index++] = (double) obj;
        dest.writeDoubleArray(array);
    }

    @Override
    public String toString() {
        return "MSignalVector{" +
                "dataVector=" + dataVector +
                '}';
    }

    public static final Parcelable.Creator<MSignalVector> CREATOR = new Creator<MSignalVector>() {
        @Override
        public MSignalVector createFromParcel(Parcel source) {
            return new MSignalVector(source);
        }

        @Override
        public MSignalVector[] newArray(int size) {
            return new MSignalVector[size];
        }
    };
}
