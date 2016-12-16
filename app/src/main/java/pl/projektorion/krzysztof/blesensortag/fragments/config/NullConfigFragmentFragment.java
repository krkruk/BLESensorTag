package pl.projektorion.krzysztof.blesensortag.fragments.config;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class NullConfigFragmentFragment extends Fragment
    implements FragmentNotifyProfileConfigInterface {


    public NullConfigFragmentFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_null_config, container, false);
    }

    @Override
    public void setPeriodMinValue(int value) {}

    @Override
    public void setPeriodSeekBarEnabled(boolean enabled) {}

    @Override
    public void setMeasurementSwitchEnabled(boolean enabled) {}

    @Override
    public void setNotificationWidgetEnabled(boolean enabled) {}

    @Override
    public void setProfile(NotifyGattProfileInterface profile) {}
}
