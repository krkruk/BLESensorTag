package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;
import pl.projektorion.krzysztof.blesensortag.math.interfaces.MDerivativeMask;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmCentralDifference implements MAlgorithm {
    private MSignalVector data;

    public MAlgorithmCentralDifference() {
    }

    public MAlgorithmCentralDifference(MSignalVector data) {
        this.data = data;
    }

    public MAlgorithmCentralDifference(Parcel in) {
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
        return new MSignalVector(data.convolve(MDerivativeMask.CENTRAL_DIFFERENCE));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    public static final Parcelable.Creator<MAlgorithmCentralDifference> CREATOR = new Creator<MAlgorithmCentralDifference>() {
        @Override
        public MAlgorithmCentralDifference createFromParcel(Parcel source) {
            return new MAlgorithmCentralDifference(source);
        }

        @Override
        public MAlgorithmCentralDifference[] newArray(int size) {
            return new MAlgorithmCentralDifference[size];
        }
    };
}
