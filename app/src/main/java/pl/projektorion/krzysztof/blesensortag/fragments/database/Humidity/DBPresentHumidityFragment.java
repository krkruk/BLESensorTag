package pl.projektorion.krzysztof.blesensortag.fragments.database.Humidity;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Humidity.DBSelectHumidity;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Humidity.DBSelectHumidityCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Humidity.DBSelectHumidityCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Humidity.DBSelectHumidityData;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentHumidityFragment extends DBPresentSensorFragmentAbstract {

    private View view;
    private LineChart chart;
    private LineData lineData;
    private LineDataSet humiditySet;
    private LineDataSet temperatureSet;

    private ServiceDataReceiver.ReceiverListener dataListener = new ServiceDataReceiver.ReceiverListener() {
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
            {
                List<? extends DBSelectInterface> data = resultData
                        .getParcelableArrayList(DBSelectIntentService.EXTRA_RESULT);
                if( data == null ) return;
                apply_data(data);
            }
        }
    };

    public DBPresentHumidityFragment() {
    }

    public static DBPresentHumidityFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentHumidityFragment fragment = new DBPresentHumidityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_humidity, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        chart.setOnChartGestureListener(super.getFlingListener());
    }

    @Override
    public DBQueryParcelableListenerInterface getExportQuery() {
        return new DBSelectHumidity(rootRecord, sensorRecord);
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance()
    {
        return new DBSelectHumidityCount(rootRecord);
    }

    @Override
    protected int attribute_count()
    {
        return DBSelectHumidityCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver()
    {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(dataListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance()
    {
        DBQueryWithLimitsListenerInterface query = new DBSelectHumidity(rootRecord, sensorRecord);
        query.setLimit(super.getStartAt(), super.getMaxRecordsPerLoad());
        return query;
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data)
    {
        List<Entry> humidityData = new ArrayList<>();
        List<Entry> temperatureData = new ArrayList<>();

        for(DBSelectInterface entry : data)
        {
            final long time = (long) entry.getData(DBSelectHumidityData.ATTRIBUTE_TIME);
            final double relativeHumid = (double) entry.getData(DBSelectHumidityData.ATTRIBUTE_RELATIVE_HUMIDITY);
            final double temperature = (double) entry.getData(DBSelectHumidityData.ATTRIBUTE_TEMPERATURE);
            humidityData.add(new Entry(time, (float)relativeHumid));
            temperatureData.add(new Entry(time, (float)temperature));

            Log.i("DATA", String.format("%d: relativeHumid: %f, temp: %f", time, relativeHumid, temperature));
        }

        create_humidity_data_set(humidityData);
        create_temperature_set(temperatureData);

        final List<ILineDataSet> sets = new ArrayList<>();
        sets.add(humiditySet);
        sets.add(temperatureSet);

        lineData = new LineData(sets);
        chart.setData(lineData);

        humiditySet.notifyDataSetChanged();
        temperatureSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.invalidate();
        chart.notifyDataSetChanged();
    }

    private void init_widgets()
    {
        chart = (LineChart) view.findViewById(R.id.humidity_chart);
        setup_chart();
    }

    private void setup_chart()
    {
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getDescription().setEnabled(false);
    }

    private void create_humidity_data_set(List<Entry> pressureData)
    {
        final int pressureColor = Color.RED;
        final String label = String.format(Locale.getDefault(), "%s [%s]",
                getString(R.string.label_humidity),
                getString(R.string.label_humidity_unit));

        humiditySet = new LineDataSet(pressureData, label);
        humiditySet.setCircleColor(pressureColor);
        humiditySet.setColor(pressureColor);
        humiditySet.setDrawCircleHole(false);
        humiditySet.setDrawCircles(true);
    }

    private void create_temperature_set(List<Entry> temperatureData)
    {
        final int temperatureColor = Color.BLUE;
        final String label = String.format(Locale.getDefault(), "%s [%s]",
                getString(R.string.label_temperature),
                getString(R.string.label_temperature_unit));

        temperatureSet = new LineDataSet(temperatureData, label);
        temperatureSet.setCircleColor(temperatureColor);
        temperatureSet.setColor(temperatureColor);
        temperatureSet.setDrawCircleHole(false);
        temperatureSet.setDrawCircles(true);
    }
}
