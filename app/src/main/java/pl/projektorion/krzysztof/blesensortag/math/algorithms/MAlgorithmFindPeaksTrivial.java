package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;
import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;

/**
 * Created by krzysztof on 18.01.17.
 */

public class MAlgorithmFindPeaksTrivial implements MAlgorithm {

    private MSignalVector data;
    private int window;

    public MAlgorithmFindPeaksTrivial(int window) {
        this.window = window;
    }

    public MAlgorithmFindPeaksTrivial(MSignalVector data, int window) {
        this.data = data;
        this.window = window;
    }

    public MAlgorithmFindPeaksTrivial(Parcel in) {
        data = in.readParcelable(MSignalVector.class.getClassLoader());
        window = in.readInt();
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
        /*
    def find_peaks(numpy_array, window):
        """Get peaks
        @:return list of peaks created by @find_peak"""
        size = len(numpy_array)
        start_at = 0
        end_at = size if size < window else window
        _peaks = []
        while True:
            if start_at == size: break
            _peak = find_peak(numpy_array[start_at:end_at])
            _peaks.append((_peak[0] + start_at, _peak[1]))
            # print("Startat, endat {} : {}".format(start_at,end_at))
            start_at = end_at
            end_at = start_at + window if start_at + window < size else size
        return _peaks
     */
        int size = data.size();
        int startAt = 0;
        int endAt = size < window ? size : window;
        List<Double> peaks = new ArrayList<>();
        List<Double> array = data.getList();
        while (true)
        {
            if( startAt >= size ) break;
            int peak = find_peak(array.subList(startAt, endAt));
            peaks.add((double) (peak + startAt));
            startAt = endAt;
            endAt = startAt + window < size ? startAt + window : size;
        }
        return new MSignalVector(peaks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeInt(window);
    }

    public static int find_peak(List<Double> data)
    {
        /*
        def find_peak(numpy_array):
            """Get a peak.
            @:return array index, value"""
            max_value = sys.float_info.min
            python_array = numpy_array.tolist()
            temp_index = 0
            index = 0
            for elem in python_array:
                index += 1
                max_value = elem if max_value <= elem else max_value
                temp_index = index if max_value == elem else temp_index
            return temp_index-1, max_value
         */
        double maxValue = Double.MIN_VALUE;
        int finalIndex = 0;
        int index = 0;
        for( double elem : data )
        {
            ++index;
            maxValue = maxValue <= elem ? elem : maxValue;
            finalIndex = maxValue == elem ? index : finalIndex;
        }
        return finalIndex - 1;
    }

    public static final Parcelable.Creator<MAlgorithmFindPeaksTrivial> CREATOR = new Creator<MAlgorithmFindPeaksTrivial>() {
        @Override
        public MAlgorithmFindPeaksTrivial createFromParcel(Parcel source) {
            return new MAlgorithmFindPeaksTrivial(source);
        }

        @Override
        public MAlgorithmFindPeaksTrivial[] newArray(int size) {
            return new MAlgorithmFindPeaksTrivial[size];
        }
    };
}
