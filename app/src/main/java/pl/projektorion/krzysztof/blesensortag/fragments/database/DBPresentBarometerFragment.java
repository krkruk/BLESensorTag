package pl.projektorion.krzysztof.blesensortag.fragments.database;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
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
    private LineChart upperChart;
    private LineData upperData;

    private Handler handler;
    private ServiceDataReceiver receiver;

    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;

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

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {
            Log.i("Motion", "dX: " + dX + "dY: " + dY);
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
        Intent intent = new Intent(context, DBSelectIntentService.class);
        final DBSelectBarometer barometer = new DBSelectBarometer(rootRecord, sensorRecord);
        intent.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, barometer);
        intent.putExtra(DBSelectIntentService.EXTRA_RESULT_RECEIVER, receiver);
        getActivity().startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
        {
            ArrayList<? extends DBSelectInterface > data = resultData.getParcelableArrayList(
                    DBSelectIntentService.EXTRA_RESULT);

            apply_data(data);
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
        upperChart = (LineChart) view.findViewById(R.id.barometer_chart);
        upperChart.setOnChartGestureListener(chartGestureListener);
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
        }

        final List<ILineDataSet> sets = new ArrayList<>();
        sets.add(new LineDataSet(pressureData, "Pressure"));
        sets.add(new LineDataSet(temperatureData, "Temperature"));

        upperData = new LineData(sets);
        upperChart.setData(upperData);
        upperChart.invalidate();
        upperChart.notifyDataSetChanged();
    }
}
