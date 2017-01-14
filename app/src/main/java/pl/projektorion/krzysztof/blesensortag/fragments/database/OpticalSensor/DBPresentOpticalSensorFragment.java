package pl.projektorion.krzysztof.blesensortag.fragments.database.OpticalSensor;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.OpticalSensor.DBSelectOpticalSensor;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.OpticalSensor.DBSelectOpticalSensorCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.OpticalSensor.DBSelectOpticalSensorCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.OpticalSensor.DBSelectOpticalSensorData;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBPresentOpticalSensorFragment extends DBPresentSensorFragmentAbstract {

    private View view;
    private LineChart chart;
    private LineData lineData;
    private LineDataSet lightIntensitySet;

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

    public static DBPresentOpticalSensorFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentOpticalSensorFragment fragment = new DBPresentOpticalSensorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_opticalsensor, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        chart.setOnChartGestureListener(getFlingListener());
    }

    @Override
    public DBQueryParcelableListenerInterface getExportQuery() {
        return new DBSelectOpticalSensor(rootRecord, sensorRecord);
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance() {
        return new DBSelectOpticalSensorCount(rootRecord);
    }

    @Override
    protected int attribute_count() {
        return DBSelectOpticalSensorCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver() {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(dataListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance() {
        DBQueryWithLimitsListenerInterface opticalSensorQuery = new DBSelectOpticalSensor(
                rootRecord, sensorRecord);
        opticalSensorQuery.setLimit(super.getStartAt(), super.getMaxRecordsPerLoad());
        return opticalSensorQuery;
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data)
    {
        List<Entry> lightIntensityData = new ArrayList<>();

        for(DBSelectInterface entry : data)
        {
            final long time = (long) entry.getData(DBSelectOpticalSensorData.ATTRIBUTE_TIME);
            final double lightIntensity = (double) entry.getData(DBSelectOpticalSensorData.ATTRIBUTE_LIGHT_INTENSITY);
            lightIntensityData.add(new Entry(time, (float)lightIntensity));

            Log.i("DATA", String.format("%d: lightIntensity: %f", time, lightIntensity));
        }

        create_light_intensity_data(lightIntensityData);

        lineData = new LineData(lightIntensitySet);
        chart.setData(lineData);

        lightIntensitySet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.invalidate();
        chart.notifyDataSetChanged();
    }

    private void init_widgets()
    {
        chart = (LineChart) view.findViewById(R.id.optical_sensor_chart);
        setup_chart();
    }

    private void setup_chart()
    {
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getDescription().setEnabled(false);
    }

    private void create_light_intensity_data(List<Entry> pressureData)
    {
        final int pressureColor = Color.RED;
        final String label = String.format(Locale.getDefault(), "%s [%s]",
                getString(R.string.label_light_intensity),
                getString(R.string.label_light_intensity_unit));

        lightIntensitySet = new LineDataSet(pressureData, label);
        lightIntensitySet.setCircleColor(pressureColor);
        lightIntensitySet.setColor(pressureColor);
        lightIntensitySet.setDrawCircleHole(false);
        lightIntensitySet.setDrawCircles(true);
    }
}
