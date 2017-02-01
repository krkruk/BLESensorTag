package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.HeartRate;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate.HeartRateData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeartRateFragment extends Fragment
    implements Observer
{
    private View view;
    private Handler handler;

    private LineChart heartRateChart;
    private LineData heartRateData;
    private LineDataSet heartRateSet;

    private Observable observable;

    private static final float DESCRIPTION_FONT_SIZE = 16.5f;
    private static final int VALUES_PER_CHART = 20;
    private long timeAxis = 0;


    public HeartRateFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_objects();
        Log.e("CREATE", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("CREATE", "onCreateView");
        view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
        init_widgets();
        configure_plot();
        return view;
    }

    @Override
    public void onDestroy() {
        Log.e("HRF", "onDestroy");
        if( observable != null ) observable.deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;

        final double heartRate = ((ProfileData) arg).getValue(HeartRateData.ATTRIBUTE_HEART_RATE);

        handler.post(new Runnable() {
            @Override
            public void run() {
                update_chart(heartRate);
                update_description(heartRate);
                invalidate();
            }
        });
    }

    private void init_objects()
    {
        handler = new Handler();
        heartRateSet = new LineDataSet(generate_init_data(), getString(R.string.label_heart_rate));
        heartRateData = new LineData(heartRateSet);
    }

    private void init_widgets()
    {
        heartRateChart = (LineChart) view.findViewById(R.id.heart_rate_chart);
    }

    private void configure_plot()
    {
        heartRateChart.setData(heartRateData);
        heartRateChart.getDescription().setText(getString(R.string.label_heart_rate));
        heartRateChart.getDescription().setTextSize(DESCRIPTION_FONT_SIZE);

        heartRateChart.getAxisRight().setEnabled(false);
        heartRateChart.getXAxis().setEnabled(false);
        heartRateSet.setDrawCircleHole(false);
        heartRateSet.setDrawCircles(false);
        heartRateSet.setColor(Color.RED);
        heartRateSet.setCircleColor(Color.RED);
        heartRateSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "";
            }
        });
    }

    private void update_chart(double value)
    {
        heartRateSet.removeFirst();
        heartRateSet.addEntry(new Entry(timeAxis++, (float) value));
    }

    private void update_description(double value)
    {
        final String description = String.format(Locale.getDefault(), "%s: %.0f",
                getString(R.string.label_heart_rate),
                value);
        heartRateChart.getDescription().setText(description);
        heartRateChart.getDescription().setTextSize(DESCRIPTION_FONT_SIZE);
    }

    private void invalidate()
    {
        heartRateSet.notifyDataSetChanged();
        heartRateData.notifyDataChanged();
        heartRateChart.notifyDataSetChanged();
        heartRateChart.invalidate();
    }

    private List<Entry> generate_init_data()
    {
        List<Entry> data = new ArrayList<>();
        for(int i = 0; i < VALUES_PER_CHART; i++)
            data.add(new Entry(timeAxis++, 0));
        return data;
    }
}
