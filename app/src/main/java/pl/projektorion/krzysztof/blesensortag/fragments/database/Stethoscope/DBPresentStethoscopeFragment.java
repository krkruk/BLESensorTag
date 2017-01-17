package pl.projektorion.krzysztof.blesensortag.fragments.database.Stethoscope;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
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
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscope;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeData;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscope;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentStethoscopeFragment extends DBPresentSensorFragmentAbstract {

    private Context appContext;
    private View view;
    private LineChart stethoscopeChart;
    private LineDataSet stethoscopeDataSet;
    private LineData stethoscopeData;

    private static final int VALUES_PER_CHART = 400;
    private static final float DESCRIPTION_FONT_SIZE = 18.5f;
    private long timeAxis = 0;

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

    public DBPresentStethoscopeFragment() {
    }

    public static DBPresentStethoscopeFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentStethoscopeFragment fragment = new DBPresentStethoscopeFragment();
        fragment.setReadMaxRecordsPerLoad(VALUES_PER_CHART);
        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.fragment_dbpresent_stethoscope, container, false);
        init_widgets();
        configure_plot();
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        stethoscopeChart.setOnChartGestureListener(getFlingListener());

    }

    @Override
    public DBQueryParcelableListenerInterface getExportQuery() {
        return new DBSelectStethoscope(rootRecord, sensorRecord);
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance() {
        return new DBSelectStethoscopeCount(rootRecord);
    }

    @Override
    protected int attribute_count() {
        return DBSelectStethoscopeCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver() {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(receiverListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance() {
        DBQueryWithLimitsListenerInterface query = new DBSelectStethoscope(rootRecord, sensorRecord);
        query.setLimit(getStartAt(), getMaxRecordsPerLoad());
        return query;
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data) {
        empty_data_set();

        for( DBSelectInterface record : data )
        {
            final double time = (double) record.getData(DBSelectStethoscopeData.ATTRIBUTE_TIME);
            final double first = (double) record.getData(DBSelectStethoscopeData.ATTRIBUTE_FIRST);

            Log.i("Loaded", time + ", " + first);
            stethoscopeDataSet.addEntry(new Entry((float) time, (float) first));
        }

        notify_data_updated();
    }

    private void init_android_framework()
    {
        setRetainInstance(true);
        appContext = getActivity().getApplicationContext();
    }

    private void init_widgets()
    {
        stethoscopeChart = (LineChart) view.findViewById(R.id.present_stethoscope_plot);
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
        stethoscopeChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        stethoscopeDataSet.setDrawCircleHole(false);
        stethoscopeDataSet.setDrawCircles(false);
        stethoscopeDataSet.setColor(Color.RED);
        stethoscopeDataSet.setCircleColor(Color.RED);
        stethoscopeDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "";
            }
        });
    }

    private List<Entry> generate_init_values() {
        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < VALUES_PER_CHART; i++)
            data.add(new Entry(timeAxis++, 0));

        return data;
    }

    private void empty_data_set()
    {
        stethoscopeDataSet.clear();
    }

    private void notify_data_updated()
    {
        stethoscopeDataSet.notifyDataSetChanged();
        stethoscopeData.notifyDataChanged();
        stethoscopeChart.notifyDataSetChanged();
        stethoscopeChart.invalidate();
    }
}
