package pl.projektorion.krzysztof.blesensortag.fragments.presentation;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattClientCallback;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattClientService;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GattModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysModelNotifyFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysProfile;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.fragments.BLeFragmentsFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformationObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.GAPServiceObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.BarometricPressureObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.ConnectionControlObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.HumidityObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.IRTemperatureObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.MovementObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.OpticalSensorObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.SimpleKeysObservableFragmentFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLePresentationFragment extends Fragment
    implements BLeGattClientCallback {

    private View view;
    private Context appContext;

    private BLeGattClientService gattClient;
    private LocalBroadcastManager broadcaster;



    private GattModelFactory modelFactory;
    private Map<UUID, GenericGattNotifyModelInterface> gattModels;

    private Map<UUID, GenericGattReadModelInterface> readModels;

    private BLeFragmentsFactory fragmentFactory;

    private UUID currentUuidDisplayed;
    private Fragment currentFragment;

    /**
     * Connection with a bound BLE service.
     */
    private ServiceConnection gattServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gattClient = ((BLeGattClientService.BLeGattClientBinder) service)
                    .getService();
            gattClient.setCallbacks(BLePresentationFragment.this);

            populate_model_notify_factory();
            populate_model_read_factory();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    /**
     * Connection with LocalBroadcastReceiver. The callback allows
     * creating a bunch of Gatt Profiles so these can be therefore handled
     * accordingly.
     */
    private BroadcastReceiver serviceGattReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
            {
                create_and_assign_factory();
                populate_fragment_factory();
            }
        }
    };

    private BroadcastReceiver serviceSelected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String uuid = intent.getStringExtra(BLeServiceScannerFragment.EXTRA_BLE_SERVICE_UUID);
            final UUID serviceUuid = UUID.fromString(uuid);
            negotiate_data_presentation_fragment(serviceUuid);
        }
    };

    public BLePresentationFragment() {
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
        final GenericGattNotifyModelInterface observer;

        observer = gattModels.get(dataChangedUuid);

        if(observer != null)
            observer.updateCharacteristic(characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt,
                                 BluetoothGattDescriptor descriptor, int status) {}

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt,
                                  BluetoothGattDescriptor descriptor, int status) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_android_framework();
        init_objects();
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ble_presentation, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        kill_bound_services();
        kill_broadcast_receivers();
        super.onDestroy();
    }

    private void init_android_framework()
    {
        appContext = getActivity().getApplicationContext();
        setRetainInstance(true);
    }

    private void init_objects()
    {
        modelFactory = new GattModelFactory();
        gattModels = new HashMap<>();
        readModels = new HashMap<>();

        fragmentFactory = new BLeFragmentsFactory();
    }

    private void init_broadcast_receivers()
    {
        IntentFilter serviceFilter = new IntentFilter();
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED);
        IntentFilter serviceSelectedFilter = new IntentFilter();
        serviceSelectedFilter.addAction(BLeServiceScannerFragment.ACTION_BLE_SERVICE_CLICKED);

        broadcaster = LocalBroadcastManager.getInstance(appContext);
        broadcaster.registerReceiver(serviceGattReceiver, serviceFilter);
        broadcaster.registerReceiver(serviceSelected, serviceSelectedFilter);
    }

    private void init_bound_services()
    {
        Intent gattServiceReq = new Intent(appContext, BLeGattClientService.class);
        appContext.bindService(gattServiceReq, gattServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void kill_bound_services()
    {
        appContext.unbindService(gattServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(serviceSelected);
        broadcaster.unregisterReceiver(serviceGattReceiver);
    }

    private void populate_model_notify_factory()
    {
        if( modelFactory == null ){
            Log.d(Constant.BLPF_ERR, Constant.POPULATION_ERR);
            return;
        }

        modelFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE, new SimpleKeysModelNotifyFactory());
        modelFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureModelNotifyFactory());
        modelFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureModelNotifyFactory());
        modelFactory.put(MovementProfile.MOVEMENT_SERVICE, new MovementModelNotifyFactory());
        modelFactory.put(HumidityProfile.HUMIDITY_SERVICE, new HumidityModelNotifyFactory());
        modelFactory.put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorModelNotifyFactory());
    }



    private void populate_model_read_factory()
    {
        readModels.put( GAPServiceReadProfile.GAP_SERVICE, new GAPServiceReadModel() );
        readModels.put( DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE,
                new DeviceInformationReadModel() );
        readModels.put( ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE,
                new ConnectionControlReadModel() );
    }

    private void create_and_assign_factory()
    {
        List<BluetoothGattService> services = gattClient.getServices();

        for(BluetoothGattService service : services)
        {
            final UUID serviceUuid = service.getUuid();
            final GenericGattNotifyModelInterface observer = modelFactory.createObserver(serviceUuid);
            final UUID dataUuid = observer.getDataUuid();
            gattModels.put(dataUuid, observer);
        }
    }

    private void populate_fragment_factory()
    {
        if( fragmentFactory == null ){
            Log.d(Constant.BLPF_ERR, Constant.POPULATION_ERR);
            return;
        }

        Observable simpleKeyModel = (Observable) gattModels.get(SimpleKeysProfile.SIMPLE_KEY_DATA);
        fragmentFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE,
                new SimpleKeysObservableFragmentFactory(simpleKeyModel));

        Observable barometricPressureModel = (Observable) gattModels.get(
                BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA);
        fragmentFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureObservableFragmentFactory(barometricPressureModel));

        Observable irTemperatureModel = (Observable) gattModels.get(
                IRTemperatureProfile.IR_TEMPERATURE_DATA);
        fragmentFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureObservableFragmentFactory(irTemperatureModel));

        Observable movementModel = (Observable) gattModels.get(MovementProfile.MOVEMENT_DATA);
        fragmentFactory.put(MovementProfile.MOVEMENT_SERVICE,
                new MovementObservableFragmentFactory(movementModel));

        Observable humidityModel = (Observable) gattModels.get(HumidityProfile.HUMIDITY_DATA);
        fragmentFactory.put(HumidityProfile.HUMIDITY_SERVICE,
                new HumidityObservableFragmentFactory(humidityModel));

        Observable opticalSensorModel = (Observable)
                gattModels.get(OpticalSensorProfile.OPTICAL_SENSOR_DATA);
        fragmentFactory.put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorObservableFragmentFactory(opticalSensorModel));

        Observable gapServiceModel = (Observable) readModels.get(GAPServiceReadProfile.GAP_SERVICE);
        fragmentFactory.put(GAPServiceReadProfile.GAP_SERVICE,
                new GAPServiceObservableFragmentFactory(gapServiceModel));

        Observable deviceInfoModel = (Observable) readModels.get(
                DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE);
        fragmentFactory.put(DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE,
                new DeviceInformationObservableFragmentFactory(deviceInfoModel));

        Observable connControlModel = (Observable) readModels.get(
                ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE);
        fragmentFactory.put(ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE,
                new ConnectionControlObservableFragmentFactory(connControlModel));
    }

    private void update_read_value(BluetoothGattCharacteristic characteristic)
    {
        for (GenericGattReadModelInterface model : readModels.values() ) {
            if (model.hasCharacteristic(characteristic))
                model.updateCharacteristic(characteristic);
        }
    }

    private void negotiate_data_presentation_fragment(UUID serviceUuid)
    {
        Log.i("Present", "Negotiate fragment");
        if( serviceUuid.equals(currentUuidDisplayed) )
            return;
        Log.i("Present", "man in the middle");
        Fragment fragment = fragmentFactory.create(serviceUuid);
        if( fragment == null )
            return;
        Log.i("Present", "Create a new fragment");
        currentUuidDisplayed = serviceUuid;

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if( currentFragment != null )
            ft.remove(currentFragment);
        ft.replace(R.id.present_ble_data, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        currentFragment = fragment;
    }

}
