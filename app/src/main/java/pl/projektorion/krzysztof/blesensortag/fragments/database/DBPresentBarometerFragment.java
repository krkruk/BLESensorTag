package pl.projektorion.krzysztof.blesensortag.fragments.database;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometer;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerData;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentBarometerFragment extends Fragment
    implements ServiceDataReceiver.ReceiverListener {

    public static final String EXTRA_ROOT_RECORD =
            "pl.projektorion.krzysztof.blesensortag.fragments.database.extra.ROOT_RECORD";

    public static final String EXTRA_SENSOR_RECORD =
            "pl.projektorion.krzysztof.blesensortag.fragments.database.extra.SENSOR_RECORD";


    private View view;
    private Context context;
    private LineChart chart;
    private LineData lineData;
    private LineDataSet pressureSet;
    private LineDataSet temperatureSet;

    private Handler handler;
    private ServiceDataReceiver receiver;
    private ServiceDataReceiver recordCounterReceiver;

    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;
    private DBSelectInterface recordCounter;

    private static final long NO_ELEMS = 5;

    private DBSelectOnChartFlingListener chartGestureListener;


    private Runnable requestNewDataTask = new Runnable() {
        @Override
        public void run() {
            request_new_data();
        }
    };

    private ServiceDataReceiver.ReceiverListener recordCounterListener = new ServiceDataReceiver.ReceiverListener() {
        private long availableRecords = 0;

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
            {
                List<? extends DBSelectInterface> data = resultData.getParcelableArrayList(
                        DBSelectIntentService.EXTRA_RESULT);

                if( data == null || data.size() <= 0 ) {
                    Log.i("RecRead", "Could not count available records");
                    request_new_data();
                    return;
                }

                recordCounter = data.get(0);
                availableRecords = (long)recordCounter
                        .getData(DBSelectBarometerCountData.ATTRIBUTE_COUNT);
            }
            prepare_chart_gesture_listener();
            request_new_data();
        }

        private void prepare_chart_gesture_listener()
        {
            chartGestureListener = new DBSelectOnChartFlingListener(availableRecords, NO_ELEMS);
            chartGestureListener.setTask(requestNewDataTask);
            chart.setOnChartGestureListener(chartGestureListener);
        }
    };

    public DBPresentBarometerFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_objects();
        acquire_data();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_barometer, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        request_record_count();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
        {
            ArrayList<? extends DBSelectInterface > data = resultData.getParcelableArrayList(
                    DBSelectIntentService.EXTRA_RESULT);

            if( data == null )
                return;

            apply_data(data);
        }
    }

    private void init_objects()
    {
        context = getActivity().getApplicationContext();
        handler = new Handler();
        receiver = new ServiceDataReceiver(handler);
        recordCounterReceiver = new ServiceDataReceiver(handler);
        receiver.setListener(this);
        recordCounterReceiver.setListener(recordCounterListener);
    }

    private void acquire_data()
    {
        final Bundle bundle = getArguments();
        rootRecord = bundle.getParcelable(EXTRA_ROOT_RECORD);
        sensorRecord = bundle.getParcelable(EXTRA_SENSOR_RECORD);
        recordCounter = new DBSelectBarometerCountData();
    }

    private void init_widgets()
    {
        chart = (LineChart) view.findViewById(R.id.barometer_chart);
        setup_chart();
    }

    private void request_record_count()
    {
        Intent intent = new Intent(context, DBSelectIntentService.class);
        DBSelectBarometerCount counter = new DBSelectBarometerCount(rootRecord);
        intent.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, counter);
        intent.putExtra(DBSelectIntentService.EXTRA_RESULT_RECEIVER, recordCounterReceiver);
        getActivity().startService(intent);
    }

    private void request_new_data()
    {
        Intent intent = new Intent(context, DBSelectIntentService.class);
        final DBSelectBarometer barometer = new DBSelectBarometer(rootRecord, sensorRecord);
        barometer.setLimit(chartGestureListener.getStartAt(),
                chartGestureListener.getMaxElementsPerLoad());
        intent.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, barometer);
        intent.putExtra(DBSelectIntentService.EXTRA_RESULT_RECEIVER, receiver);
        getActivity().startService(intent);
    }

    private void apply_data(ArrayList<? extends DBSelectInterface> data)
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
}
