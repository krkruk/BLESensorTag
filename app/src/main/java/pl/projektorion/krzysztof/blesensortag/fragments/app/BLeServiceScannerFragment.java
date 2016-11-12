package pl.projektorion.krzysztof.blesensortag.fragments.app;


import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.adapters.BLeServiceScannerAdapterGroupDataContainer;
import pl.projektorion.krzysztof.blesensortag.adapters.BLeServiceScannerExpandableAdapter;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorGenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattClientService;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.fragments.BLeFragmentsFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.BarometricPressureNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.HumidityNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.IRTemperatureNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.MovementNotifyConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.OpticalSensorNotifyFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.SimpleKeysNotifyConfigFragmentFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLeServiceScannerFragment extends Fragment {

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.extra.BLE_DEVICE";


    public final static String ACTION_BLE_SERVICE_CLICKED =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.action.SERVICE_CLICKED";

    public final static String EXTRA_BLE_SERVICE_UUID =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.extra.BLE_SERVICE_UUID";


    private BluetoothDevice bleDevice;
    private BLeGattClientService gattService;

    private View view;
    private Context appContext;
    private ExpandableListView serviceWidgetExpandableList;
    private BLeServiceScannerExpandableAdapter serviceWidgetExpandableAdapter;

    final private Handler handler = new Handler(Looper.getMainLooper());
    private LocalBroadcastManager broadcaster;

    private GenericGattProfileFactory profileFactory;
    private Map<UUID, GenericGattProfileInterface> gattProfiles;
    private BLeFragmentsFactory configFragFactory;

    private BroadcastReceiver serviceGattReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeGattClientService.ACTION_GATT_CONNECTED.equals(action))
            {
                display_status(R.string.status_connected);
                populate_profile_notify_factory();
                populate_profile_read_factory();

                gattService.discoverServices();
            }
            else if(BLeGattClientService.ACTION_GATT_CONNECTING.equals(action))
            {
                display_status(R.string.status_connecting);
            }
            else if(BLeGattClientService.ACTION_GATT_DISCONNECTED.equals(action))
            {
                display_status(R.string.status_disconnected);
            }
            else if(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
            {
                serviceWidgetExpandableAdapter.clear();
                final List<BluetoothGattService> services = gattService.getServices();
                create_profile_factories(services);
                populate_config_fragment_factory();
                enable_all_notifications();
                enable_all_measurements();
                populate_adapter();
            }
        }
    };

    private ServiceConnection gattServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gattService = ((BLeGattClientService.BLeGattClientBinder) service)
                    .getService();
            gattService.connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private ExpandableListView.OnGroupExpandListener expandListener =
            new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;
        @Override
        public void onGroupExpand(int groupPosition) {
            BLeServiceScannerAdapterGroupDataContainer data =
                    (BLeServiceScannerAdapterGroupDataContainer)
                            serviceWidgetExpandableAdapter.getGroup(groupPosition);
            if( (previousGroup != -1) && (groupPosition != previousGroup) )
                serviceWidgetExpandableList.collapseGroup(previousGroup);
            previousGroup = groupPosition;

            final UUID uuid = data.getServiceUuid();
            Intent bleServiceClicked = new Intent(ACTION_BLE_SERVICE_CLICKED);
            bleServiceClicked.putExtra(EXTRA_BLE_SERVICE_UUID,
                    uuid.toString());
            broadcaster.sendBroadcast(bleServiceClicked);
            demand_read_values(uuid);
        }
    };


    public BLeServiceScannerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init_android_framework();
        retrieve_incoming_data();
        assert_ble_device_exists();
        init_adapters();
        init_objects();
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ble_service_scanner, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onDestroy() {
        kill_bound_services();
        kill_broadcast_receivers();
        super.onDestroy();
    }

    public static BLeServiceScannerFragment newInstance(BluetoothDevice device) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_BLE_DEVICE, device);
        BLeServiceScannerFragment fragment = new BLeServiceScannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Populate GattProfileFactory with ProfileFactories.
     * Each ProfileNotifyFactory will createProfile a profile based on Service Key passed
     * into a map.
     */
    private void populate_profile_notify_factory()
    {
        if( profileFactory == null ) {
            Log.d(Constant.BLPF_ERR, Constant.POPULATION_ERR);
            return;
        }

        profileFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE,
                new SimpleKeysGenericProfileFactory(gattService));
        profileFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureGenericProfileFactory(gattService));
        profileFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureGenericProfileFactory(gattService));
        profileFactory.put(MovementProfile.MOVEMENT_SERVICE,
                new MovementGenericProfileFactory(gattService));
        profileFactory.put(HumidityProfile.HUMIDITY_SERVICE,
                new HumidityGenericProfileFactory(gattService));
        profileFactory.put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorGenericProfileFactory(gattService));
    }

    private void populate_profile_read_factory()
    {
        profileFactory.put( GAPServiceReadProfile.GAP_SERVICE, new GAPServiceGenericProfileFactory(gattService) );
        profileFactory.put( DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE,
                new DeviceInformationGenericProfileFactory(gattService) );
        profileFactory.put(ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE,
                new ConnectionControlGenericProfileFactory(gattService));
    }

    private void create_profile_factories(List<BluetoothGattService> services)
    {
        for(BluetoothGattService service : services)
        {
            final UUID serviceUuid = service.getUuid();
            final GenericGattProfileInterface profile = profileFactory
                    .createProfile(serviceUuid);

            gattProfiles.put(serviceUuid, profile);
        }
    }

    private void populate_config_fragment_factory()
    {
        GenericGattProfileInterface buffer
                = gattProfiles.get(SimpleKeysProfile.SIMPLE_KEY_SERVICE);
        configFragFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE,
                new SimpleKeysNotifyConfigFragmentFactory(buffer));

        buffer = gattProfiles.get(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE);
        configFragFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureNotifyConfigFragmentFactory(buffer));

        buffer = gattProfiles.get(IRTemperatureProfile.IR_TEMPERATURE_SERVICE);
        configFragFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureNotifyConfigFragmentFactory(buffer));

        buffer = gattProfiles.get(MovementProfile.MOVEMENT_SERVICE);
        configFragFactory.put(MovementProfile.MOVEMENT_SERVICE,
                new MovementNotifyConfigFragmentFactory(buffer));

        buffer = gattProfiles.get(HumidityProfile.HUMIDITY_SERVICE);
        configFragFactory.put(HumidityProfile.HUMIDITY_SERVICE,
                new HumidityNotifyConfigFragmentFactory(buffer));

        buffer = gattProfiles.get(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE);
        configFragFactory.put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorNotifyFragmentFactory(buffer));
    }

    private void populate_adapter()
    {
        Map<BLeServiceScannerAdapterGroupDataContainer, Fragment> dataEntries
                = new LinkedHashMap<>();

        for(UUID uuid : gattProfiles.keySet())
        {
            final GenericGattProfileInterface profile = gattProfiles.get(uuid);
            final String name = profile.getName();
            if( !name.equals("") ) {
                final BLeServiceScannerAdapterGroupDataContainer data =
                        new BLeServiceScannerAdapterGroupDataContainer(name, uuid);
                dataEntries.put(data, configFragFactory.create(uuid));
            }
        }
        serviceWidgetExpandableAdapter.extend(dataEntries);
        serviceWidgetExpandableAdapter.notifyDataSetChanged();
    }

    private void enable_all_notifications()
    {
        for(GenericGattProfileInterface profile : gattProfiles.values())
            if( profile instanceof NotifyGattProfileInterface )
                ((NotifyGattProfileInterface)profile).enableNotification(true);
    }

    private void enable_all_measurements()
    {
        for(GenericGattProfileInterface profile : gattProfiles.values())
            if( profile instanceof NotifyGattProfileInterface )
                ((NotifyGattProfileInterface)profile)
                        .enableMeasurement(
                                NotifyGattProfileInterface.ENABLE_ALL_MEASUREMENTS);
    }

    private void init_android_framework()
    {
        setRetainInstance(true);
        this.appContext = getActivity().getApplicationContext();
    }

    private void retrieve_incoming_data()
    {
        Bundle bundle = getArguments();
        bleDevice = bundle.getParcelable(EXTRA_BLE_DEVICE);
    }

    private void assert_ble_device_exists()
    {
        if( bleDevice == null ) {
            final Activity activity = getActivity();
            activity.finish();
            Toast.makeText(appContext,
                    R.string.toast_ble_service_scan_device_not_passed,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void init_adapters()
    {
        serviceWidgetExpandableAdapter = new BLeServiceScannerExpandableAdapter(appContext,
                null, getFragmentManager());
    }

    private void init_objects()
    {
        profileFactory = new GenericGattProfileFactory();
        gattProfiles = new LinkedHashMap<>();
        configFragFactory = new BLeFragmentsFactory();
    }

    private void init_broadcast_receivers()
    {
        IntentFilter serviceFilter = new IntentFilter();
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED);
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_CONNECTED);
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_CONNECTING);
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_DISCONNECTED);

        broadcaster = LocalBroadcastManager.getInstance(appContext);
        broadcaster.registerReceiver(serviceGattReceiver, serviceFilter);
    }

    private void init_bound_services()
    {
        Intent initGatt = new Intent(appContext, BLeGattClientService.class);
        initGatt.putExtra(BLeGattClientService.EXTRA_BLE_DEVICE, bleDevice);
        appContext.bindService(initGatt, gattServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void init_widgets()
    {
        serviceWidgetExpandableList = (ExpandableListView) view.findViewById(R.id.listview_device_services);
        serviceWidgetExpandableList.setAdapter(serviceWidgetExpandableAdapter);
        serviceWidgetExpandableList.setOnGroupExpandListener(expandListener);
    }

    private void kill_bound_services()
    {
        appContext.unbindService(gattServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(serviceGattReceiver);
    }

    private void demand_read_values(UUID valueUuid)
    {
        for( GenericGattProfileInterface profile : gattProfiles.values() )
        {
            if( profile instanceof GenericGattReadProfileInterface
            && profile.isService(valueUuid) ) {
                ((GenericGattReadProfileInterface)profile)
                        .demandReadCharacteristics(GenericGattReadProfileInterface.ATTRIBUTE_ALL);
            }
        }
    }

    private void display_status(int resId)
    {
        final ActionBar actionBar = getActivity().getActionBar();
        final int timeout = 2000;
        try {
            actionBar.setTitle(resId);
        } catch (NullPointerException e)
        {
            return;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actionBar.setTitle(R.string.app_name);
            }
        }, timeout);
    }
}
