package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmSubtract implements MAlgorithm {
    private MSignalVector data;
    private double valueToSubtract;

    public MAlgorithmSubtract(double valueToSubtract) {
        this.data = new MSignalVector();
        this.valueToSubtract = valueToSubtract;
    }

    public MAlgorithmSubtract(MSignalVector data, double valueToSubtract) {
        this.data = data;
        this.valueToSubtract = valueToSubtract;
    }

    public MAlgorithmSubtract(Parcel in) {
        this.data = in.readParcelable(MSignalVector.class.getClassLoader());
        this.valueToSubtract = in.readDouble();
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
        data.subtract(valueToSubtract);
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeDouble(valueToSubtract);
    }

    public static final Parcelable.Creator<MAlgorithmSubtract> CREATOR = new Creator<MAlgorithmSubtract>() {
        @Override
        public MAlgorithmSubtract createFromParcel(Parcel source) {
            return new MAlgorithmSubtract(source);
        }

        @Override
        public MAlgorithmSubtract[] newArray(int size) {
            return new MAlgorithmSubtract[size];
        }
    };
}
