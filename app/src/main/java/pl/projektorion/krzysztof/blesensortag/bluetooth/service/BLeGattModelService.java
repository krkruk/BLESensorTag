package pl.projektorion.krzysztof.blesensortag.bluetooth.service;

import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.factories.BLeDataModelFactory;

public class BLeGattModelService extends Service
    implements BLeGattClientCallback{

    public static final String ACTION_GATT_MODELS_CREATED =
            "pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattModelService.action.MODELS_CREATED";

    private IBinder binder = new BLeGattModelBinder();
    private Context appContext;

    private BLeGattClientService gattClient;
    private LocalBroadcastManager localBroadcastManager;

    private GenericGattModelFactory modelFactory;
    private BLeAvailableGattModels models;

    /**
     * Connection with a bound BLE service.
     */
    private boolean isServiceBound = false;
    private ServiceConnection gattServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gattClient = ((BLeGattClientService.BLeGattClientBinder) service)
                    .getService();
            gattClient.setCallbacks(BLeGattModelService.this);
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    /**
     * Connection with LocalBroadcastReceiver. The callback allows
     * creating a bunch of Gatt Models so these can be therefore handled
     * accordingly.
     */
    private BroadcastReceiver serviceGattReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
            {
                create_and_assign_factory();
                localBroadcastManager.sendBroadcast(new Intent(ACTION_GATT_MODELS_CREATED));
            }
        }
    };


    public BLeGattModelService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init_android_framework();
        init_objects();
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public void onDestroy() {
        kill_bound_services();
        kill_broadcast_receivers();
        super.onDestroy();
    }

    /*
     * Setup started service
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /*
     * Setup bound service
     */

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public GenericGattModelInterface getModel(UUID uuid)
    {
        if( models == null ) return null;
        return models.get(uuid);
    }

    public BLeAvailableGattModels getModels()
    {
        return models;
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
                                     int status) {
        if( status == BluetoothGatt.GATT_SUCCESS )
            update_read_value(characteristic);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic, int status) {
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        final UUID dataChangedUuid = characteristic.getUuid();
        final GenericGattModelInterface observer
                = models.get(dataChangedUuid);

        if(observer != null)
            observer.updateCharacteristic(characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt,
                                 BluetoothGattDescriptor descriptor, int status) {}

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt,
                                  BluetoothGattDescriptor descriptor, int status) {}


    private void create_and_assign_factory()
    {
        List<BluetoothGattService> services = gattClient.getServices();

        for(BluetoothGattService service : services)
        {
            final UUID serviceUuid = service.getUuid();
            final GenericGattModelInterface model = modelFactory.createModel(serviceUuid);
            final UUID lookupUuid = get_lookup_uuid(model, serviceUuid);

            models.put(lookupUuid, model);
        }
    }

    /**
     * Get a proper UUID lookup key.
     * Notifying models require a data UUID due to
     * {@link BLeGattClientCallback#onCharacteristicChanged(BluetoothGatt, BluetoothGattCharacteristic)}
     * Read models however require 1+ data UUID to be identified so only service UUID is passed.
     * In order to assign data in the read model case all records are scanned and compared
     * to the UUIDs of the read model instance.
     * @param model
     * @param defaultUuid
     * @return
     */
    private UUID get_lookup_uuid(GenericGattModelInterface model, UUID defaultUuid)
    {
        if( model instanceof GenericGattNotifyModelInterface) {
            final GenericGattNotifyModelInterface notifyModel =
                    (GenericGattNotifyModelInterface) model;
            defaultUuid = notifyModel.getDataUuid();
        }
        return defaultUuid;
    }

    private void init_android_framework()
    {
        appContext = getApplicationContext();
    }

    private void init_objects()
    {
        modelFactory = new BLeDataModelFactory();
        models = new BLeAvailableGattModels();
    }

    private void init_broadcast_receivers()
    {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        final IntentFilter bleServiceFilter = new IntentFilter(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED);
        localBroadcastManager.registerReceiver(serviceGattReceiver, bleServiceFilter);
    }

    private void init_bound_services()
    {
        final Intent gattServiceReq = new Intent(appContext, BLeGattClientService.class);
        appContext.bindService(gattServiceReq, gattServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void kill_bound_services()
    {
        if( isServiceBound && gattServiceConnection != null )
            appContext.unbindService(gattServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        localBroadcastManager.unregisterReceiver(serviceGattReceiver);
    }

    private void update_read_value(BluetoothGattCharacteristic characteristic)
    {
        for (GenericGattModelInterface model : models.values() ) {
            if (model instanceof GenericGattReadModelInterface
                    && ((GenericGattReadModelInterface)model).hasCharacteristic(characteristic))
                model.updateCharacteristic(characteristic);
        }
    }


    public class BLeGattModelBinder extends Binder {
        public BLeGattModelService getService() {
            return BLeGattModelService.this;
        }
    }
}
