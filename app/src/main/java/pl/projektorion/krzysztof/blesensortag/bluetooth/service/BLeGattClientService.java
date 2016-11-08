package pl.projektorion.krzysztof.blesensortag.bluetooth.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;
import pl.projektorion.krzysztof.blesensortag.utils.CommandExecutor;

public class BLeGattClientService extends Service
    implements BLeGattIO {

    public static final String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.extra.BLE_DEVICE";

    public static final String ACTION_GATT_CONNECTED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.GATT_CONNECTED";

    public static final String ACTION_GATT_CONNECTING =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.GATT_CONNECTING";

    public static final String ACTION_GATT_DISCONNECTED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.GATT_DISCONNECTED";

    public static final String ACTION_GATT_DISCONNECTING =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.GATT_DISCONNECTING";

    public static final String ACTION_GATT_SERVICES_DISCOVERED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.action.GATT_SERVICES_DISCOVERED";

    private Context appContext;

    private BluetoothDevice bleDevice;
    private BluetoothGatt gattClient;
    private BLeGattClientCallback callbacks;

    private Binder binder = new BLeGattClientBinder();
    private LocalBroadcastManager broadcaster;

    private CommandExecutor cmdExecutor;


    /**
     * Bluetooth Low Energy implementation of API callbacks
     */
    private BluetoothGattCallback bleCallbacks = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if( status != BluetoothGatt.GATT_SUCCESS )
            {
                Log.d(Constant.GATT_ERR, String.format(Locale.ENGLISH,
                        Constant.GATT_FAILURE, status));
                return;
            }

            String state;
            switch( newState )
            {
                case BluetoothGatt.STATE_CONNECTED:
                    state = ACTION_GATT_CONNECTED; break;
                case BluetoothGatt.STATE_CONNECTING:
                    state = ACTION_GATT_CONNECTING; break;
                case BluetoothGatt.STATE_DISCONNECTED:
                    state = ACTION_GATT_DISCONNECTED; break;
                case BluetoothGatt.STATE_DISCONNECTING:
                    state = ACTION_GATT_DISCONNECTING; break;
                default: return;
            }

            update_state(state);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if( status == BluetoothGatt.GATT_SUCCESS )
                update_state(ACTION_GATT_SERVICES_DISCOVERED);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if( callbacks != null )
                callbacks.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            if( callbacks != null )
                callbacks.onCharacteristicWrite(gatt, characteristic, status);
            cmdExecutor.nextExecute();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            if( callbacks != null )
                callbacks.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor descriptor, int status) {
            if( callbacks != null )
                callbacks.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            if( callbacks != null )
                callbacks.onDescriptorWrite(gatt, descriptor, status);
            cmdExecutor.nextExecute();
        }
    };


    public BLeGattClientService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init_android_framework();
        init_broadcast_receivers();
        init_objects();
    }

    @Override
    public IBinder onBind(Intent intent) {
        retrieve_intent_data(intent);

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
        gattClient.disconnect();
        super.onDestroy();
    }

    public void connect()
    {
        if( bleDevice == null) {
            Log.d("Service", "No device to connect available");
            return;
        }
        if( gattClient != null ) {
            gattClient.connect();
            return;
        }
        gattClient = bleDevice.connectGatt(appContext, true, bleCallbacks);
    }

    public void disconnect()
    {
        if( gattClient == null )
            return;
        gattClient.disconnect();
    }

    public boolean discoverServices()
    {
        if( gattClient != null )
            return gattClient.discoverServices();
        return false;
    }

    public List<BluetoothGattService> getServices() { return gattClient.getServices(); }

    @Override
    public void add(CommandAbstract cmd) {
        cmdExecutor.add(cmd);
    }

    @Override
    public BluetoothGattService getService(UUID service) { return gattClient.getService(service); }

    @Override
    public boolean setNotificationEnable(BluetoothGattCharacteristic c, boolean state)
    {
        return gattClient.setCharacteristicNotification(c, state);
    }

    @Override
    public boolean writeDescriptor(BluetoothGattDescriptor d) {
        if( gattClient == null )
            return false;
        return gattClient.writeDescriptor(d);
    }

    @Override
    public boolean writeCharacteristic(BluetoothGattCharacteristic c) {
        if( gattClient == null )
            return false;
        return gattClient.writeCharacteristic(c);
    }

    public void setCallbacks(BLeGattClientCallback callbacks)
    {
        this.callbacks = callbacks;
    }

    private void init_android_framework()
    {
        appContext = getApplicationContext();
    }

    private void retrieve_intent_data(Intent intent)
    {
        if( bleDevice == null ) {
            bleDevice = intent.getParcelableExtra(EXTRA_BLE_DEVICE);
            if(bleDevice == null)
                throw new NullPointerException(Constant.NO_INTENT_DATA_ERR);
        }
    }

    private void init_broadcast_receivers()
    {
        broadcaster = LocalBroadcastManager.getInstance(appContext);
    }

    private void init_objects()
    {
        cmdExecutor = new CommandExecutor();
    }

    void update_state(String action)
    {
        final Intent updatedState = new Intent(action);
        broadcaster.sendBroadcast(updatedState);
    }


    public class BLeGattClientBinder extends Binder {
        public BLeGattClientService getService() {
            return BLeGattClientService.this;
        }
    }


}
