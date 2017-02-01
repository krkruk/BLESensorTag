package pl.projektorion.krzysztof.blesensortag.fragments.database.HeartRate;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
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

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Barometer.DBSelectBarometer;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate.DBSelectHeartRate;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate.DBSelectHeartRateCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate.DBSelectHeartRateCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate.DBSelectHeartRateData;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentHeartRateFragment extends DBPresentSensorFragmentAbstract {

    private View view;
    private LineChart heartRateChart;
    private LineDataSet heartRateSet;
    private LineData heartRateData;

    private static final float DESCRIPTION_FONT_SIZE = 16.5f;


    private ServiceDataReceiver.ReceiverListener dataListener = new ServiceDataReceiver.ReceiverListener() {
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if( resultCode == DBSelectIntentService.RESULT_CODE)
            {
                List<? extends DBSelectInterface> data = resultData
                        .getParcelableArrayList(DBSelectIntentService.EXTRA_RESULT_DATA);
                if( data == null ) return;
                apply_data(data);
            }
        }
    };

    public DBPresentHeartRateFragment() {
    }

    public static DBPresentHeartRateFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentHeartRateFragment fragment = new DBPresentHeartRateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        init_objects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_heart_rate, container, false);
        init_widgets();
        setup_chart();
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        heartRateChart.setOnChartGestureListener(getFlingListener());
    }

    @Override
    public DBQueryParcelableListenerInterface getExportQuery() {
        return new DBSelectHeartRate(rootRecord, sensorRecord);
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance() {
        return new DBSelectHeartRateCount(rootRecord);
    }

    @Override
    protected int attribute_count() {
        return DBSelectHeartRateCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver() {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(dataListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance() {
        DBQueryWithLimitsListenerInterface heartQuery = new DBSelectHeartRate(rootRecord, sensorRecord);
        heartQuery.setLimit(super.getStartAt(), super.getMaxRecordsPerLoad());
        return heartQuery;
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data) {
        heartRateSet.clear();
        for( DBSelectInterface entry : data)
        {
            final double time = (double) entry.getData(DBSelectHeartRateData.ATTRIBUTE_TIME);
            final double heartRate = (double) entry.getData(DBSelectHeartRateData.ATTRIBUTE_HEART_RATE);
            heartRateSet.addEntry(new Entry((float) time, (float) heartRate));
        }

        invalidate();
    }

    private void init_objects()
    {
        heartRateSet = new LineDataSet(generate_init_values(), getString(R.string.label_heart_rate));
        heartRateData = new LineData(heartRateSet);
    }

    private void init_widgets()
    {
        heartRateChart = (LineChart) view.findViewById(R.id.heart_rate_db_chart);
    }

    private void setup_chart()
    {
        heartRateChart.setData(heartRateData);
        heartRateChart.getDescription().setText(getString(R.string.label_heart_rate));
        heartRateChart.getDescription().setTextSize(DESCRIPTION_FONT_SIZE);

        heartRateChart.getAxisRight().setEnabled(false);
        heartRateChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
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

    private void invalidate()
    {
        heartRateSet.notifyDataSetChanged();
        heartRateData.notifyDataChanged();
        heartRateChart.notifyDataSetChanged();
        heartRateChart.invalidate();
    }

    private List<Entry> generate_init_values() {
        int timeAxis = 0;

        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < getMaxRecordsPerLoad(); i++)
            data.add(new Entry(timeAxis++, 0));

        return data;
    }
}
