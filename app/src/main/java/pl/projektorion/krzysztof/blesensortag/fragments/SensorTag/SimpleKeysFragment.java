package pl.projektorion.krzysztof.blesensortag.fragments.SensorTag;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.projektorion.krzysztof.blesensortag.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleKeysFragment extends Fragment {

    private View view;
    private TextView labelLeftButton;
    private TextView labelRightButton;
    private TextView labelReedRelay;

    public SimpleKeysFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_simple_keys, container, false);
        init_widgets();
        return view;
    }

    private void init_widgets()
    {
        labelLeftButton = (TextView) view.findViewById(R.id.label_left_button_state);
        labelRightButton = (TextView) view.findViewById(R.id.label_right_button_state);
        labelReedRelay = (TextView) view.findViewById(R.id.label_reed_relay_state);
    }
}
