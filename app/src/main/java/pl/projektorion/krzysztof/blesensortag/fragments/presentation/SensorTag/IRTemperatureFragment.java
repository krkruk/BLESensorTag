package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureData;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.DoubleChartFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class IRTemperatureFragment extends DoubleChartFragment
    implements Observer{

    private View view;

    private static final float FONT_SIZE = 14.3f;

    private Observable observable;
    private Handler handler;

    public IRTemperatureFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        AbstractProfileData temperatureData = (AbstractProfileData) arg;
        if( handler == null ) return;
        final float ambientTemp = (float) temperatureData.getValue(IRTemperatureData.ATTRIBUTE_AMBIENT_TEMPERATURE);
        final float objectTemp = (float) temperatureData.getValue(IRTemperatureData.ATTRIBUTE_OBJECT_TEMPERATURE);

        handler.post(new Runnable() {
            @Override
            public void run() {
            update_upper_chart(ambientTemp);
            update_lower_chart(objectTemp);
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
        return getString(R.string.label_ambient_temperature);
    }

    @Override
    protected String get_lower_title() {
        return getString(R.string.label_object_temperature);
    }

    @Override
    protected int get_upper_chart_color() {
        return Color.GREEN;
    }

    @Override
    protected int get_lower_chart_color() {
        return Color.GRAY;
    }

    @Override
    protected String get_upper_measure_unit() {
        return getString(R.string.label_temperature_unit);
    }

    @Override
    protected String get_lower_measure_unit() {
        return get_upper_measure_unit();
    }

    @Override
    protected float get_description_font_size() {
        return FONT_SIZE;
    }
}
