package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.interfaces.MDerivativeMask;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmBackwardDifference implements MAlgorithm {
    private MSignalVector data;

    public MAlgorithmBackwardDifference() {
    }

    public MAlgorithmBackwardDifference(MSignalVector data) {
        this.data = data;
    }

    public MAlgorithmBackwardDifference(Parcel in) {
        data = in.readParcelable(MSignalVector.class.getClassLoader());
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
        return new MSignalVector(data.convolve(MDerivativeMask.BACKWARD_DIFFERENCE));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    public static final Parcelable.Creator<MAlgorithmBackwardDifference> CREATOR = new Creator<MAlgorithmBackwardDifference>() {
        @Override
        public MAlgorithmBackwardDifference createFromParcel(Parcel source) {
            return new MAlgorithmBackwardDifference(source);
        }

        @Override
        public MAlgorithmBackwardDifference[] newArray(int size) {
            return new MAlgorithmBackwardDifference[size];
        }
    };
}
