package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmAdd implements MAlgorithm {
    private MSignalVector data;
    private double valueToAdd;

    public MAlgorithmAdd(double valueToAdd) {
        this.data = new MSignalVector();
        this.valueToAdd = valueToAdd;
    }

    public MAlgorithmAdd(MSignalVector data, double valueToAdd) {
        this.data = data;
        this.valueToAdd = valueToAdd;
    }

    public MAlgorithmAdd(Parcel in) {
        data = in.readParcelable(MSignalVector.class.getClassLoader());
        valueToAdd = in.readDouble();
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
        data.add(valueToAdd);
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeDouble(valueToAdd);
    }

    public static final Parcelable.Creator<MAlgorithmAdd> CREATOR = new Creator<MAlgorithmAdd>() {
        @Override
        public MAlgorithmAdd createFromParcel(Parcel source) {
            return new MAlgorithmAdd(source);
        }

        @Override
        public MAlgorithmAdd[] newArray(int size) {
            return new MAlgorithmAdd[size];
        }
    };
}
