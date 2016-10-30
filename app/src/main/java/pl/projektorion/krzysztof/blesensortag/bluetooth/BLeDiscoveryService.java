package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import pl.projektorion.krzysztof.blesensortag.R;

public class BLeDiscoveryService extends Service {

    public final static String ACTION_BLE_NOT_SUPPORTED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.BLE_NOT_SUPPORTED";
    public final static String ACTION_BLE_DEVICE_FOUND =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.BLE_DEVICE_FOUND";
    public final static String ACTION_BLE_DISCOVERY_STARTED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.BLE_DISCOVERY_STARTED";
    public final static String ACTION_BLE_DISCOVERY_STOPPED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.BLE_DISCOVERY_STOPPED";

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.extra.BLE_DEVICE_FOUND";
    public final static String EXTRA_BLE_DEVICE_RSSI =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.extra.BLE_DEVICE_FOUND_RSSI";
    public final static String EXTRA_BLE_DEVICE_SCAN_DATA =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.extra.BLE_DEVICE_SCAN_DATA";

    private BluetoothAdapter btAdapter;
    private LocalBroadcastManager localBroadcastManager;
    private Binder binder = new BLeDiscoveryServiceBinder();
    private Handler scanHandler = new Handler(Looper.getMainLooper());
    private boolean isScanning = false;

    //callbacks
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_BLE_DEVICE, device);
            bundle.putInt(EXTRA_BLE_DEVICE_RSSI, rssi);
            bundle.putByteArray(EXTRA_BLE_DEVICE_SCAN_DATA, scanRecord);
            send_local_broadcast(ACTION_BLE_DEVICE_FOUND, bundle);
        }
    };

    private Runnable stopScanning = new Runnable() {
        @Override
        public void run() {
            isScanning = false;
            stopBLeScan(true);
        }
    };

    public class BLeDiscoveryServiceBinder extends Binder {
        public BLeDiscoveryService getService() {
            return BLeDiscoveryService.this;
        }
    }

    public BLeDiscoveryService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        does_handle_ble();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopBLeScan();
        return super.onUnbind(intent);
    }

    /**
     * This function enables BT. It should be called just after
     * the service was connected.
     * @return Returns intent if BT is not enabled, null otherwise.
     */
    public Intent initBluetoothLe()
    {
        BluetoothManager btManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        this.btAdapter = btManager.getAdapter();

        if(btAdapter == null || !btAdapter.isEnabled())
            return new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        return null;
    }

    public boolean startBLeScan(int scanPeriod)
    {
        if(!isScanning) {
            isScanning = true;
            scanHandler.postDelayed(stopScanning, scanPeriod);
            send_local_broadcast(ACTION_BLE_DISCOVERY_STARTED);
            return btAdapter.startLeScan(leScanCallback);
        }

        stopBLeScan(true);
        return false;
    }

    public void stopBLeScan()
    {
        stopBLeScan(false);
    }

    public void stopBLeScan(boolean sendBroadcast)
    {
        isScanning = false;
        if( btAdapter != null ) {
            btAdapter.stopLeScan(leScanCallback);
            if( sendBroadcast )
                send_local_broadcast(ACTION_BLE_DISCOVERY_STOPPED);
        }
        scanHandler.removeCallbacks(stopScanning);
    }

    public boolean isScanning() { return isScanning; }

    private boolean does_handle_ble() {
        if( !getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, R.string.toast_ble_not_supported,
                    Toast.LENGTH_LONG).show();
            send_local_broadcast(ACTION_BLE_NOT_SUPPORTED);
            return false;
        }
        return true;
    }

    private void send_local_broadcast(String action)
    {
        final Intent intent = new Intent(action);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void send_local_broadcast(String action, Bundle data)
    {
        final Intent intent = new Intent(action);
        intent.putExtras(data);
        localBroadcastManager.sendBroadcast(intent);
    }
}
