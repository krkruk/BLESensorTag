package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private static final String ACTION_LEFT_BTN_STATUS =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.action.LEFT_BTN_STATUS";
    private static final String ACTION_RIGHT_BTN_STATUS =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.action.RIGHT_BTN_STATUS";
    private static final String ACTION_REED_RELAY_STATUS =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.action.REED_RELAY_STATUS";

    private View view;
    private ImageView imgLeftButton;
    private ImageView imgRightButton;
    private ImageView imgReedRelay;

    private boolean leftButtonState = false;
    private boolean rightButtonState = false;
    private boolean reedRelayState = false;

    private Handler handler;
    private Observable modelObservable;

    public SimpleKeysFragment() {
    }

    @Override
    public void update(Observable o, Object arg) {
        modelObservable = o;
        final AbstractProfileData data = (AbstractProfileData) arg;
        leftButtonState = data.getValue(SimpleKeysData.ATTRIBUTE_LEFT_BUTTON) > 0;
        rightButtonState = data.getValue(SimpleKeysData.ATTRIBUTE_RIGHT_BUTTON) > 0;
        reedRelayState = data.getValue(SimpleKeysData.ATTRIBUTE_REED_RELAY) > 0;

        handler.post(new Runnable() {
            @Override
            public void run() {
                set_left_button_img(leftButtonState);
                set_right_button_img(rightButtonState);
                set_reed_relay(reedRelayState);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        if( savedInstanceState != null )
        {
            leftButtonState = savedInstanceState.getBoolean(ACTION_LEFT_BTN_STATUS, false);
            rightButtonState = savedInstanceState.getBoolean(ACTION_RIGHT_BTN_STATUS, false);
            reedRelayState = savedInstanceState.getBoolean(ACTION_REED_RELAY_STATUS, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_simple_keys, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onDestroy() {
        if( modelObservable != null )
            modelObservable.deleteObserver(this);

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ACTION_LEFT_BTN_STATUS, leftButtonState);
        outState.putBoolean(ACTION_RIGHT_BTN_STATUS, rightButtonState);
        outState.putBoolean(ACTION_REED_RELAY_STATUS, reedRelayState);
        super.onSaveInstanceState(outState);
    }

    private void init_widgets()
    {
        imgLeftButton = (ImageView) view.findViewById(R.id.left_button_img);
        imgRightButton = (ImageView) view.findViewById(R.id.right_button_img);
        imgReedRelay = (ImageView) view.findViewById(R.id.reed_relay_img);

        set_left_button_img(leftButtonState);
        set_right_button_img(rightButtonState);
        set_reed_relay(reedRelayState);
    }

    private void set_left_button_img(boolean isOnState)
    {
        imgLeftButton.setImageResource(get_img_resource(isOnState));
    }

    private void set_right_button_img(boolean isOnState)
    {
        imgRightButton.setImageResource(get_img_resource(isOnState));
    }

    private void set_reed_relay(boolean isOnState)
    {
        imgReedRelay.setImageResource(get_img_resource(isOnState));
    }

    private int get_img_resource(boolean isOnState)
    {
        return isOnState ? R.drawable.button_on_green : R.drawable.button_off;
    }
}
