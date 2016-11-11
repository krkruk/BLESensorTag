package pl.projektorion.krzysztof.blesensortag.fragments.presentation.SensorTag;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl.ConnectionControlData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionControlFragment extends Fragment
    implements Observer {

    private View view;
    private TextView labelConnectionInterval;
    private TextView labelSlaveLatency;
    private TextView labelSupervisionTimeout;


    private Observable observable;
    private Handler handler;


    public ConnectionControlFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final double connInterval = data.getValue(ConnectionControlData.ATTRIBUTE_CONNECTION_INTERVAL);
        final double slaveLatency = data.getValue(ConnectionControlData.ATTRIBUTE_SLAVE_LATENCY);
        final double supTimeout = data.getValue(ConnectionControlData.ATTRIBUTE_SUPERVISION_TIMEOUT);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelConnectionInterval.setText(to_string(connInterval));
                labelSlaveLatency.setText(to_string(slaveLatency));
                labelSupervisionTimeout.setText(to_string(supTimeout));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connection_control, container, false);
        init_widgets();
        handler = new Handler();
        return view;
    }

    @Override
    public void onDestroy() {
        if( observable != null )
            observable.deleteObserver(this);

        super.onDestroy();
    }

    private void init_widgets()
    {
        labelConnectionInterval = (TextView) view.findViewById(R.id.connection_interval_status);
        labelSlaveLatency = (TextView) view.findViewById(R.id.slave_latency_status);
        labelSupervisionTimeout = (TextView) view.findViewById(R.id.supervision_timeout_status);
    }

    private String to_string(double value)
    {
        return String.format(Locale.ENGLISH, "%.0f", value);
    }
}
