package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;


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
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarometricPressureFragment extends Fragment
    implements Observer {

    private View view;
    private TextView labelBarometricPressure;
    private TextView labelTemperature;

    private Observable observable;
    private Handler handler;

    public BarometricPressureFragment() {
        // Required empty public constructor
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final double pressure = data.getValue(BarometricPressureData.ATTRIBUTE_PRESSURE_hPa);
        final double temperature = data.getValue(BarometricPressureData.ATTRIBUTE_CENTIGRADE);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelBarometricPressure.setText(String.format(Locale.ENGLISH, "%.2f", pressure));
                labelTemperature.setText(String.format(Locale.ENGLISH, "%.2f", temperature));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_barometric_pressure, container, false);
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
        labelBarometricPressure = (TextView) view.findViewById(R.id.barometric_pressure_state);
        labelTemperature = (TextView) view.findViewById(R.id.temperature_state);
    }

}
