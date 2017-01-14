package pl.projektorion.krzysztof.blesensortag.fragments.test;


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattProfiles;
import pl.projektorion.krzysztof.blesensortag.database.DBService;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * Test capabilities of the database. This class should not be used
 * in a release version of the program. However, it boosts debugging.
 * A simple {@link Fragment} subclass.
 */
public class DBTestFragment extends Fragment {

    private View view;
    private Context context;

    private BarometricPressureProfile barometricPressureProfile;
    private HumidityProfile humidityProfile;
    private IRTemperatureProfile irTemperatureProfile;
    private MovementProfile movementProfile;
    private OpticalSensorProfile opticalSensorProfile;

    private BarometricPressureModel barometricPressureModel;
    private HumidityModel humidityModel;
    private IRTemperatureModel irTemperatureModel;
    private MovementModel movementModel;
    private OpticalSensorModel opticalSensorModel;

    private Executor mockDataAdder = Executors.newSingleThreadExecutor();

    private DBService dbService;

    public DBTestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        init_db_connection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbtest, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        context.unbindService(dbConn);
        super.onDestroy();
    }

    private void init_db_connection()
    {
//        context.deleteDatabase(Constant.DB_NAME);
        context.bindService(new Intent(context, DBService.class), dbConn, Context.BIND_AUTO_CREATE);
    }

    private void config_db_service()
    {
        Log.i("DB", "init database");

        //prepare mock services
        BluetoothGattService barometricService = new BluetoothGattService(
                BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattService humidityService = new BluetoothGattService(
                HumidityProfile.HUMIDITY_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattService irTemperatureService = new BluetoothGattService(
            IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
            BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattService movementService = new BluetoothGattService(
            MovementProfile.MOVEMENT_SERVICE,
            BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattService opticalSensorService = new BluetoothGattService(
            OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
            BluetoothGattService.SERVICE_TYPE_PRIMARY);

        List<BluetoothGattService> serviceList = new ArrayList<>();
        serviceList.add(barometricService);
        serviceList.add(humidityService);
        serviceList.add(irTemperatureService);
        serviceList.add(movementService);
        serviceList.add(opticalSensorService);

        //prepare mock profiles
        barometricPressureProfile = new BarometricPressureProfile(mockGattIO);
        humidityProfile = new HumidityProfile(mockGattIO);
        irTemperatureProfile = new IRTemperatureProfile(mockGattIO);
        movementProfile = new MovementProfile(mockGattIO);
        opticalSensorProfile = new OpticalSensorProfile(mockGattIO);

        BLeAvailableGattProfiles profiles = new BLeAvailableGattProfiles();
        profiles.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                barometricPressureProfile);
        profiles.put(HumidityProfile.HUMIDITY_SERVICE, humidityProfile);
        profiles.put(IRTemperatureProfile.IR_TEMPERATURE_SERVICE, irTemperatureProfile);
        profiles.put(MovementProfile.MOVEMENT_SERVICE, movementProfile);
        profiles.put(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE, opticalSensorProfile);

        //prepare mock models
        barometricPressureModel = new BarometricPressureModel();
        humidityModel = new HumidityModel();
        irTemperatureModel = new IRTemperatureModel();
        movementModel = new MovementModel();
        opticalSensorModel = new OpticalSensorModel();

        BLeAvailableGattModels models = new BLeAvailableGattModels();
        models.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA, barometricPressureModel);
        models.put(HumidityProfile.HUMIDITY_DATA, humidityModel);
        models.put(IRTemperatureProfile.IR_TEMPERATURE_DATA, irTemperatureModel);
        models.put(MovementProfile.MOVEMENT_DATA, movementModel);
        models.put(OpticalSensorProfile.OPTICAL_SENSOR_DATA, opticalSensorModel);

        //initService
        dbService.setModels(models);
        dbService.setProfiles(profiles);
        dbService.initService();

        mockDataAdder.execute(mockDataCreator);
    }

    private ServiceConnection dbConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService = ((DBService.DBServiceBinder) service).getService();
            config_db_service();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Runnable mockDataCreator = new Runnable() {
        @Override
        public void run() {
            BluetoothGattCharacteristic barometerCharacteristic =
                    new BluetoothGattCharacteristic(
                            BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA,
                            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                            BluetoothGattCharacteristic.PERMISSION_READ);
            barometerCharacteristic.setValue(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});

            BluetoothGattCharacteristic humidityCharacteristic =
                    new BluetoothGattCharacteristic(
                            HumidityProfile.HUMIDITY_DATA,
                            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                            BluetoothGattCharacteristic.PERMISSION_READ);
            humidityCharacteristic.setValue(new byte[] {1,2,3,4});

            BluetoothGattCharacteristic irTemperatureCharacteristic =
                    new BluetoothGattCharacteristic(
                            IRTemperatureProfile.IR_TEMPERATURE_DATA,
                            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                            BluetoothGattCharacteristic.PERMISSION_READ);
            irTemperatureCharacteristic.setValue(new byte[] {1,2,3,4});

            BluetoothGattCharacteristic movementCharacteristic =
                    new BluetoothGattCharacteristic(
                            MovementProfile.MOVEMENT_DATA,
                            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                            BluetoothGattCharacteristic.PERMISSION_READ);
            movementCharacteristic.setValue(new byte[]{
                    0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                    0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c,
                    0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12
            });

            BluetoothGattCharacteristic opticalSensorCharacteristic =
                    new BluetoothGattCharacteristic(
                            OpticalSensorProfile.OPTICAL_SENSOR_DATA,
                            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                            BluetoothGattCharacteristic.PERMISSION_READ);
            opticalSensorCharacteristic.setValue(new byte[]{4,5});


            for(int i=0; i<20; i++)
            {
                barometricPressureModel.updateCharacteristic(barometerCharacteristic);
                humidityModel.updateCharacteristic(humidityCharacteristic);
                irTemperatureModel.updateCharacteristic(irTemperatureCharacteristic);
                movementModel.updateCharacteristic(movementCharacteristic);
                opticalSensorModel.updateCharacteristic(opticalSensorCharacteristic);
            }

            dbService.write();
            Log.i("EXECUTOR", "Data probably added");
        }
    };

    private BLeGattIO mockGattIO = new BLeGattIO() {
        @Override
        public void addWrite(CommandAbstract cmd) {

        }

        @Override
        public void addRead(CommandAbstract cmd) {

        }

        @Override
        public BluetoothGattService getService(UUID service) {
            return null;
        }

        @Override
        public boolean setNotificationEnable(BluetoothGattCharacteristic c, boolean state) {
            return false;
        }

        @Override
        public boolean writeCharacteristic(BluetoothGattCharacteristic c) {
            return false;
        }

        @Override
        public boolean writeDescriptor(BluetoothGattDescriptor d) {
            return false;
        }

        @Override
        public boolean readCharacteristic(BluetoothGattCharacteristic c) {
            return false;
        }

        @Override
        public boolean readDescriptor(BluetoothGattDescriptor d) {
            return false;
        }
    };
}
