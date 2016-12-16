package pl.projektorion.krzysztof.blesensortag.fragments.config;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.NotifyGattProfileInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotifyProfileConfigFragment extends Fragment
    implements FragmentNotifyProfileConfigInterface {

    private View view;
    private Switch notificationSwitch;
    private Switch measurementSwitch;
    private SeekBar periodSeekBar;
    private int PERIOD_MIN_VALUE = 0x0a;

    private NotifyGattProfileInterface profile;
    private boolean notificationSwitchEnabled = true;
    private boolean measurementSwitchEnabled = true;
    private boolean periodSeekBarEnabled = true;


    private CompoundButton.OnCheckedChangeListener notificationWidgetListner =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if( profile != null )
                profile.enableNotification(isChecked);
        }
    };

    private CompoundButton.OnCheckedChangeListener measurementWidgetListener =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if( profile == null )
                return;
            final int enable = isChecked ? NotifyGattProfileInterface.ENABLE_ALL_MEASUREMENTS
                    : NotifyGattProfileInterface.DISABLE_ALL_MEASUREMENTS;
            profile.enableMeasurement(enable);
        }
    };

    private SeekBar.OnSeekBarChangeListener periodSeekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                private int progress = PERIOD_MIN_VALUE;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if( progress < PERIOD_MIN_VALUE )
                progress = PERIOD_MIN_VALUE;

            if( profile != null )
                profile.configurePeriod( progress );
            periodSeekBar.setProgress(progress);
        }
    };

    public FragmentNotifyProfileConfigFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notify_profile_config, container, false);
        init_widgets();
        init_start_widget_values();
        set_listeners();
        apply_widget_configs();
        return view;
    }

    public void setProfile(NotifyGattProfileInterface profile)
    {
        this.profile = profile;
    }

    public void setNotificationWidgetEnabled(boolean enabled)
    {
        notificationSwitchEnabled = enabled;
    }

    public void setMeasurementSwitchEnabled(boolean enabled)
    {
        measurementSwitchEnabled = enabled;
    }

    public void setPeriodSeekBarEnabled(boolean enabled)
    {
        periodSeekBarEnabled = enabled;
    }

    public void setPeriodMinValue(int value) {
        this.PERIOD_MIN_VALUE = value;
    }

    private void init_widgets()
    {
        notificationSwitch = (Switch) view.findViewById(R.id.switch_notification_state);
        measurementSwitch = (Switch) view.findViewById(R.id.switch_measurement_state);
        periodSeekBar = (SeekBar) view.findViewById(R.id.seekbar_period_state);
    }

    private void init_start_widget_values()
    {
        periodSeekBar.setProgress(64);

        if( profile != null ) {
            notificationSwitch.setChecked(profile.isNotifying());
            measurementSwitch.setChecked(profile.isMeasuring()
                    != NotifyGattProfileInterface.DISABLE_ALL_MEASUREMENTS);
            periodSeekBar.setProgress(profile.getPeriod());
        }
    }

    private void set_listeners()
    {
        notificationSwitch.setOnCheckedChangeListener(notificationWidgetListner);
        measurementSwitch.setOnCheckedChangeListener(measurementWidgetListener);
        periodSeekBar.setOnSeekBarChangeListener(periodSeekBarListener);
    }

    private void apply_widget_configs()
    {
        notificationSwitch.setEnabled(notificationSwitchEnabled);
        measurementSwitch.setEnabled(measurementSwitchEnabled);
        periodSeekBar.setEnabled(periodSeekBarEnabled);
    }

}
