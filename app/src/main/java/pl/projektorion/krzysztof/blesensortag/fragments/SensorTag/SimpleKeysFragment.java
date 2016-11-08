package pl.projektorion.krzysztof.blesensortag.fragments.SensorTag;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys.SimpleKeysData;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleKeysFragment extends Fragment
    implements Observer {

    private View view;
    private TextView labelLeftButton;
    private TextView labelRightButton;
    private TextView labelReedRelay;

    private Handler handler;
    private Observable modelObservable;

    public SimpleKeysFragment() {
    }

    @Override
    public void update(Observable o, Object arg) {
        modelObservable = o;
        final AbstractProfileData data = (AbstractProfileData) arg;
        final int leftButton = (int) data.getValue(SimpleKeysData.ATTRIBUTE_LEFT_BUTTON);
        final int rightButton = (int) data.getValue(SimpleKeysData.ATTRIBUTE_RIGHT_BUTTON);
        final int reedRelay = (int) data.getValue(SimpleKeysData.ATTRIBUTE_REED_RELAY);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelLeftButton.setText(button_status(leftButton));
                labelRightButton.setText(button_status(rightButton));
                labelReedRelay.setText(button_status(reedRelay));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_simple_keys, container, false);
        handler = new Handler();
        init_widgets();
        return view;
    }

    @Override
    public void onDestroy() {
        if( modelObservable != null )
            modelObservable.deleteObserver(this);
        Log.i("Frag", "Deleted from observable");
        super.onDestroy();
    }

    private void init_widgets()
    {
        labelLeftButton = (TextView) view.findViewById(R.id.label_left_button_state);
        labelRightButton = (TextView) view.findViewById(R.id.label_right_button_state);
        labelReedRelay = (TextView) view.findViewById(R.id.label_reed_relay_state);
    }

    private String button_status(int status)
    {
        return status == 1 ? "On" : "Off";
    }
}
