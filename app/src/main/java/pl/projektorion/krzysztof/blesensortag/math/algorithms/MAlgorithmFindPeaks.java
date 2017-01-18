package pl.projektorion.krzysztof.blesensortag.math.algorithms;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;
import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;

/**
 * Created by krzysztof on 18.01.17.
 */

public class MAlgorithmFindPeaks implements MAlgorithm {

    private MSignalVector data;
    private int peakAtExpectRate;

    public MAlgorithmFindPeaks(int peakAtExpectRate) {
        this.peakAtExpectRate = peakAtExpectRate;
    }

    public MAlgorithmFindPeaks(MSignalVector data, int peakAtExpectRate) {
        this.data = data;
        this.peakAtExpectRate = peakAtExpectRate;
    }

    public MAlgorithmFindPeaks(Parcel in) {
        data = in.readParcelable(MSignalVector.class.getClassLoader());
        peakAtExpectRate = in.readInt();
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
        def find_peaks_enhanced2 (numpy_array, peak_expect_rate):
            size = len(numpy_array)
            window = int(peak_expect_rate // 2)
            half_window = window // 2
            _peaks = []

            highest_peak = find_peak(numpy_array)
            print("Highest Peak", highest_peak)
            presumed_peaks = compute_potential_peak_location(int(highest_peak[0]), int(peak_expect_rate), size)
            for _peak in presumed_peaks:
                start_at = 0 if _peak - half_window < 0 else _peak - half_window
                end_at = size - 1 if _peak + half_window >= size else _peak + half_window
                print("Startat: {}, endat {}".format(start_at, end_at))
                _found_peak = find_peak(numpy_array[start_at:end_at])
                _peaks.append((_found_peak[0] + start_at, _found_peak[1]))
            return _peaks
     */
        final int size = data.size();
        final int window = Math.round(peakAtExpectRate / 2);
        final int halfWindow = Math.round(window / 2);
        List<Double> peaks = new ArrayList<>();
        List<Double> array = data.toList();
        int highestPeak = find_peak(array);
        List<Integer> potentialPeaks = compute_potential_peaks(highestPeak);
        for(int potentialPeak : potentialPeaks)
        {
            final int startAt = potentialPeak - halfWindow < 0 ? 0 : potentialPeak - halfWindow;
            final int endAt = potentialPeak + halfWindow >= size ? size - 1 : potentialPeak + halfWindow;
            final int peakFound = find_peak(array.subList(startAt, endAt));
            peaks.add((double) (peakFound + startAt));
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
        dest.writeInt(peakAtExpectRate);
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
            finalIndex = maxValue <= elem ? index : finalIndex;
        }
        return finalIndex - 1;
    }

    private List<Integer> compute_potential_peaks(int highestPeak)
    {
        /*
        def compute_potential_peak_location(highest_peak, peak_expect_rate, array_size):
            potential_peaks = []
            negatives = [neg_peak for neg_peak in range(highest_peak, 0, -peak_expect_rate)]
            positives = [pos_peak for pos_peak in range(highest_peak+peak_expect_rate, array_size, peak_expect_rate)]
            potential_peaks.extend(negatives)
            potential_peaks.extend(positives)
            potential_peaks.sort()
            print("Potential peaks:", potential_peaks)
            return potential_peaks
         */
        final int size = data.size();
        List<Integer> potential_peaks = new ArrayList<>();
        for( int negatives = highestPeak; negatives > 0; negatives -= peakAtExpectRate)
            potential_peaks.add(negatives);
        for( int positives = highestPeak + peakAtExpectRate; positives < size; positives += peakAtExpectRate)
            potential_peaks.add(positives);
        Collections.sort(potential_peaks);
        return potential_peaks;
    }

    public static final Parcelable.Creator<MAlgorithmFindPeaks> CREATOR = new Creator<MAlgorithmFindPeaks>() {
        @Override
        public MAlgorithmFindPeaks createFromParcel(Parcel source) {
            return new MAlgorithmFindPeaks(source);
        }

        @Override
        public MAlgorithmFindPeaks[] newArray(int size) {
            return new MAlgorithmFindPeaks[size];
        }
    };
}
