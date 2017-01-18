package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

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

    public MAlgorithmMultiply(Parcel in) {
        this.data = in.readParcelable(MSignalVector.class.getClassLoader());
        this.multiplier = in.readDouble();
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
        data.power(multiplier);
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeDouble(multiplier);
    }

    public static final Parcelable.Creator<MAlgorithmMultiply> CREATOR = new Creator<MAlgorithmMultiply>() {
        @Override
        public MAlgorithmMultiply createFromParcel(Parcel source) {
            return new MAlgorithmMultiply(source);
        }

        @Override
        public MAlgorithmMultiply[] newArray(int size) {
            return new MAlgorithmMultiply[size];
        }
    };
}
