package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.GAPService;


import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.GAPService.GAPServiceData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.ProfileStringData;

/**
 * A simple {@link Fragment} subclass.
 */
public class GAPServiceFragment extends Fragment
    implements Observer {

    private View view;
    private TextView labelGapDeviceName;
    private String deviceName;

    private Observable observable;
    private Handler handler;

    public GAPServiceFragment() {}

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileStringData data = (ProfileStringData) arg;
        if( handler == null ) return;
        final String deviceName = data.getValue(GAPServiceData.ATTRIBUTE_DEVICE_NAME);
        this.deviceName = deviceName;
        handler.post(new Runnable() {
            @Override
            public void run() {
                labelGapDeviceName.setText(deviceName);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gap_service, container, false);
        init_widgets();
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
        labelGapDeviceName = (TextView) view.findViewById(R.id.gap_device_name);
    }
}
