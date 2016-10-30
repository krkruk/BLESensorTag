package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.util.List;

import pl.projektorion.krzysztof.blesensortag.constants.Constant;

public class BLeServiceScannerService extends Service {
    public static final String EXTRA_RESULT_RECEIVER =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.extra.RESULT_RECEIVER";
    public static final String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.extra.BLE_DEVICE";

    public static final String ACTION_BLE_DEVICES_FOUND =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.BLE_DEVICES_FOUND";

    public static final String ACTION_BLE_CONNECTED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.BLE_CONNECTED";

    public static final String ACTION_BLE_DISCONNECTED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.BLE_DISCONNECTED";


    private Binder binder = new BLeServiceScannerBinder();
    private Context appContext;
    private ResultReceiver resultReceiver;
    private LocalBroadcastManager broadcaster;
    private BluetoothDevice bLeDevice;
    private BluetoothGatt bLeGatt;

    private BluetoothGattCallback bLeGattCallbacks = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("Callback", "Connection status changed");
            if( status == BluetoothGatt.GATT_SUCCESS )
            {
                String state = newState == BluetoothGatt.STATE_CONNECTED
                        ? ACTION_BLE_CONNECTED : ACTION_BLE_DISCONNECTED;
                send_update(state);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            send_update(ACTION_BLE_DEVICES_FOUND);
        }
    };

    public class BLeServiceScannerBinder extends Binder {
        public BLeServiceScannerService getService() {
            return BLeServiceScannerService.this;
        }
    }

    public BLeServiceScannerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        retrieve_incoming_data(intent);
        init_local_objects();

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bLeGatt.disconnect();
        return false;
    }

    public void connect()
    {
        bLeGatt = bLeDevice.connectGatt(appContext, true, bLeGattCallbacks);
    }

    public boolean discoverServices()
    {
        return bLeGatt.discoverServices();
    }

    public List<BluetoothGattService> getServices()
    {
        return bLeGatt.getServices();
    }

    private void retrieve_incoming_data(Intent intent) throws NullPointerException
    {
        resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        bLeDevice = intent.getParcelableExtra(EXTRA_BLE_DEVICE);
        if( resultReceiver == null || bLeDevice == null ) {
            Log.e(Constant.BLESSS_ERR, Constant.NO_INTENT_DATA_ERR);
            throw new NullPointerException(Constant.NO_INTENT_DATA_ERR);
        }
    }

    private void init_local_objects()
    {
        appContext = getApplicationContext();
        broadcaster = LocalBroadcastManager.getInstance(appContext);
    }

    private void send_update(String action)
    {
        Intent intent = new Intent(action);
        broadcaster.sendBroadcast(intent);
    }
}
