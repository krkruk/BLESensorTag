package pl.projektorion.krzysztof.blesensortag.fragments.SensorTag;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovementFragment extends Fragment
    implements Observer {

    private View view;
    private TextView labelGyroscope;
    private TextView labelAccelerometer;
    private TextView labelMagnetometer;

    private Observable observable;
    private Handler handler;

    public MovementFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final double gyroX = data.getValue(MovementData.ATTRIBUTE_GYRO_X);
        final double gyroY = data.getValue(MovementData.ATTRIBUTE_GYRO_Y);
        final double gyroZ = data.getValue(MovementData.ATTRIBUTE_GYRO_Z);
        final double accX = data.getValue(MovementData.ATTRIBUTE_ACC_X);
        final double accY = data.getValue(MovementData.ATTRIBUTE_ACC_Y);
        final double accZ = data.getValue(MovementData.ATTRIBUTE_ACC_Z);
        final double magnetX = data.getValue(MovementData.ATTRIBUTE_MAGNET_X);
        final double magnetY = data.getValue(MovementData.ATTRIBUTE_MAGNET_Y);
        final double magnetZ = data.getValue(MovementData.ATTRIBUTE_MAGNET_Z);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelGyroscope.setText(String.format(Locale.ENGLISH,
                        " %.02f : %.02f : %.02f degrees", gyroX, gyroY, gyroZ));
                labelAccelerometer.setText(String.format(Locale.ENGLISH,
                        " %.02f : %.02f : %.02f G", accX, accY, accZ));
                labelMagnetometer.setText(String.format(Locale.ENGLISH,
                        " %.02f : %.02f : %.02f µT", magnetX, magnetY, magnetZ));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movement, container, false);
        init_widgets();
        handler = new Handler();
        return view;
    }

    @Override
    public void onDestroy() {
        if( observable != null )
            observable.deleteObserver(this);

        super.onDestroy();
    }

    private void init_widgets()
    {
        labelMagnetometer = (TextView) view.findViewById(R.id.magnetometer_state);
        labelGyroscope = (TextView) view.findViewById(R.id.gyroscope_state);
        labelAccelerometer = (TextView) view.findViewById(R.id.accelerometer_state);
    }
}
