package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileData;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.DoubleChartFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HumidityFragment extends DoubleChartFragment
    implements Observer{

    private View view;
    private static final float FONT_SIZE = 14.5f;

    private Observable observable;
    private Handler handler;

    public HumidityFragment() {
        super();
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        if( handler == null ) return;
        final float temperature = (float) data.getValue(HumidityData.ATTRIBUTE_TEMPERATURE_CELSIUS);
        final float humidity = (float) data.getValue(HumidityData.ATTRIBUTE_RELATIVE_HUMIDITY);

        handler.post(new Runnable() {
            @Override
            public void run() {
            update_upper_chart(humidity);
            update_lower_chart(temperature);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_humidity, container, false);
        view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        if( observable != null )
            observable.deleteObserver(this);

        super.onDestroy();
    }

    @Override
    protected String get_upper_title() {
        return getString(R.string.label_humidity);
    }

    @Override
    protected String get_lower_title() {
        return getString(R.string.label_temperature);
    }

    @Override
    protected int get_upper_chart_color() {
        return Color.BLUE;
    }

    @Override
    protected int get_lower_chart_color() {
        return Color.RED;
    }

    @Override
    protected String get_upper_measure_unit() {
        return getString(R.string.label_humidity_unit);
    }

    @Override
    protected String get_lower_measure_unit() {
        return getString(R.string.label_temperature_unit);
    }

    @Override
    protected float get_description_font_size() {
        return FONT_SIZE;
    }
}
