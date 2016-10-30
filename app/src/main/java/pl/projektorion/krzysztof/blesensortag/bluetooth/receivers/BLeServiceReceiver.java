package pl.projektorion.krzysztof.blesensortag.bluetooth.receivers;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by krzysztof on 30.10.16.
 */

public class BLeServiceReceiver extends ResultReceiver {
    private ReceiverListener listener;

    public interface ReceiverListener {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    public BLeServiceReceiver(Handler handler) {
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
