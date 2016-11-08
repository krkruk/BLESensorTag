package pl.projektorion.krzysztof.blesensortag.fragments.SensorTag;


import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpticalSensorFragment extends Fragment
    implements Observer {

    private View view;
    private TextView labelLightIntensity;

    private Observable observable;
    private Handler handler;

    public OpticalSensorFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final double lightIntensity = data.getValue(OpticalSensorData.ATTRIBUTE_LIGHT_INTENSITY_LUX);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelLightIntensity.setText(String.format(Locale.ENGLISH,
                        "%.02f", lightIntensity));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_optical_sensor, container, false);
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
        labelLightIntensity = (TextView) view.findViewById(R.id.light_intensity_status);
    }

}
