package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;


import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private static final String ACTION_BULB_STATE =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.OpticalSesnorFragment.action.BULB_STATE";
    private static final String ACTION_LIGHT_INTENSITY =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.OpticalSesnorFragment.action.LIGHT_INTENSITY";

    private View view;
    private TextView labelLightIntensity;
    private ImageView imgLightBulb;

    private Observable observable;
    private Handler handler;

    private boolean bulbState = false;
    private double lightIntensity = 0.0f;

    public OpticalSensorFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        lightIntensity = data.getValue(OpticalSensorData.ATTRIBUTE_LIGHT_INTENSITY_LUX);
        bulbState = lightIntensity > 0;

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelLightIntensity.setText(d_to_str(lightIntensity));
                set_bulb_image(bulbState);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        if(savedInstanceState != null){
            bulbState = savedInstanceState.getBoolean(ACTION_BULB_STATE, false);
            lightIntensity = savedInstanceState.getDouble(ACTION_LIGHT_INTENSITY, 0.0f);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_optical_sensor, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ACTION_BULB_STATE, bulbState);
        outState.putDouble(ACTION_LIGHT_INTENSITY, lightIntensity);
        super.onSaveInstanceState(outState);
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
        imgLightBulb = (ImageView) view.findViewById(R.id.light_bulb_img);

        labelLightIntensity.setText(d_to_str(lightIntensity));
        set_bulb_image(bulbState);
    }

    private void set_bulb_image(boolean bulbOn)
    {
        final int res = bulbOn ? R.drawable.light_bulb_on : R.drawable.light_bulb_off;
        imgLightBulb.setImageResource(res);
    }

    private String d_to_str(double value)
    {
        return String.format(Locale.ENGLISH, "%.2f", value);
    }
}
