package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

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

    public MAlgorithmPower(Parcel in) {
        this.data = in.readParcelable(MSignalVector.class.getClassLoader());
        this.exponent = in.readDouble();
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
        data.power(exponent);
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeDouble(exponent);
    }

    public static final Parcelable.Creator<MAlgorithmPower> CREATOR = new Creator<MAlgorithmPower>() {
        @Override
        public MAlgorithmPower createFromParcel(Parcel source) {
            return new MAlgorithmPower(source);
        }

        @Override
        public MAlgorithmPower[] newArray(int size) {
            return new MAlgorithmPower[size];
        }
    };
}
