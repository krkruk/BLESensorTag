package pl.projektorion.krzysztof.blesensortag;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattModelService;
import pl.projektorion.krzysztof.blesensortag.database.DBService;

/**
 * Created by krzysztof on 11.01.17.
 */

public class DBServiceBLe extends DBService {
    private IBinder binder = new DBServiceBLeBinder();
    private Context appContext;
    private BLeGattModelService modelService;
    private LocalBroadcastManager localBroadcastManager;
    private Handler handler = new Handler();
    private Executor writeExecutor = Executors.newSingleThreadExecutor();

    private int dumpDataPeriod = 1000;
    private boolean isRunning = false;
    private ServiceConnection modelConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            modelService = ((BLeGattModelService.BLeGattModelBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private BroadcastReceiver modelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( !intent.getAction().equals(BLeGattModelService.ACTION_GATT_MODELS_CREATED) )
                return;

            if( modelService == null )
                return;

            setModels( modelService.getModels() );
        }
    };

    private Runnable writeCommand = new Runnable() {
        @Override
        public void run() {
            writeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    write();
                }
            });
            handler.postDelayed(this, dumpDataPeriod);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        appContext = getApplicationContext();
        init_service_connection();
        init_broadcast_receiver();

        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAsyncWrite();
        kill_service_connection();
        kill_broadcast_receivers();
    }

    @Override
    public void initService() throws NullPointerException {
        if( modelService != null ) setModels(modelService.getModels());
        super.initService();
    }

    public void startAsyncWrite()
    {
        if( isEmpty() && !isRunning )
            return;
        isRunning = true;
        handler.postDelayed(writeCommand, dumpDataPeriod);
    }

    public void stopAsyncWrite()
    {
        isRunning = false;
        handler.removeCallbacks(writeCommand);
    }

    private void init_service_connection()
    {
        final Intent modelService = new Intent(appContext, BLeGattModelService.class);
        bindService(modelService, modelConn, BIND_AUTO_CREATE);
    }

    private void init_broadcast_receiver()
    {
        localBroadcastManager = LocalBroadcastManager.getInstance(appContext);
        localBroadcastManager.registerReceiver(modelReceiver,
                new IntentFilter(BLeGattModelService.ACTION_GATT_MODELS_CREATED));
    }

    private void kill_service_connection()
    {
        unbindService(modelConn);
    }

    private void kill_broadcast_receivers()
    {
        localBroadcastManager.unregisterReceiver(modelReceiver);
    }

    public class DBServiceBLeBinder extends Binder
    {
        public DBServiceBLe getService() {
            return DBServiceBLe.this;
        }
    }
}
