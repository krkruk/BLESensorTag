package pl.projektorion.krzysztof.blesensortag.fragments;


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
import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattClientCallback;
import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattClientService;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.GattModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.GattProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.SimpleKeys.SimpleKeysModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.SimpleKeys.SimpleKeysProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.SimpleKeys.SimpleKeysProfile;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.fragments.SensorTag.BarometricPressureFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.SensorTag.HumidityFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.SensorTag.IRTemperatureFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.SensorTag.MovementFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.SensorTag.SensorTagFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.SensorTag.SimpleKeysFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLePresentationFragment extends Fragment
    implements BLeGattClientCallback {

    private View view;
    private Context appContext;

    private BLeGattClientService gattClient;
    private LocalBroadcastManager broadcaster;


    private GattProfileFactory profileFactory;
    private GattModelFactory modelFactory;
    private Map<UUID, GenericGattProfileInterface> gattProfiles;
    private Map<UUID, GenericGattObserverInterface> gattModels;

    private SensorTagFragmentFactory fragmentFactory;

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

            populate_profile_factory();
            populate_model_factory();
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
                enable_all_notifications();
                enable_all_measurements();
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
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic, int status) {
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        final UUID dataChangedUuid = characteristic.getUuid();
        final GenericGattObserverInterface observer;

        try {
            observer = gattModels.get(dataChangedUuid);
        } catch (NullPointerException|ClassCastException e) { return; }

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
        profileFactory = new GattProfileFactory();
        modelFactory = new GattModelFactory();
        gattProfiles = new HashMap<>();
        gattModels = new HashMap<>();

        fragmentFactory = new SensorTagFragmentFactory();
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

    /**
     * Populate GattProfileFactory with ProfileFactories.
     * Each ProfileFactory will createProfile a profile based on Service Key passed
     * into a map.
     */
    private void populate_profile_factory()
    {
        if( profileFactory == null ) {
            Log.d(Constant.BLPF_ERR, Constant.POPULATION_ERR);
            return;
        }

        profileFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE, new SimpleKeysProfileFactory(gattClient));
        profileFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureProfileFactory(gattClient));
        profileFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureProfileFactory(gattClient));
        profileFactory.put(MovementProfile.MOVEMENT_SERVICE, new MovementProfileFactory(gattClient));
        profileFactory.put(HumidityProfile.HUMIDITY_SERVICE, new HumidityProfileFactory(gattClient));
    }

    private void populate_model_factory()
    {
        if( modelFactory == null ){
            Log.d(Constant.BLPF_ERR, Constant.POPULATION_ERR);
            return;
        }

        modelFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE, new SimpleKeysModelFactory());
        modelFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureModelFactory());
        modelFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureModelFactory());
        modelFactory.put(MovementProfile.MOVEMENT_SERVICE, new MovementModelFactory());
        modelFactory.put(HumidityProfile.HUMIDITY_SERVICE, new HumidityModelFactory());
    }

    private void create_and_assign_factory()
    {
        List<BluetoothGattService> services = gattClient.getServices();

        for(BluetoothGattService service : services)
        {
            final UUID serviceUuid = service.getUuid();
            final GenericGattProfileInterface profile = profileFactory.createProfile(serviceUuid);
            final GenericGattObserverInterface observer = modelFactory.createObserver(serviceUuid);
            final UUID dataUuid = observer.getDataUuid();
            gattProfiles.put(dataUuid, profile);
            gattModels.put(dataUuid, observer);
        }
    }

    private void enable_all_notifications()
    {
        for(GenericGattProfileInterface profile : gattProfiles.values())
            profile.enableNotification(true);
    }

    private void enable_all_measurements()
    {
        for(GenericGattProfileInterface profile : gattProfiles.values())
            profile.enableMeasurement(GenericGattProfileInterface.ENABLE_ALL_MEASUREMENTS);
    }

    private void populate_fragment_factory()
    {
        if( fragmentFactory == null ){
            Log.d(Constant.BLPF_ERR, Constant.POPULATION_ERR);
            return;
        }

        Observable simpleKeyModel = (Observable) gattModels.get(SimpleKeysProfile.SIMPLE_KEY_DATA);
        fragmentFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE,
                new SimpleKeysFragmentFactory(simpleKeyModel));

        Observable barometricPressureModel = (Observable) gattModels.get(
                BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA);
        fragmentFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureFragmentFactory(barometricPressureModel));

        Observable irTemperatureModel = (Observable) gattModels.get(
                IRTemperatureProfile.IR_TEMPERATURE_DATA);
        fragmentFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureFragmentFactory(irTemperatureModel));

        Observable movementModel = (Observable) gattModels.get(MovementProfile.MOVEMENT_DATA);
        fragmentFactory.put(MovementProfile.MOVEMENT_SERVICE,
                new MovementFragmentFactory(movementModel));

        Observable humidityModel = (Observable) gattModels.get(HumidityProfile.HUMIDITY_DATA);
        fragmentFactory.put(HumidityProfile.HUMIDITY_SERVICE,
                new HumidityFragmentFactory(humidityModel));
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
