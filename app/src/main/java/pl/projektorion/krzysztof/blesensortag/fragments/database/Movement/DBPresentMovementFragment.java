package pl.projektorion.krzysztof.blesensortag.fragments.database.Movement;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
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
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Movement.DBSelectMovement;
import pl.projektorion.krzysztof.blesensortag.database.selects.Movement.DBSelectMovementCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.Movement.DBSelectMovementCountData;
import pl.projektorion.krzysztof.blesensortag.database.selects.Movement.DBSelectMovementData;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBSelectOnChartFlingIDListener;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentMovementFragment extends DBPresentSensorFragmentAbstract {
    private static final float FONT_SIZE = 14.5f;
    private View view;
    private LineChart accChart;
    private LineDataSet accXSet;
    private LineDataSet accYSet;
    private LineDataSet accZSet;

    private LineChart gyrChart;
    private LineDataSet gyrXSet;
    private LineDataSet gyrYSet;
    private LineDataSet gyrZSet;

    private LineChart magChart;
    private LineDataSet magXSet;
    private LineDataSet magYSet;
    private LineDataSet magZSet;

    private DBSelectOnChartFlingIDListener accChartListener;
    private final int accOnFlingId = 0x01;
    private DBSelectOnChartFlingIDListener gyrChartListener;
    private final int gyrOnFlingId = 0x02;
    private DBSelectOnChartFlingIDListener magChartListener;
    private final int magOnFlingId = 0x03;
    private DBSelectOnChartFlingIDListener currentChartListener = null;
    private final int initialOnFlingId = 0x00;

    private DBSelectOnChartFlingIDListener.ChartTask onFlingTask = new DBSelectOnChartFlingIDListener.ChartTask() {
        @Override
        public void run(DBSelectOnChartFlingIDListener listener) {
            currentChartListener = listener;
            request_new_data();
        }
    };

    private ServiceDataReceiver.ReceiverListener receiverListener = new ServiceDataReceiver.ReceiverListener() {
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
            {
                List<? extends DBSelectInterface> data = resultData
                        .getParcelableArrayList(DBSelectIntentService.EXTRA_RESULT);
                if( data == null ) return;
                switch (currentChartListener.getChartId())
                {
                    case initialOnFlingId:
                        apply_data(data); break;
                    case accOnFlingId:
                        apply_acc_data(data);
                        break;
                    case gyrOnFlingId:
                        apply_gyr_data(data);
                        break;
                    case magOnFlingId:
                        apply_mag_data(data);
                        break;
                    default: break;
                }
            }
        }
    };

    public DBPresentMovementFragment() {
    }

    public static DBPresentMovementFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentMovementFragment fragment = new DBPresentMovementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_movement, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        currentChartListener = new DBSelectOnChartFlingIDListener(                  //ugly solution but
                getMaxRecordsPerLoad(), getMaxRecordsPerLoad(), initialOnFlingId);  //i don't give a fuck at this time

        super.onReceiveResult(resultCode, resultData);
        create_chart_listeners();
        setup_chart_listeners();
    }

    @Override
    public DBQueryParcelableListenerInterface getExportQuery() {
        return new DBSelectMovement(rootRecord, sensorRecord);
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance() {
        return new DBSelectMovementCount(rootRecord);
    }

    @Override
    protected int attribute_count() {
        return DBSelectMovementCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver() {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(receiverListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance() {
        DBQueryWithLimitsListenerInterface movementQuery = new DBSelectMovement(
                rootRecord, sensorRecord);
        movementQuery.setLimit(currentChartListener.getStartAt(),
                currentChartListener.getMaxElementsPerLoad());
        return movementQuery;
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data) {
        List<Entry> accXData = new ArrayList<>();
        List<Entry> accYData = new ArrayList<>();
        List<Entry> accZData = new ArrayList<>();

        List<Entry> gyrXData = new ArrayList<>();
        List<Entry> gyrYData = new ArrayList<>();
        List<Entry> gyrZData = new ArrayList<>();

        List<Entry> magXData = new ArrayList<>();
        List<Entry> magYData = new ArrayList<>();
        List<Entry> magZData = new ArrayList<>();

        for(DBSelectInterface record : data)
        {
            final long time = (long) record.getData(DBSelectMovementData.ATTRIBUTE_MEASUREMENT);
            accXData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_ACC_X)));
            accYData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_ACC_Y)));
            accZData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_ACC_Z)));

            gyrXData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_GYRO_X)));
            gyrYData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_GYRO_Y)));
            gyrZData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_GYRO_Z)));

            magXData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_MAGNET_X)));
            magYData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_MAGNET_Y)));
            magZData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_MAGNET_Z)));
        }

        create_acc_x_set(accXData);
        create_acc_y_set(accYData);
        create_acc_z_set(accZData);

        create_gyr_x_set(gyrXData);
        create_gyr_y_set(gyrYData);
        create_gyr_z_set(gyrZData);

        create_mag_x_set(magXData);
        create_mag_y_set(magYData);
        create_mag_z_set(magZData);

        apply_to_acc_chart();
        apply_to_gyr_chart();
        apply_to_mag_chart();
    }

    protected void apply_acc_data(List<? extends DBSelectInterface> data)
    {
        List<Entry> accXData = new ArrayList<>();
        List<Entry> accYData = new ArrayList<>();
        List<Entry> accZData = new ArrayList<>();

        for(DBSelectInterface record : data)
        {
            final long time = (long) record.getData(DBSelectMovementData.ATTRIBUTE_MEASUREMENT);
            accXData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_ACC_X)));
            accYData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_ACC_Y)));
            accZData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_ACC_Z)));
        }

        create_acc_x_set(accXData);
        create_acc_y_set(accYData);
        create_acc_z_set(accZData);

        apply_to_acc_chart();
    }

    protected void apply_gyr_data(List<? extends  DBSelectInterface> data)
    {
        List<Entry> gyrXData = new ArrayList<>();
        List<Entry> gyrYData = new ArrayList<>();
        List<Entry> gyrZData = new ArrayList<>();

        for(DBSelectInterface record : data)
        {
            final long time = (long) record.getData(DBSelectMovementData.ATTRIBUTE_MEASUREMENT);

            gyrXData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_GYRO_X)));
            gyrYData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_GYRO_Y)));
            gyrZData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_GYRO_Z)));
        }

        create_gyr_x_set(gyrXData);
        create_gyr_y_set(gyrYData);
        create_gyr_z_set(gyrZData);

        apply_to_gyr_chart();
    }

    protected void apply_mag_data(List<? extends DBSelectInterface> data)
    {
        List<Entry> magXData = new ArrayList<>();
        List<Entry> magYData = new ArrayList<>();
        List<Entry> magZData = new ArrayList<>();

        for(DBSelectInterface record : data)
        {
            final long time = (long) record.getData(DBSelectMovementData.ATTRIBUTE_MEASUREMENT);

            magXData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_MAGNET_X)));
            magYData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_MAGNET_Y)));
            magZData.add(new Entry(time, (float) record.getData(DBSelectMovementData.ATTRIBUTE_MAGNET_Z)));
        }

        create_mag_x_set(magXData);
        create_mag_y_set(magYData);
        create_mag_z_set(magZData);

        apply_to_mag_chart();
    }

    private void init_widgets()
    {
        accChart = (LineChart) view.findViewById(R.id.acc_present_chart);
        gyrChart = (LineChart) view.findViewById(R.id.gyr_present_chart);
        magChart = (LineChart) view.findViewById(R.id.mag_present_chart);

        setup_charts();
    }

    private void create_chart_listeners()
    {
        accChartListener = new DBSelectOnChartFlingIDListener(
                getAvailableRecords(), getMaxRecordsPerLoad(), accOnFlingId);
        gyrChartListener = new DBSelectOnChartFlingIDListener(
                getAvailableRecords(), getMaxRecordsPerLoad(), gyrOnFlingId);
        magChartListener = new DBSelectOnChartFlingIDListener(
                getAvailableRecords(), getMaxRecordsPerLoad(), magOnFlingId);
    }

    private void setup_chart_listeners()
    {
        accChartListener.setTask(onFlingTask);
        gyrChartListener.setTask(onFlingTask);
        magChartListener.setTask(onFlingTask);

        accChart.setOnChartGestureListener(accChartListener);
        gyrChart.setOnChartGestureListener(gyrChartListener);
        magChart.setOnChartGestureListener(magChartListener);
    }

    private void setup_charts()
    {
        accChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        gyrChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        magChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        accChart.getDescription().setText(getString(R.string.label_accelerometer));
        accChart.getDescription().setTextSize(FONT_SIZE);
        gyrChart.getDescription().setText(getString(R.string.label_gyroscope));
        gyrChart.getDescription().setTextSize(FONT_SIZE);
        magChart.getDescription().setText(getString(R.string.label_magnetometer));
        magChart.getDescription().setTextSize(FONT_SIZE);
    }

    private void create_acc_x_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_acc_x);
        final int color = Color.RED;

        accXSet = new LineDataSet(data, label);
        accXSet.setDrawCircleHole(false);
        accXSet.setDrawCircles(true);
        accXSet.setCircleColor(color);
        accXSet.setColor(color);
    }

    private void create_acc_y_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_acc_y);
        final int color = Color.GREEN;

        accYSet = new LineDataSet(data, label);
        accYSet.setDrawCircleHole(false);
        accYSet.setDrawCircles(true);
        accYSet.setCircleColor(color);
        accYSet.setColor(color);
    }

    private void create_acc_z_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_acc_z);
        final int color = Color.BLUE;

        accZSet = new LineDataSet(data, label);
        accZSet.setDrawCircleHole(false);
        accZSet.setDrawCircles(true);
        accZSet.setCircleColor(color);
        accZSet.setColor(color);
    }

    private void create_gyr_x_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_gyr_x);
        final int color = Color.RED;

        gyrXSet = new LineDataSet(data, label);
        gyrXSet.setDrawCircleHole(false);
        gyrXSet.setDrawCircles(true);
        gyrXSet.setCircleColor(color);
        gyrXSet.setColor(color);
    }

    private void create_gyr_y_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_gyr_y);
        final int color = Color.GREEN;

        gyrYSet = new LineDataSet(data, label);
        gyrYSet.setDrawCircleHole(false);
        gyrYSet.setDrawCircles(true);
        gyrYSet.setCircleColor(color);
        gyrYSet.setColor(color);
    }

    private void create_gyr_z_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_gyr_z);
        final int color = Color.BLUE;

        gyrZSet = new LineDataSet(data, label);
        gyrZSet.setDrawCircleHole(false);
        gyrZSet.setDrawCircles(true);
        gyrZSet.setCircleColor(color);
        gyrZSet.setColor(color);
    }

    private void create_mag_x_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_mag_x);
        final int color = Color.RED;

        magXSet = new LineDataSet(data, label);
        magXSet.setDrawCircleHole(false);
        magXSet.setDrawCircles(true);
        magXSet.setCircleColor(color);
        magXSet.setColor(color);
    }

    private void create_mag_y_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_mag_y);
        final int color = Color.GREEN;

        magYSet = new LineDataSet(data, label);
        magYSet.setDrawCircleHole(false);
        magYSet.setDrawCircles(true);
        magYSet.setCircleColor(color);
        magYSet.setColor(color);
    }

    private void create_mag_z_set(List<Entry> data)
    {
        final String label = getString(R.string.axis_mag_z);
        final int color = Color.BLUE;

        magZSet = new LineDataSet(data, label);
        magZSet.setDrawCircleHole(false);
        magZSet.setDrawCircles(true);
        magZSet.setCircleColor(color);
        magZSet.setColor(color);
    }

    private void apply_to_acc_chart()
    {
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(accXSet);
        dataSets.add(accYSet);
        dataSets.add(accZSet);

        final LineData lineData = new LineData(dataSets);
        accChart.setData(lineData);
        accChart.invalidate();
        accChart.notifyDataSetChanged();
    }

    private void apply_to_gyr_chart()
    {
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(gyrXSet);
        dataSets.add(gyrYSet);
        dataSets.add(gyrZSet);

        final LineData lineData = new LineData(dataSets);
        gyrChart.setData(lineData);
        gyrChart.invalidate();
        gyrChart.notifyDataSetChanged();
    }

    private void apply_to_mag_chart()
    {
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(magXSet);
        dataSets.add(magYSet);
        dataSets.add(magZSet);

        final LineData lineData = new LineData(dataSets);
        magChart.setData(lineData);
        magChart.invalidate();
        magChart.notifyDataSetChanged();
    }
}
