package pl.projektorion.krzysztof.blesensortag;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

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

    private ServiceConnection modelConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            modelService = ((BLeGattModelService.BLeGattModelBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        appContext = getApplicationContext();
        init_service_connection();
        return binder;
    }

    private void init_service_connection()
    {
        final Intent modelService = new Intent(appContext, BLeGattModelService.class);
        bindService(modelService, modelConn, BIND_AUTO_CREATE);
    }

    public class DBServiceBLeBinder extends Binder
    {
        public DBServiceBLe getService() {
            return DBServiceBLe.this;
        }
    }
}
