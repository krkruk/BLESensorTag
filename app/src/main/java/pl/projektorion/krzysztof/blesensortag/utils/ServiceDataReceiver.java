package pl.projektorion.krzysztof.blesensortag.utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by krzysztof on 30.10.16.
 */

public class ServiceDataReceiver extends ResultReceiver {
    private ReceiverListener listener;

    public interface ReceiverListener {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    public ServiceDataReceiver(Handler handler) {
        super(handler);
    }

    public void setListener(ReceiverListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if( listener!= null )
            listener.onReceiveResult(resultCode, resultData);
    }
}
