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
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureData;

/**
 * A simple {@link Fragment} subclass.
 */
public class IRTemperatureFragment extends Fragment
    implements Observer{

    private View view;
    private TextView labelAmbientTemperature;
    private TextView labelObjectTemperature;

    private Observable observable;
    private Handler handler;

    public IRTemperatureFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        AbstractProfileData temperatureData = (AbstractProfileData) arg;
        final double ambientTemp = temperatureData.getValue(IRTemperatureData.ATTRIBUTE_AMBIENT_TEMPERATURE);
        final double objectTemp = temperatureData.getValue(IRTemperatureData.ATTRIBUTE_OBJECT_TEMPERATURE);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelAmbientTemperature.setText(String.format(Locale.ENGLISH, "%f", ambientTemp));
                labelObjectTemperature.setText(String.format(Locale.ENGLISH, "%f", objectTemp));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_irtemperature, container, false);
        init_widgets();
        handler = new Handler();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init_widgets()
    {
        labelAmbientTemperature = (TextView) view.findViewById(R.id.ambient_temperature_state);
        labelObjectTemperature = (TextView) view.findViewById(R.id.object_temperature_state);
    }

}
