package pl.projektorion.krzysztof.blesensortag.fragments.database;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature.DBSelectIRTemperature;
import pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature.DBSelectIRTemperatureCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature.DBSelectIRTemperatureCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature.DBSelectIRTemperatureData;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBPresentIRTemperatureFragment extends DBPresentSensorFragmentAbstract {

    private View view;
    private LineChart chart;
    private LineData lineData;
    private LineDataSet objectTemperatureSet;
    private LineDataSet ambientTemperatureSet;

    private ServiceDataReceiver.ReceiverListener receiverListener = new ServiceDataReceiver.ReceiverListener() {
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

    public static DBPresentIRTemperatureFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentIRTemperatureFragment fragment = new DBPresentIRTemperatureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_irtemperature, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        chart.setOnChartGestureListener(getFlingListener());
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance() {
        return new DBSelectIRTemperatureCount(rootRecord);
    }

    @Override
    protected int attribute_count() {
        return DBSelectIRTemperatureCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver() {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(receiverListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance() {
        DBQueryWithLimitsListenerInterface irTempQuery = new DBSelectIRTemperature(
                rootRecord, sensorRecord);
        irTempQuery.setLimit(super.getStartAt(), super.getMaxRecordsPerLoad());
        return irTempQuery;
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data)
    {
        List<Entry> objectTemperature = new ArrayList<>();
        List<Entry> ambientTemperature = new ArrayList<>();

        for(DBSelectInterface entry : data)
        {
            final long time = (long) entry.getData(DBSelectIRTemperatureData.ATTRIBUTE_TIME);
            final double objectTemp = (double) entry.getData(DBSelectIRTemperatureData.ATTRIBUTE_OBJECT_TEMPERATURE);
            final double ambientTemp = (double) entry.getData(DBSelectIRTemperatureData.ATTRIBUTE_AMBIENT_TEMPERATURE);
            objectTemperature.add(new Entry(time, (float)objectTemp));
            ambientTemperature.add(new Entry(time, (float)ambientTemp));

            Log.i("DATA", String.format("%d: objectTemp: %f, temp: %f", time, objectTemp, ambientTemp));
        }

        create_object_temp_set(objectTemperature);
        create_ambient_temp_set(ambientTemperature);

        final List<ILineDataSet> sets = new ArrayList<>();
        sets.add(objectTemperatureSet);
        sets.add(ambientTemperatureSet);

        lineData = new LineData(sets);
        chart.setData(lineData);

        objectTemperatureSet.notifyDataSetChanged();
        ambientTemperatureSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.invalidate();
        chart.notifyDataSetChanged();
    }

    private void init_widgets()
    {
        chart = (LineChart) view.findViewById(R.id.ir_temperature_chart);
        setup_chart();
    }

    private void setup_chart()
    {
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getDescription().setEnabled(false);
    }

    private void create_object_temp_set(List<Entry> pressureData)
    {
        final int pressureColor = Color.RED;
        final String label = String.format(Locale.getDefault(), "%s [%s]",
                getString(R.string.label_object_temperature),
                getString(R.string.label_temperature_unit));

        objectTemperatureSet = new LineDataSet(pressureData, label);
        objectTemperatureSet.setCircleColor(pressureColor);
        objectTemperatureSet.setColor(pressureColor);
        objectTemperatureSet.setDrawCircleHole(false);
        objectTemperatureSet.setDrawCircles(true);
    }

    private void create_ambient_temp_set(List<Entry> temperatureData)
    {
        final int temperatureColor = Color.BLUE;
        final String label = String.format(Locale.getDefault(), "%s [%s]",
                getString(R.string.label_ambient_temperature),
                getString(R.string.label_temperature_unit));

        ambientTemperatureSet = new LineDataSet(temperatureData, label);
        ambientTemperatureSet.setCircleColor(temperatureColor);
        ambientTemperatureSet.setColor(temperatureColor);
        ambientTemperatureSet.setDrawCircleHole(false);
        ambientTemperatureSet.setDrawCircles(true);
    }
}
