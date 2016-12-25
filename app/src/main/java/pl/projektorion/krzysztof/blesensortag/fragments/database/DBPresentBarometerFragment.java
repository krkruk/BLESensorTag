package pl.projektorion.krzysztof.blesensortag.fragments.database;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometer;
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

    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;

    private long startAt = 0;
    private static final long NO_ELEMS = 5;

    private OnChartGestureListener chartGestureListener = new OnChartGestureListener() {
        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            Log.i("Fling", String.format("vX: %f, vY: %f", velocityX, velocityY));
            Intent intent = new Intent(context, DBSelectIntentService.class);
            final DBSelectBarometer barometer = new DBSelectBarometer(rootRecord, sensorRecord);
            barometer.setLimit(5, NO_ELEMS);
            Log.i("Request", barometer.getQuery());
            Log.i("ReqDat", barometer.getQueryData()[1] + " - " + barometer.getQueryData()[2]);
            intent.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, barometer);
            intent.putExtra(DBSelectIntentService.EXTRA_RESULT_RECEIVER, receiver);
            getActivity().startService(intent);
        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {
//            Log.i("Movement", String.format("dX: %f, dY: %f", dX, dY));
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
        request_new_data();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
        {
            ArrayList<? extends DBSelectInterface > data = resultData.getParcelableArrayList(
                    DBSelectIntentService.EXTRA_RESULT);

            apply_data(data);
            Log.i("Data", "Updated!");
        }
    }

    private void init_objects()
    {
        context = getActivity().getApplicationContext();
        handler = new Handler();
        receiver = new ServiceDataReceiver(handler);
        receiver.setListener(this);
    }

    private void acquire_data()
    {
        final Bundle bundle = getArguments();
        rootRecord = bundle.getParcelable(EXTRA_ROOT_RECORD);
        sensorRecord = bundle.getParcelable(EXTRA_SENSOR_RECORD);
    }

    private void init_widgets()
    {
        chart = (LineChart) view.findViewById(R.id.barometer_chart);
        chart.setOnChartGestureListener(chartGestureListener);
        setup_chart();
    }

    private void request_new_data()
    {
        Intent intent = new Intent(context, DBSelectIntentService.class);
        final DBSelectBarometer barometer = new DBSelectBarometer(rootRecord, sensorRecord);
        barometer.setLimit(startAt, NO_ELEMS);
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
