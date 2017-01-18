package pl.projektorion.krzysztof.blesensortag.math;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;


public class MComputeService extends IntentService
    implements MAlgorithmExecutor.ResultListener
{

    public static final String EXTRA_ALGORITHM_EXECUTOR =
            "pl.projektorion.krzysztof.blesensortag.math.ALGORITHM_EXECUTOR";

    public static final String EXTRA_DATA_RECEIVER =
            "pl.projektorion.krzysztof.blesensortag.math.DATA_RECEIVER";

    public static final String EXTRA_RESULT_DATA =
            "pl.projektorion.krzysztof.blesensortag.math.RESULT_DATA";

    public static final String EXTRA_RESULT_CODE =
            "pl.projektorion.krzysztof.blesensortag.math.RESULT_CODE";

    public static final int DEFAULT_RESULT_CODE = 666;

    public MComputeService() {
        super("MComputeService");
    }

    private MAlgorithmExecutor executor;
    private ResultReceiver receiver;
    private int resultCode;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executor = intent.getParcelableExtra(EXTRA_ALGORITHM_EXECUTOR);
        receiver = intent.getParcelableExtra(EXTRA_DATA_RECEIVER);
        resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, DEFAULT_RESULT_CODE);
        if( executor == null || receiver == null )
            throw new NullPointerException("Haven't received enough data thru Intent! Abort");
        executor.setListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        executor.execute();
    }

    @Override
    public synchronized void onExecuted(MSignalVector result) {
        Log.i("onExec", "executed");
        final Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RESULT_DATA, result);
        receiver.send(resultCode, bundle);
    }

    public static ServiceDataReceiver createServiceReceiver(Handler handler)
    {
        return new ServiceDataReceiver(handler);
    }
}
