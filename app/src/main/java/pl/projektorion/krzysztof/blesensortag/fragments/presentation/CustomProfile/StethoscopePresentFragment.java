package pl.projektorion.krzysztof.blesensortag.fragments.presentation.CustomProfile;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile.StethoscopeData;

/**
 * A simple {@link Fragment} subclass.
 */
public class StethoscopePresentFragment extends Fragment
    implements Observer
{

    private Context appContext;
    private View view;
    private Handler handler;
    private LineChart stethoscopeChart;
    private LineDataSet stethoscopeDataSet;
    private LineData stethoscopeData;

    private Observable observable;

    private static final int VALUES_PER_CHART = 3000;
    private static final float DESCRIPTION_FONT_SIZE = 18.5f;
    private long timeAxis = 0;

    public StethoscopePresentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_android_framework();
        init_objects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_present_stethoscope, container, false);
        handler = new Handler();
        init_widgets();
        configure_plot();
        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        StethoscopeData data = (StethoscopeData) arg;
        final double first = data.getValue(StethoscopeData.ATTRIBUTE_FIRST);
        final double second = data.getValue(StethoscopeData.ATTRIBUTE_SECOND);
        handler.post(new Runnable() {
            @Override
            public void run() {
                update_value((float) first);
            }
        });
    }

    private void init_android_framework()
    {
        setRetainInstance(true);
        appContext = getActivity().getApplicationContext();
    }

    private void init_widgets()
    {
        stethoscopeChart = (LineChart) view.findViewById(R.id.stethoscope_chart);
    }

    private void init_objects()
    {
        stethoscopeDataSet = new LineDataSet(generate_init_values(), getString(R.string.label_stethoscope));
        stethoscopeData = new LineData(stethoscopeDataSet);
    }

    private void configure_plot()
    {
        stethoscopeChart.setData(stethoscopeData);
        stethoscopeChart.getDescription().setText(getString(R.string.label_stethoscope));
        stethoscopeChart.getDescription().setTextSize(DESCRIPTION_FONT_SIZE);

        stethoscopeChart.getAxisRight().setEnabled(false);
        stethoscopeChart.getXAxis().setEnabled(false);
        stethoscopeDataSet.setDrawCircleHole(false);
        stethoscopeDataSet.setDrawCircles(false);
        stethoscopeDataSet.setColor(Color.RED);
        stethoscopeDataSet.setCircleColor(Color.RED);
    }

    private void update_value(float value)
    {
        stethoscopeDataSet.addEntry(new Entry(timeAxis++, value));
        stethoscopeDataSet.removeFirst();
        stethoscopeDataSet.notifyDataSetChanged();
        stethoscopeData.notifyDataChanged();
        stethoscopeChart.notifyDataSetChanged();
        stethoscopeChart.invalidate();
    }

    private List<Entry> generate_init_values()
    {
        List<Entry> data = new ArrayList<>();
        for( int i = 0; i < VALUES_PER_CHART; i++ )
            data.add(new Entry(timeAxis++, 0));

        return data;
    }
}
