package pl.projektorion.krzysztof.blesensortag.math;

import java.util.concurrent.ConcurrentLinkedQueue;

import pl.projektorion.krzysztof.blesensortag.math.interfaces.MAlgorithm;

/**
 * Created by krzysztof on 17.01.17.
 */

public class MAlgorithmExecutor extends ConcurrentLinkedQueue<MAlgorithm>
{
    public static final int STATUS_SUCCESS = 0x00;
    public static final int STATUS_EMPTY = 0x01;
    public static final int STATUS_ALGORITHM_NO_START_DATA = 0x02;
    public static final int STATUS_NO_LISTENER = 0x03;
    public static final int STATUS_UNDEFINED = 0xff;

    private ResultListener resultListener;
    private int status = STATUS_UNDEFINED;

    /**
     * Execute the whole algorithm. Result is passed via \
     * {@link ResultListener#onExecuted(MSignalVector)}
     */
    public void execute()
    {
        status = STATUS_UNDEFINED;
        if( isEmpty() ) { status = STATUS_EMPTY; return; }
        MAlgorithm cmd = poll();
        if( !cmd.hasData() ){ status = STATUS_ALGORITHM_NO_START_DATA; return; }
        MSignalVector data = cmd.compute();

        while (!isEmpty())
        {
            cmd = poll();
            cmd.setData(data);
            data = cmd.compute();
        }
        if( resultListener == null ) { status = STATUS_NO_LISTENER; return; }

        resultListener.onExecuted(data);
        status = STATUS_SUCCESS;
    }

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

    public static class Build {
        private MAlgorithmExecutor executor;

        public Build() {
            this.executor = new MAlgorithmExecutor();
        }

        public Build(ResultListener resultListener) {
            this.executor = new MAlgorithmExecutor();
            this.executor.setListener(resultListener);
        }

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
}
