package pl.projektorion.krzysztof.blesensortag.fragments.presentation;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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


import java.util.Observable;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlReadProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattModelService;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.fragments.BLeFragmentsFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.DeviceInformationObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.GAPService.GAPServiceObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.BarometricPressure.BarometricPressureObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.ConnectionControl.ConnectionControlObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.Humidity.HumidityObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.IRTemperature.IRTemperatureObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.Movement.MovementObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.OpticalSensor.OpticalSensorObservableFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.SimpleKeys.SimpleKeysObservableFragmentFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLePresentationFragment extends Fragment {

    private View view;
    private Context appContext;

    private BLeGattModelService gattModelService;
    private LocalBroadcastManager broadcaster;

    private BLeFragmentsFactory fragmentFactory;

    private UUID currentUuidDisplayed;
    private Fragment currentFragment;

    /**
     * Connection with a bound BLE model service.
     */
    private ServiceConnection gattModelServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gattModelService = ((BLeGattModelService.BLeGattModelBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            gattModelService = null;
        }
    };

    /**
     * Connection with LocalBroadcastReceiver. The callback allows
     * creating a bunch of Gatt Fragments so these can be therefore displayed.
     * The action is triggered after all models are created.
     */
    private BroadcastReceiver serviceModelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeGattModelService.ACTION_GATT_MODELS_CREATED.equals(action))
            {
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
        fragmentFactory = new BLeFragmentsFactory();
    }

    private void init_broadcast_receivers()
    {
        IntentFilter serviceModelFilter = new IntentFilter();
        serviceModelFilter.addAction(BLeGattModelService.ACTION_GATT_MODELS_CREATED);
        IntentFilter serviceSelectedFilter = new IntentFilter();
        serviceSelectedFilter.addAction(BLeServiceScannerFragment.ACTION_BLE_SERVICE_CLICKED);

        broadcaster = LocalBroadcastManager.getInstance(appContext);
        broadcaster.registerReceiver(serviceModelReceiver, serviceModelFilter);
        broadcaster.registerReceiver(serviceSelected, serviceSelectedFilter);
    }

    private void init_bound_services()
    {
        final Intent gattModelServiceReq = new Intent(appContext, BLeGattModelService.class);
        appContext.bindService(gattModelServiceReq, gattModelServiceConnection, 
                Context.BIND_AUTO_CREATE);
    }

    private void kill_bound_services()
    {
        appContext.unbindService(gattModelServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(serviceSelected);
        broadcaster.unregisterReceiver(serviceModelReceiver);
    }

    private void populate_fragment_factory()
    {
        if( fragmentFactory == null ){
            Log.d(Constant.BLPF_ERR, Constant.POPULATION_ERR);
            return;
        }

        Observable simpleKeyModel = (Observable) gattModelService.getModel(SimpleKeysProfile.SIMPLE_KEY_DATA);
        fragmentFactory.put(SimpleKeysProfile.SIMPLE_KEY_SERVICE,
                new SimpleKeysObservableFragmentFactory(simpleKeyModel));

        Observable barometricPressureModel = (Observable) gattModelService.getModel(
                BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA);
        fragmentFactory.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new BarometricPressureObservableFragmentFactory(barometricPressureModel));

        Observable irTemperatureModel = (Observable) gattModelService.getModel(
                IRTemperatureProfile.IR_TEMPERATURE_DATA);
        fragmentFactory.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new IRTemperatureObservableFragmentFactory(irTemperatureModel));

        Observable movementModel = (Observable) gattModelService.getModel(MovementProfile.MOVEMENT_DATA);
        fragmentFactory.put(MovementProfile.MOVEMENT_SERVICE,
                new MovementObservableFragmentFactory(movementModel));

        Observable humidityModel = (Observable) gattModelService.getModel(HumidityProfile.HUMIDITY_DATA);
        fragmentFactory.put(HumidityProfile.HUMIDITY_SERVICE,
                new HumidityObservableFragmentFactory(humidityModel));

        Observable opticalSensorModel = (Observable)
                gattModelService.getModel(OpticalSensorProfile.OPTICAL_SENSOR_DATA);
        fragmentFactory.put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new OpticalSensorObservableFragmentFactory(opticalSensorModel));

        Observable gapServiceModel = (Observable) gattModelService.getModel(GAPServiceReadProfile.GAP_SERVICE);
        fragmentFactory.put(GAPServiceReadProfile.GAP_SERVICE,
                new GAPServiceObservableFragmentFactory(gapServiceModel));

        Observable deviceInfoModel = (Observable) gattModelService.getModel(
                DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE);
        fragmentFactory.put(DeviceInformationReadProfile.DEVICE_INFORMATION_SERVICE,
                new DeviceInformationObservableFragmentFactory(deviceInfoModel));

        Observable connControlModel = (Observable) gattModelService.getModel(
                ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE);
        fragmentFactory.put(ConnectionControlReadProfile.CONNECTION_CONTROL_SERVICE,
                new ConnectionControlObservableFragmentFactory(connControlModel));
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
