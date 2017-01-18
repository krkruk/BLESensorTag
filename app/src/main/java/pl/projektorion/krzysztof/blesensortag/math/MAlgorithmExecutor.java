package pl.projektorion.krzysztof.blesensortag.math;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import pl.projektorion.krzysztof.blesensortag.math.algorithms.MAlgorithmGaussFilter;
import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmExecutor implements Parcelable
{
    public static final int STATUS_SUCCESS = 0x00;
    public static final int STATUS_EMPTY = 0x01;
    public static final int STATUS_ALGORITHM_NO_START_DATA = 0x02;
    public static final int STATUS_NO_LISTENER = 0x03;
    public static final int STATUS_UNDEFINED = 0xff;

    private List<MAlgorithm> algorithms;
    private ResultListener resultListener;
    private MSignalVector initialData;
    private int status = STATUS_UNDEFINED;

    public MAlgorithmExecutor() {
        this.algorithms = new ArrayList<>();
    }

    public MAlgorithmExecutor(Parcel in) {
        Object[] algs = in.readArray(MAlgorithm.class.getClassLoader());
        initialData = in.readParcelable(MSignalVector.class.getClassLoader());
        status = in.readInt();
        algorithms = new ArrayList<>();
        for( Object alg : algs )
            algorithms.add((MAlgorithm) alg);
    }

    /**
     * Set initial data. Calling this method before execute is optional.
     * However, the very first algorithm is presumed to have embedded
     * required data. Otherwise, {@link MAlgorithmExecutor#getStatus()}
     * will return {@value STATUS_ALGORITHM_NO_START_DATA}.
     * @param initialData {@link MSignalVector} vector of data
     */
    public void setInitialData(MSignalVector initialData) {
        this.initialData = initialData;
    }

    public void add(MAlgorithm algorithm)
    {
        algorithms.add( algorithm );
    }

    /**
     * Execute the whole algorithm. Result is passed via \
     * {@link ResultListener#onExecuted(MSignalVector)}
     */
    public void execute()
    {
        status = STATUS_UNDEFINED;
        if( algorithms.isEmpty() ) { status = STATUS_EMPTY; return; }
        MAlgorithm cmd = algorithms.remove(0);
        if( !cmd.hasData() ){
            if( initialData == null ) {
                status = STATUS_ALGORITHM_NO_START_DATA;
                return;
            }
            cmd.setData(initialData);
        }
        MSignalVector data = cmd.compute();
        Iterator iter = algorithms.listIterator();
        while (iter.hasNext())
        {
            cmd = (MAlgorithm) iter.next();
            cmd.setData(data);
            data = cmd.compute();
            iter.remove();
        }
        if( resultListener == null ) { status = STATUS_NO_LISTENER; return; }

        resultListener.onExecuted(data);
        status = STATUS_SUCCESS;
    }

    /**
     * Set listener that handles final result. If none passed,
     * {@link MAlgorithmExecutor#getStatus()} will return
     * {@value STATUS_NO_LISTENER}
     * @param resultListener {@link MSignalVector} final result
     */
    public void setListener(ResultListener resultListener)
    {
        this.resultListener = resultListener;
    }

    /**
     * Get status of the execution. Might ease debugging.
     * @return int - get status based on STATUS_*
     */
    public int getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*
        private List<MAlgorithm> algorithms;
        private ResultListener resultListener;
        private MSignalVector initialData;
        private int status = STATUS_UNDEFINED;
         */
        dest.writeArray(algorithms.toArray());
        dest.writeParcelable(initialData, flags);
        dest.writeInt(status);
    }

    public static class Build {
        private MAlgorithmExecutor executor;

        public Build() {
            this.executor = new MAlgorithmExecutor();
        }

        public Build(ResultListener resultListener) {
            this.executor = new MAlgorithmExecutor();
            this.executor.setListener(resultListener);
        }

        public Build setData(MSignalVector data)
        {
            executor.setInitialData(data);
            return this;
        }

        /**
         * Set one of the available algorithms. The algorithms
         * can be chained. The data is transferred between
         * each individual algorithm so the final result
         * should embrace all of these.
         * @param algorithm {@link MAlgorithm} algorithm
         * @return {@link Build} Builder pattern. See {@link Build#build()}
         */
        public Build setAlgorithm(MAlgorithm algorithm)
        {
            executor.add(algorithm);
            return this;
        }

        /**
         * Set executor listener. See {@link ResultListener#onExecuted(MSignalVector)}
         * @param resultListener {@link ResultListener}
         * @return {@link Build} Builder pattern. See {@link Build#build()}
         */
        public Build setListener(ResultListener resultListener)
        {
            executor.setListener(resultListener);
            return this;
        }

        /**
         * Build final algorithm that can be passed to a working thread
         * @return {@link MAlgorithmExecutor} Object responsible for executing the algorithm
         */
        public MAlgorithmExecutor build()
        {
            return executor;
        }
    }


    public interface ResultListener {
        /**
         * Pass a result of algorithm processing
         * @param result {@link MSignalVector} final algorithm result
         */
        void onExecuted(MSignalVector result);
    }

    public static final Parcelable.Creator<MAlgorithmExecutor> CREATOR = new Creator<MAlgorithmExecutor>() {
        @Override
        public MAlgorithmExecutor createFromParcel(Parcel source) {
            return new MAlgorithmExecutor(source);
        }

        @Override
        public MAlgorithmExecutor[] newArray(int size) {
            return new MAlgorithmExecutor[size];
        }
    };
}
