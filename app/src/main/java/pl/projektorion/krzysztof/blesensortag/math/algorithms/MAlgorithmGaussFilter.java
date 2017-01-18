package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

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

    public MAlgorithmGaussFilter(Parcel in) {
        this.sigma = in.readDouble();
        this.length = in.readInt();
        this.data = in.readParcelable(MSignalVector.class.getClassLoader());
    }

    @Override
    public void setData(MSignalVector data) {
        this.data = data;
    }

    @Override
    public boolean hasData() {
        return data != null && !data.isEmpty();
    }

    @Override
    public MSignalVector compute() {
        return new MSignalVector(data.convolve(MFilter.gaussFilter1D(sigma, length)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(sigma);
        dest.writeInt(length);
        dest.writeParcelable(data, flags);
    }

    public static final Parcelable.Creator<MAlgorithmGaussFilter> CREATOR = new Creator<MAlgorithmGaussFilter>() {
        @Override
        public MAlgorithmGaussFilter createFromParcel(Parcel source) {
            return new MAlgorithmGaussFilter(source);
        }

        @Override
        public MAlgorithmGaussFilter[] newArray(int size) {
            return new MAlgorithmGaussFilter[size];
        }
    };
}
