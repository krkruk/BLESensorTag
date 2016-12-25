package pl.projektorion.krzysztof.blesensortag.fragments.database;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometer;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerData;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentBarometerFragment extends DBPresentSensorFragmentAbstract {

    private View view;
    private LineChart chart;
    private LineData lineData;
    private LineDataSet pressureSet;
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

    public DBPresentBarometerFragment() {
    }

    public static DBPresentBarometerFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentBarometerFragment fragment = new DBPresentBarometerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_barometer, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        chart.setOnChartGestureListener(getFlingListener());
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data)
    {
        List<Entry> pressureData = new ArrayList<>();
        List<Entry> temperatureData = new ArrayList<>();

        for(DBSelectInterface entry : data)
        {
            final long time = (long) entry.getData(DBSelectBarometerData.ATTRIBUTE_TIME);
            final double pressure = (double) entry.getData(DBSelectBarometerData.ATTRIBUTE_BAROMETRIC_PRESSURE);
            final double temperature = (double) entry.getData(DBSelectBarometerData.ATTRIBUTE_TEMPERATURE);
            pressureData.add(new Entry(time, (float)pressure));
            temperatureData.add(new Entry(time, (float)temperature));

            Log.i("DATA", String.format("%d: pressure: %f, temp: %f", time, pressure, temperature));
        }

        create_pressure_set(pressureData);
        create_temperature_set(temperatureData);

        final List<ILineDataSet> sets = new ArrayList<>();
        sets.add(pressureSet);
        sets.add(temperatureSet);

        lineData = new LineData(sets);
        chart.setData(lineData);

        pressureSet.notifyDataSetChanged();
        temperatureSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.invalidate();
        chart.notifyDataSetChanged();
    }

    private void init_widgets()
    {
        chart = (LineChart) view.findViewById(R.id.barometer_chart);
        setup_chart();
    }

    private void setup_chart()
    {
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getDescription().setEnabled(false);
    }

    private void create_pressure_set(List<Entry> pressureData)
    {
        final int pressureColor = Color.RED;

        pressureSet = new LineDataSet(pressureData, "Pressure");
        pressureSet.setCircleColor(pressureColor);
        pressureSet.setColor(pressureColor);
        pressureSet.setDrawCircleHole(false);
        pressureSet.setDrawCircles(true);
    }

    private void create_temperature_set(List<Entry> temperatureData)
    {
        final int temperatureColor = Color.BLUE;

        temperatureSet = new LineDataSet(temperatureData, "Temperature");
        temperatureSet.setCircleColor(temperatureColor);
        temperatureSet.setColor(temperatureColor);
        temperatureSet.setDrawCircleHole(false);
        temperatureSet.setDrawCircles(true);
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance() {
        return new DBSelectBarometerCount(rootRecord);
    }

    @Override
    protected int attribute_count() {
        return DBSelectBarometerCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver() {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(dataListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance() {
        DBQueryWithLimitsListenerInterface barometerQuery = new DBSelectBarometer(rootRecord, sensorRecord);
        barometerQuery.setLimit(super.getStartAt(), super.getMaxRecordsPerLoad());
        return barometerQuery;
    }
}
