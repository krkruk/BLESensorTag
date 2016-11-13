package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarometricPressureFragment extends Fragment
    implements Observer {

    private View view;

    private LineChart pressureChart;
    private LineDataSet pressureDataSet;
    private LineData pressureData;
    private Description pressureDescription;
    private long pressureTimeline = MEASUREMENT_COUNT_PER_CHART;

    private LineChart temperatureChart;
    private LineDataSet temperatureDataSet;
    private LineData temperatureData;
    private Description temperatureDescription;
    private long temperatureTimeline = MEASUREMENT_COUNT_PER_CHART;

    private static final int MEASUREMENT_COUNT_PER_CHART = 5;
    private static final float FONT_SIZE =  14.3f;



    private Observable observable;
    private Handler handler;

    public BarometricPressureFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final float pressure = (float)data.getValue(BarometricPressureData.ATTRIBUTE_PRESSURE_hPa);
        final float temperature = (float)data.getValue(BarometricPressureData.ATTRIBUTE_CENTIGRADE);
        update_pressure_data(pressure);
        update_temperature_data(temperature);

        handler.post(new Runnable() {
            @Override
            public void run() {
                pressureDescription = pressureChart.getDescription();
                pressureDescription.setText(value_to_string(pressure,
                        R.string.label_barometric_pressure_unit));
                pressureDescription.setTextSize(FONT_SIZE);

                temperatureDescription = temperatureChart.getDescription();
                temperatureDescription.setText(value_to_string(temperature,
                        R.string.label_temperature_unit));
                temperatureDescription.setTextSize(FONT_SIZE);

                pressureChart.invalidate();
                temperatureChart.invalidate();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_objects();
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

    private void init_objects()
    {
        final String pressureLegend = getString(R.string.label_barometric_pressure) + " "
                + getString(R.string.label_barometric_pressure_unit);

        final String temperatureLegend = getString(R.string.label_temperature) + " "
                + getString(R.string.label_temperature_unit);

        pressureDataSet = new LineDataSet(generate_init_values(), pressureLegend);
        temperatureDataSet = new LineDataSet(generate_init_values(), temperatureLegend);

        pressureData = new LineData(pressureDataSet);
        temperatureData = new LineData(temperatureDataSet);
    }

    private List<Entry> generate_init_values()
    {
        List<Entry> initData = new ArrayList<>();
        for(int i = 0; i<MEASUREMENT_COUNT_PER_CHART; i++) {
            initData.add(new Entry(i, 0));
        }
        return initData;
    }

    private void init_widgets()
    {
        pressureChart = (LineChart) view.findViewById(R.id.pressure_chart);
        temperatureChart = (LineChart) view.findViewById(R.id.temperature_chart);

        init_pressure_chart_properties();
        init_temperature_chart_properties();

        pressureChart.setData(pressureData);
        temperatureChart.setData(temperatureData);
    }

    private void init_pressure_chart_properties()
    {
        pressureDataSet.setColor(Color.BLUE);
        pressureDataSet.setDrawCircles(true);
        pressureDataSet.setDrawCircleHole(false);
        pressureDataSet.setCircleColor(Color.BLUE);
        pressureChart.getAxisRight().setEnabled(false);
        pressureChart.getXAxis().setEnabled(false);
    }

    private void init_temperature_chart_properties()
    {
        temperatureDataSet.setColor(Color.RED);
        temperatureDataSet.setDrawCircleHole(false);
        temperatureDataSet.setDrawCircles(true);
        temperatureDataSet.setCircleColor(Color.RED);
        temperatureChart.getAxisRight().setEnabled(false);
        temperatureChart.getXAxis().setEnabled(false);
    }

    private void update_pressure_data(float pressure)
    {
        pressureDataSet.addEntry(new Entry(pressureTimeline++, pressure));
        pressureDataSet.removeFirst();
        Log.i("Barometr", String.format("Timeline %d, pressure %f", pressureTimeline, pressure));
        pressureDataSet.notifyDataSetChanged();
        pressureData.notifyDataChanged();
        pressureChart.notifyDataSetChanged();
    }

    private void update_temperature_data(float temperature)
    {
        temperatureDataSet.addEntry(new Entry(temperatureTimeline++, temperature));
        temperatureDataSet.removeFirst();
        temperatureDataSet.notifyDataSetChanged();
        temperatureData.notifyDataChanged();
        temperatureChart.notifyDataSetChanged();
    }

    private String value_to_string(float value, int unitResId)
    {
        return String.format(Locale.getDefault(), "%.1f %s",
                value,
                getString(unitResId));
    }
}
