package pl.projektorion.krzysztof.blesensortag.fragments.database.Stethoscope;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeParamData;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;
import pl.projektorion.krzysztof.blesensortag.math.MAlgorithmExecutor;
import pl.projektorion.krzysztof.blesensortag.math.MComputeService;
import pl.projektorion.krzysztof.blesensortag.math.MSignalVector;
import pl.projektorion.krzysztof.blesensortag.math.algorithms.MAlgorithmFindPeaks;
import pl.projektorion.krzysztof.blesensortag.math.algorithms.MAlgorithmGaussFilter;
import pl.projektorion.krzysztof.blesensortag.math.algorithms.MAlgorithmPower;
import pl.projektorion.krzysztof.blesensortag.math.algorithms.MAlgorithmMean;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentStethoscopeFragment extends DBPresentSensorFragmentAbstract {

    public static final String ACTION_COMPUTED_CARDIAC_RR =
            "pl.projektorion.krzysztof.blesensortag.fragments.database.Stethoscope.action.COMPUTED_CARDIAC_RR";

    public static final String EXTRA_CARDIAC_RR =
            "pl.projektorion.krzysztof.blesensortag.fragments.database.Stethoscope.extra.CARDIAC_R";

    private Context appContext;
    private View view;
    private LineChart stethoscopeChart;
    private LineDataSet stethoscopeDataSet;
    private LineData stethoscopeData;

    private static final int VALUES_PER_CHART = 3000;
    private static final float DESCRIPTION_FONT_SIZE = 18.5f;
    private long timeAxis = 0;

    private List<Double> dataToRecompute = new ArrayList<>();
    private double initialChartTime = 0.0;
    private static final int CONV_BOUNDARY_DATA_DISMISS_LIMIT = 15;
    private static final int UPDATE_CHART_RESULT_CODE = 67181;
    private static final int PEAK_SEEK_RESULT_CODE = 5412;

    private ServiceDataReceiver serviceReceiver;

    private ServiceDataReceiver.ReceiverListener receiverListener = new ServiceDataReceiver.ReceiverListener() {
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

    private ServiceDataReceiver.ReceiverListener computationReceiver = new ServiceDataReceiver.ReceiverListener() {
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if( resultCode == UPDATE_CHART_RESULT_CODE )
            {
                final MSignalVector result =
                        resultData.getParcelable(MComputeService.EXTRA_RESULT_DATA);
                apply_recomputed_data(result);
            }
            else if( resultCode == PEAK_SEEK_RESULT_CODE )
            {
                final MSignalVector result =
                        resultData.getParcelable(MComputeService.EXTRA_RESULT_DATA);
                if( result == null || result.isEmpty() ) return;
                Log.i("PEAKS", "MEAN R-R: " + result.toInteger().toString());
                final Intent cardiacRrIntent = new Intent(ACTION_COMPUTED_CARDIAC_RR);
                final double rr = result.toList().get(0);
                cardiacRrIntent.putExtra(EXTRA_CARDIAC_RR, rr);
                LocalBroadcastManager.getInstance(appContext).sendBroadcast(cardiacRrIntent);
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
        dataToRecompute.clear();
        empty_data_set();

        if( !data.isEmpty() )
            set_initial_chart_time(data.get(0));

        Log.i("APPLY", "In apply_data");
        Log.i("TIME", "Time base [ms]: " + initialChartTime);
        final double notifyPeriod = (double) sensorRecord
                .getData(DBSelectStethoscopeParamData.ATTRIBUTE_NOTIFY_PERIOD);
        Log.i("TIME", "NotifyBase base [ms]: " + notifyPeriod);
        for( DBSelectInterface record : data )
        {
            final double time = (double) record.getData(DBSelectStethoscopeData.ATTRIBUTE_TIME);
            final double first = (double) record.getData(DBSelectStethoscopeData.ATTRIBUTE_FIRST);

            add_data_to_compute(first);
            stethoscopeDataSet.addEntry(new Entry((float) time, (float) first));
        }

        notify_data_updated();
        invoke_signal_filtering_service();
    }

    protected void apply_recomputed_data(MSignalVector data)
    {
        Log.i("RECOMP", "Applying recomputed");
        Log.i("TIME", "Time base [ms]: " + initialChartTime);
        empty_data_set();
        final double notifyPeriod = (double) sensorRecord
                .getData(DBSelectStethoscopeParamData.ATTRIBUTE_NOTIFY_PERIOD);
        Log.i("TIME", "NotifyBase base [ms]: " + notifyPeriod);
        double time = initialChartTime;
        final List<Double> numericData = data.toList();
        int index = 0;
        final int dataSize = data.size();
        final int upperLimit = dataSize - CONV_BOUNDARY_DATA_DISMISS_LIMIT;
        for( double value : numericData )
        {
            if(index++ < CONV_BOUNDARY_DATA_DISMISS_LIMIT || index-1 > upperLimit) continue;

            stethoscopeDataSet.addEntry(new Entry((float) time, (float) value));
            time += notifyPeriod;
        }

        dataToRecompute = data.toList();
        notify_data_updated();
        invoke_peak_seeker_service();
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
        serviceReceiver = MComputeService.createServiceReceiver(new Handler());
        serviceReceiver.setListener(computationReceiver);
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

    private void invoke_signal_filtering_service()
    {
        final Intent computeService = new Intent(appContext, MComputeService.class);
        computeService.putExtra(MComputeService.EXTRA_DATA_RECEIVER, serviceReceiver);
        computeService.putExtra(MComputeService.EXTRA_ALGORITHM_EXECUTOR, create_filter_algorithm());
        computeService.putExtra(MComputeService.EXTRA_RESULT_CODE, UPDATE_CHART_RESULT_CODE);
        appContext.startService(computeService);
    }

    private void invoke_peak_seeker_service()
    {
        final Intent computeService = new Intent(appContext, MComputeService.class);
        computeService.putExtra(MComputeService.EXTRA_DATA_RECEIVER, serviceReceiver);
        computeService.putExtra(MComputeService.EXTRA_ALGORITHM_EXECUTOR, create_peak_seek_algorithm());
        computeService.putExtra(MComputeService.EXTRA_RESULT_CODE, PEAK_SEEK_RESULT_CODE);
        appContext.startService(computeService);
    }

    private void add_data_to_compute(double data)
    {
        dataToRecompute.add(data);
    }

    private void set_initial_chart_time(DBSelectInterface firstRecord)
    {
        this.initialChartTime = (double) firstRecord
                .getData(DBSelectStethoscopeData.ATTRIBUTE_TIME);

    }

    private MAlgorithmExecutor create_filter_algorithm()
    {
        return new MAlgorithmExecutor.Build()
                .setData(new MSignalVector(dataToRecompute))
                .setAlgorithm(new MAlgorithmGaussFilter(1.7, 5))
                .build();
    }

    private MAlgorithmExecutor create_peak_seek_algorithm()
    {
        final double notifyPeriod = (double) sensorRecord
                .getData(DBSelectStethoscopeParamData.ATTRIBUTE_NOTIFY_PERIOD);
        return new MAlgorithmExecutor.Build()
                .setData(new MSignalVector(dataToRecompute))
                .setAlgorithm(new MAlgorithmPower(2))
                .setAlgorithm(new MAlgorithmFindPeaks((int) (800.0f / notifyPeriod)))
                .setAlgorithm(new MAlgorithmMean(notifyPeriod))
                .build();
    }
}
