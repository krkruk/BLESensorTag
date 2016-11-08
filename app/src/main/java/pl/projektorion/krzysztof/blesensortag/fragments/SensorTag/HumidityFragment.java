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
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class HumidityFragment extends Fragment
    implements Observer{

    private View view;
    private TextView labelTemperature;
    private TextView labelHumidity;

    private Observable observable;
    private Handler handler;

    public HumidityFragment() {
        // Required empty public constructor
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final double temperature = data.getValue(HumidityData.ATTRIBUTE_TEMPERATURE_CELSIUS);
        final double humidity = data.getValue(HumidityData.ATTRIBUTE_RELATIVE_HUMIDITY);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelTemperature.setText(String.format(Locale.ENGLISH, "%.02f", temperature));
                labelHumidity.setText(String.format(Locale.ENGLISH, "%.02f", humidity));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_humidity, container, false);
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
        labelTemperature = (TextView) view.findViewById(R.id.temperature_humid_state);
        labelHumidity = (TextView) view.findViewById(R.id.humidity_state);
    }

}
