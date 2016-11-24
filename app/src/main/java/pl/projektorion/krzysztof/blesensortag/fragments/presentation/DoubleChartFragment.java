package pl.projektorion.krzysztof.blesensortag.fragments.presentation;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class DoubleChartFragment extends Fragment {

    private View view;
    private LineChart upperChart;
    private LineDataSet upperDataSet;
    private LineData upperData;
    private Description upperDescription;
    private TextView upperTitle;
    private long upperTimeline = MAX_MEASUREMENTS_PER_CHART;

    private LineChart lowerChart;
    private LineDataSet lowerDataSet;
    private LineData lowerData;
    private Description lowerDescription;
    private TextView lowerTitle;
    private long lowerTimeline = MAX_MEASUREMENTS_PER_CHART;


    private static final int MAX_MEASUREMENTS_PER_CHART = 5;

    public DoubleChartFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_double_chart, container, false);
        init_objects();
        init_widgets();
        init_values();
        setup_charts();
        update_upper_chart(0.0f);
        update_lower_chart(0.0f);
        return view;
    }

    public void update_upper_chart(float data)
    {
        apply_upper_chart_new_value(data);
        upperDescription = upperChart.getDescription();
        upperDescription.setText(value_to_string(data, get_upper_measure_unit()));
        upperDescription.setTextSize(get_description_font_size());
        upperChart.invalidate();
    }

    public void update_lower_chart(float data)
    {
        apply_lower_chart_new_value(data);
        lowerDescription = lowerChart.getDescription();
        lowerDescription.setText(value_to_string(data, get_lower_measure_unit()));
        lowerDescription.setTextSize(get_description_font_size());
        lowerChart.invalidate();
    }

    private void init_objects()
    {
        upperDataSet = new LineDataSet(generate_init_values(), get_upper_title());
        upperData = new LineData(upperDataSet);

        lowerDataSet = new LineDataSet(generate_init_values(), get_lower_title());
        lowerData = new LineData(lowerDataSet);
    }

    private void init_widgets()
    {
        upperChart = (LineChart) view.findViewById(R.id.upper_chart);
        lowerChart = (LineChart) view.findViewById(R.id.lower_chart);
        upperTitle = (TextView) view.findViewById(R.id.upper_chart_title);
        lowerTitle = (TextView) view.findViewById(R.id.lower_chart_title);
    }

    private void init_values()
    {
        upperTitle.setText(get_upper_title());
        lowerTitle.setText(get_lower_title());
    }

    private void setup_charts()
    {
        setup_upper_chart_properties();
        setup_lower_chart_properties();

        upperChart.setData(upperData);
        lowerChart.setData(lowerData);
    }

    private void apply_upper_chart_new_value(float newValue)
    {
        upperDataSet.addEntry(new Entry(upperTimeline++, newValue));
        upperDataSet.removeFirst();
        upperDataSet.notifyDataSetChanged();
        upperData.notifyDataChanged();
        upperChart.notifyDataSetChanged();
    }

    private void apply_lower_chart_new_value(float newValue)
    {
        lowerDataSet.addEntry(new Entry(lowerTimeline++, newValue));
        lowerDataSet.removeFirst();
        lowerDataSet.notifyDataSetChanged();
        lowerData.notifyDataChanged();
        lowerChart.notifyDataSetChanged();
    }

    private void setup_upper_chart_properties()
    {
        upperDataSet.setColor(get_upper_chart_color());
        upperDataSet.setDrawCircles(true);
        upperDataSet.setDrawCircleHole(false);
        upperDataSet.setCircleColor(get_upper_chart_color());
        upperChart.getAxisRight().setEnabled(false);
        upperChart.getXAxis().setEnabled(false);
    }

    private void setup_lower_chart_properties()
    {
        lowerDataSet.setColor(get_lower_chart_color());
        lowerDataSet.setDrawCircles(true);
        lowerDataSet.setDrawCircleHole(false);
        lowerDataSet.setCircleColor(get_lower_chart_color());
        lowerChart.getAxisRight().setEnabled(false);
        lowerChart.getXAxis().setEnabled(false);
    }

    private List<Entry> generate_init_values()
    {
        List<Entry> data = new ArrayList<>();
        for( int i = 0; i < MAX_MEASUREMENTS_PER_CHART; i++)
            data.add(new Entry(i, 0.0f));
        return data;
    }

    private String value_to_string(float value, String unit)
    {
        return String.format(Locale.getDefault(), "%.1f %s",
                value,
                unit);
    }

    protected abstract String get_upper_title();

    protected abstract String get_lower_title();

    protected abstract int get_upper_chart_color();

    protected abstract int get_lower_chart_color();

    protected abstract String get_upper_measure_unit();

    protected abstract String get_lower_measure_unit();

    protected abstract float get_description_font_size();
}
