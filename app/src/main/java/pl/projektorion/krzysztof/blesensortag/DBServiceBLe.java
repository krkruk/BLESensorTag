package pl.projektorion.krzysztof.blesensortag;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
    public static final String ACTION_STOP_SERVICE =
            "pl.projektorion.krzysztof.blesensortag.action.STOP_SERVICE";

    private static final int NOTIFICATION_ID = 934021;
    private static final int PENDING_ID = 64209;

    private IBinder binder = new DBServiceBLeBinder();
    private Context appContext;
    private BLeGattModelService modelService;
    private LocalBroadcastManager localBroadcastManager;
    private Handler handler = new Handler();
    private Executor writeExecutor = Executors.newSingleThreadExecutor();

    private int dumpDataPeriod = 1000;
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
            if( intent.getAction().equals(BLeGattModelService.ACTION_GATT_MODELS_CREATED) ) {
                if (modelService == null)
                    return;
                setModels(modelService.getModels());
            }
            else if( intent.getAction().equals(ACTION_STOP_SERVICE) )
            {
                stopAsyncWrite();
                stopSelf();
            }
        }
    };

    private Runnable writeCommand = new Runnable() {
        @Override
        public void run() {
            writeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    write();
                    Log.i("WRITE", "Data Written");
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("STARTED", "In started service");
        if( profiles == null || models == null )
            Log.d("DATA", "Required data thru bound service not available");

        Notification notification = create_notification();
        initService();
        startAsyncWrite();
        startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
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
        handler.postDelayed(writeCommand, dumpDataPeriod);
    }

    public void stopAsyncWrite()
    {
        handler.removeCallbacks(writeCommand);
    }

    private void init_service_connection()
    {
        final Intent modelService = new Intent(appContext, BLeGattModelService.class);
        bindService(modelService, modelConn, BIND_AUTO_CREATE);
    }

    private void init_broadcast_receiver()
    {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(BLeGattModelService.ACTION_GATT_MODELS_CREATED);
        filter.addAction(ACTION_STOP_SERVICE);
        localBroadcastManager = LocalBroadcastManager.getInstance(appContext);
        localBroadcastManager.registerReceiver(modelReceiver, filter);
    }

    private void kill_service_connection()
    {
        unbindService(modelConn);
    }

    private void kill_broadcast_receivers()
    {
        localBroadcastManager.unregisterReceiver(modelReceiver);
    }

    private Notification create_notification()
    {
        Notification.Builder builder = new Notification.Builder(appContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notif_text));

        Intent mainActivity = new Intent(appContext, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mainActivity);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(PENDING_ID,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        return builder.build();
    }

    public class DBServiceBLeBinder extends Binder
    {
        public DBServiceBLe getService() {
            return DBServiceBLe.this;
        }
    }
}
