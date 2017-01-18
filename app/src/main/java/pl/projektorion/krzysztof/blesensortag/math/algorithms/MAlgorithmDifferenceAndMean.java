package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;
import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;

/**
 * Created by krzysztof on 18.01.17.
 */

public class MAlgorithmDifferenceAndMean implements MAlgorithm {

    private MSignalVector data;
    private double samplingRateMs;

    public MAlgorithmDifferenceAndMean(double samplingRateMs) {
        this.samplingRateMs = samplingRateMs;
    }

    public MAlgorithmDifferenceAndMean(MSignalVector data, double samplingRateMs) {
        this.data = data;
        this.samplingRateMs = samplingRateMs;
    }

    public MAlgorithmDifferenceAndMean(Parcel in) {
        this.data = in.readParcelable(MSignalVector.class.getClassLoader());
        this.samplingRateMs = in.readDouble();
    }

    @Override
    public void setData(MSignalVector data) {
        this.data = data;
    }

    @Override
    public boolean hasData() {
        return data != null && !data.isEmpty();
    }

    /**
     * Compute difference between elements y=x[i+1]-x[i]. Then compute
     * a mean given list of y variables
     * @return {@link MSignalVector} return single value - mean of elements
     */
    @Override
    public MSignalVector compute() {
        List<Double> array = data.toList();
        double t1 = array.remove(0);
        double accumulator = 0.0f;
        int denominator = 0;
        for( double t2 : array ) {
            accumulator += (t2 - t1) * samplingRateMs;
            t1 = t2;
            denominator++;
        }
        return new MSignalVector(Arrays.asList(accumulator / denominator));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeDouble(samplingRateMs);
    }

    public static final Parcelable.Creator<MAlgorithmDifferenceAndMean> CREATOR = new Creator<MAlgorithmDifferenceAndMean>() {
        @Override
        public MAlgorithmDifferenceAndMean createFromParcel(Parcel source) {
            return new MAlgorithmDifferenceAndMean(source);
        }

        @Override
        public MAlgorithmDifferenceAndMean[] newArray(int size) {
            return new MAlgorithmDifferenceAndMean[size];
        }
    };
}
