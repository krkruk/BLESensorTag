package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag.Movement;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovementFragment extends Fragment
    implements Observer {

    private View view;
    private BarChart accChart;
    private BarData accData;
    private BarDataSet accDataSet;
    private String accLabel;

    private BarChart gyrChart;
    private BarData gyrData;
    private BarDataSet gyrDataSet;
    private String gyrLabel;

    private BarChart magChart;
    private BarData magData;
    private BarDataSet magDataSet;
    private String magLabel;

    private float gyroX;
    private float gyroY;
    private float gyroZ;
    private float accX;
    private float accY;
    private float accZ;
    private float magnetX;
    private float magnetY;
    private float magnetZ;

    private Observable observable;
    private Handler handler;

    private IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter() {
        final String[] labels = { "X", "Y", "Z" };
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            final int index = (int) value;
            if( labels.length < index )
                return "";
            return labels[index];
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    };

    public MovementFragment() {}

    @Override
    public void update(Observable o, final Object arg) {
        observable = o;
        final ProfileData data = (ProfileData) arg;
        if( handler == null ) return;
        gyroX = (float) data.getValue(MovementData.ATTRIBUTE_GYRO_X);
        gyroY = (float) data.getValue(MovementData.ATTRIBUTE_GYRO_Y);
        gyroZ = (float) data.getValue(MovementData.ATTRIBUTE_GYRO_Z);
        accX = (float) data.getValue(MovementData.ATTRIBUTE_ACC_X);
        accY = (float) data.getValue(MovementData.ATTRIBUTE_ACC_Y);
        accZ = (float) data.getValue(MovementData.ATTRIBUTE_ACC_Z);
        magnetX = (float) data.getValue(MovementData.ATTRIBUTE_MAGNET_X);
        magnetY = (float) data.getValue(MovementData.ATTRIBUTE_MAGNET_Y);
        magnetZ = (float) data.getValue(MovementData.ATTRIBUTE_MAGNET_Z);

        handler.post(new Runnable() {
            @Override
            public void run() {
                accData.removeDataSet(accDataSet);
                accDataSet = generate_acc_data_set(accX, accY, accZ);
                accData.addDataSet(accDataSet);

                gyrData.removeDataSet(gyrDataSet);
                gyrDataSet = generate_gyr_data_set(gyroX, gyroY, gyroZ);
                gyrData.addDataSet(gyrDataSet);

                magData.removeDataSet(magDataSet);
                magDataSet = generate_mag_data_set(magnetX, magnetY, magnetZ);
                magData.addDataSet(magDataSet);
                update_data();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        accLabel = getString(R.string.label_accelerometer);
        gyrLabel = getString(R.string.label_gyroscope);
        magLabel = getString(R.string.label_magnetometer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movement, container, false);
        init_objects();
        init_widgets();
        setup_charts();
        update_data();
        return view;
    }

    private BarDataSet generate_acc_data_set(float x, float y, float z)
    {
        BarDataSet dataSet = new BarDataSet(generate_entries(x, y, z), accLabel);
        dataSet.setColor(Color.RED);
        return dataSet;
    }

    private BarDataSet generate_gyr_data_set(float x, float y, float z)
    {
        BarDataSet dataSet = new BarDataSet(generate_entries(x, y, z), gyrLabel);
        dataSet.setColor(Color.GREEN);
        return dataSet;
    }

    private BarDataSet generate_mag_data_set(float x, float y, float z)
    {
        BarDataSet dataSet = new BarDataSet(generate_entries(x, y, z), magLabel);
        dataSet.setColor(Color.BLUE);
        return dataSet;
    }

    @Override
    public void onDestroy() {
        if( observable != null )
            observable.deleteObserver(this);

        super.onDestroy();
    }

    private void init_objects()
    {
        accDataSet = generate_acc_data_set(0f,0f,0f);
        accData = new BarData(accDataSet);

        gyrDataSet = generate_gyr_data_set(0f,0f,0f);
        gyrData = new BarData(gyrDataSet);

        magDataSet = generate_mag_data_set(0f,0f,0f);
        magData = new BarData(magDataSet);
    }

    private List<BarEntry> generate_entries(float x, float y, float z)
    {
        List<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(0.0f, x));
        data.add(new BarEntry(1.0f, y));
        data.add(new BarEntry(2.0f, z));
        return data;
    }

    private void init_widgets()
    {
        accChart = (BarChart) view.findViewById(R.id.accelerometer_chart);
        gyrChart = (BarChart) view.findViewById(R.id.gyroscope_chart);
        magChart = (BarChart) view.findViewById(R.id.magnetometer_chart);

        accChart.setData(accData);
        gyrChart.setData(gyrData);
        magChart.setData(magData);
    }

    private void setup_charts()
    {
        accChart.setFitBars(true);
        XAxis accXAxis = accChart.getXAxis();
        accXAxis.setValueFormatter(xAxisFormatter);
        accXAxis.setGranularity(1f);
        accXAxis.setDrawAxisLine(false);
        accXAxis.setDrawGridLines(false);
        accXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        accChart.getAxisRight().setEnabled(false);
        accChart.getDescription().setEnabled(false);

        gyrChart.setFitBars(true);
        XAxis gyrXAxis = gyrChart.getXAxis();
        gyrXAxis.setValueFormatter(xAxisFormatter);
        gyrXAxis.setGranularity(1f);
        gyrXAxis.setDrawAxisLine(false);
        gyrXAxis.setDrawGridLines(false);
        gyrXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        gyrChart.getAxisRight().setEnabled(false);
        gyrChart.getDescription().setEnabled(false);

        magChart.setFitBars(true);
        XAxis magXAxis = magChart.getXAxis();
        magXAxis.setValueFormatter(xAxisFormatter);
        magXAxis.setGranularity(1f);
        magXAxis.setDrawAxisLine(false);
        magXAxis.setDrawGridLines(false);
        magXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        magChart.getAxisRight().setEnabled(false);
        magChart.getDescription().setEnabled(false);
    }

    private void update_data()
    {
        accDataSet.notifyDataSetChanged();
        accData.notifyDataChanged();
        accChart.notifyDataSetChanged();
        accChart.invalidate();

        gyrDataSet.notifyDataSetChanged();
        gyrData.notifyDataChanged();
        gyrChart.notifyDataSetChanged();
        gyrChart.invalidate();

        magDataSet.notifyDataSetChanged();
        magData.notifyDataChanged();
        magChart.notifyDataSetChanged();
        magChart.invalidate();
    }
}
