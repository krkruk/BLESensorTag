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
import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.database.DBService;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBTestFragment extends Fragment {

    private View view;
    private Context context;

    private BarometricPressureModel barometricPressureModel;
    private MovementModel movementModel;

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
        BluetoothGattService movementService = new BluetoothGattService(
                MovementProfile.MOVEMENT_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);
        List<BluetoothGattService> serviceList = new ArrayList<>();
        serviceList.add(barometricService);
        serviceList.add(movementService);

        //prepare mock models
        barometricPressureModel = new BarometricPressureModel();
        movementModel = new MovementModel();
        BLeAvailableGattModels models = new BLeAvailableGattModels();
        models.put(BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA, barometricPressureModel);
        models.put(MovementProfile.MOVEMENT_DATA, movementModel);

        //initService
        dbService.initGattServices(serviceList);
        dbService.initDatabase(models);

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

            for(int i=0; i<20; i++)
            {
                barometricPressureModel.updateCharacteristic(barometerCharacteristic);
                movementModel.updateCharacteristic(movementCharacteristic);
            }
            dbService.write();
            Log.i("EXECUTOR", "Data probably added");
        }
    };
}
