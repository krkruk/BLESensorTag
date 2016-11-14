package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureData;

/**
 * A simple {@link Fragment} subclass.
 */
public class IRTemperatureFragment extends Fragment
    implements Observer{

    private View view;
    private LineChart ambientTempChart;
    private LineDataSet ambientDataSet;
    private LineData ambientData;
    private Description ambientDescription;
    private long ambientTimeline = MAX_MEASUREMENTS_PER_CHART;

    private LineChart objectTempChart;
    private LineDataSet objectDataSet;
    private LineData objectData;
    private Description objectDescription;
    private long objectTimeline = MAX_MEASUREMENTS_PER_CHART;

    private static final int MAX_MEASUREMENTS_PER_CHART = 5;
    private static final float FONT_SIZE = 14.3f;

    private Observable observable;
    private Handler handler;

    public IRTemperatureFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        AbstractProfileData temperatureData = (AbstractProfileData) arg;
        final float ambientTemp = (float) temperatureData.getValue(IRTemperatureData.ATTRIBUTE_AMBIENT_TEMPERATURE);
        final float objectTemp = (float) temperatureData.getValue(IRTemperatureData.ATTRIBUTE_OBJECT_TEMPERATURE);

        update_ambient_temp_data(ambientTemp);
        update_object_temp_data(objectTemp);

        handler.post(new Runnable() {
            @Override
            public void run() {

            ambientDescription = ambientTempChart.getDescription();
            ambientDescription.setText(value_to_string(ambientTemp,
                R.string.label_barometric_pressure_unit));
            ambientDescription.setTextSize(FONT_SIZE);

            objectDescription = objectTempChart.getDescription();
            objectDescription.setText(value_to_string(objectTemp,
                    R.string.label_temperature_unit));
            objectDescription.setTextSize(FONT_SIZE);

            ambientTempChart.invalidate();
            objectTempChart.invalidate();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_irtemperature, container, false);
        init_objects();
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

    private void update_ambient_temp_data(float data)
    {
        ambientDataSet.addEntry(new Entry(ambientTimeline++, data));
        ambientDataSet.removeFirst();
        ambientDataSet.notifyDataSetChanged();
        ambientData.notifyDataChanged();
        ambientTempChart.notifyDataSetChanged();
    }

    private void update_object_temp_data(float data)
    {
        objectDataSet.addEntry(new Entry(objectTimeline++, data));
        objectDataSet.removeFirst();
        objectDataSet.notifyDataSetChanged();
        objectData.notifyDataChanged();
        objectTempChart.notifyDataSetChanged();
    }

    private void init_objects()
    {
        final String ambientLabel = getString(R.string.label_ambient_temperature);
        ambientDataSet = new LineDataSet(gen_init_values(), ambientLabel);

        final String objectLabel = getString(R.string.label_object_temperature);
        objectDataSet = new LineDataSet(gen_init_values(), objectLabel);

        ambientData = new LineData(ambientDataSet);
        objectData = new LineData(objectDataSet);
    }

    private List<Entry> gen_init_values()
    {
        List<Entry> data = new ArrayList<>();
        for(int i = 0; i < MAX_MEASUREMENTS_PER_CHART; i++)
            data.add(new Entry(i, 0.0f));
        return data;
    }

    private void init_widgets()
    {
        ambientTempChart = (LineChart) view.findViewById(R.id.ambient_temperature_chart);
        objectTempChart = (LineChart) view.findViewById(R.id.object_temperature_chart);

        init_ambient_chart_properties();
        init_object_chart_properties();

        ambientTempChart.setData(ambientData);
        objectTempChart.setData(objectData);
    }

    private void init_ambient_chart_properties()
    {
        ambientDataSet.setColor(Color.GREEN);
        ambientDataSet.setDrawCircles(true);
        ambientDataSet.setDrawCircleHole(false);
        ambientDataSet.setCircleColor(Color.GREEN);
        ambientTempChart.getAxisRight().setEnabled(false);
        ambientTempChart.getXAxis().setEnabled(false);
    }

    private void init_object_chart_properties()
    {
        objectDataSet.setColor(Color.GRAY);
        objectDataSet.setDrawCircles(true);
        objectDataSet.setDrawCircleHole(false);
        objectDataSet.setCircleColor(Color.GRAY);
        objectTempChart.getAxisRight().setEnabled(false);
        objectTempChart.getXAxis().setEnabled(false);
    }

    private String value_to_string(float value, int unitResId)
    {
        return String.format(Locale.getDefault(), "%.1f %s",
                value,
                getString(unitResId));
    }
}
