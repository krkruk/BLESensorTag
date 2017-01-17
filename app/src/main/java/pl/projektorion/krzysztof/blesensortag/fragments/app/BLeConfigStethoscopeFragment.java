package pl.projektorion.krzysztof.blesensortag.fragments.app;


import android.app.ActionBar;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile.StethoscopeProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattIOService;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattProfiles;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLeConfigStethoscopeFragment extends Fragment
    implements CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener
{

    public static final String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.BLE_DEVICE";

    private static final String EXTRA_SERVICE_SCANNED =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.SERVICE_SCANNED";

    private Context appContext;
    private View view;
    private Handler handler;
    private TextView periodLabel;
    private Switch enableSwitch;
    private SeekBar seekBar;
    private int PERIOD_MIN_VALUE = 0x0a;
    private int seekBarProgress = PERIOD_MIN_VALUE;

    private BluetoothDevice bleDevice;
    private BLeGattIOService gattService;
    private LocalBroadcastManager broadcaster;
    private boolean wereServicesScanned = false;

    private StethoscopeProfile stethoscopeProfile;
    private  BLeAvailableGattProfiles profiles = new BLeAvailableGattProfiles();


    private BroadcastReceiver serviceGattReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeGattIOService.ACTION_GATT_CONNECTED.equals(action))
            {
                if( wereServicesScanned ) return;

                display_status(R.string.status_connected);
                stethoscopeProfile = new StethoscopeProfile(gattService);
                profiles.put(StethoscopeProfile.STETHOSCOPE_SERVICE, stethoscopeProfile);

                seekBar.setProgress((int) (stethoscopeProfile.getPeriod() / 10.0f));
                gattService.discoverServices();
            }
            else if(BLeGattIOService.ACTION_GATT_CONNECTING.equals(action))
            {
                display_status(R.string.status_connecting);
            }
            else if(BLeGattIOService.ACTION_GATT_DISCONNECTED.equals(action))
            {
                display_status(R.string.status_disconnected);
            }
            else if(BLeGattIOService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
            {
                wereServicesScanned = true;
                final List<BluetoothGattService> services = gattService.getServices();
                verify_stethoscope_profile_existence(services);
                enable_widgets(true);
            }
        }
    };

    private ServiceConnection bleServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gattService = ((BLeGattIOService.BLeGattIOBinder) service).getService();
            gattService.connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public BLeConfigStethoscopeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_framework();
        if( !restore_saved_instance(savedInstanceState) )
            retrieve_data();
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ble_config_stethoscope, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_BLE_DEVICE, bleDevice);
        outState.putBoolean(EXTRA_SERVICE_SCANNED, wereServicesScanned);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        load_widgets_state(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        kill_bound_services();
        kill_broadcast_receivers();
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if( stethoscopeProfile == null )
            return;
        stethoscopeProfile.enableNotification(isChecked);
        stethoscopeProfile.enableMeasurement(isChecked ?
                NotifyGattProfileInterface.ENABLE_ALL_MEASUREMENTS
                : NotifyGattProfileInterface.DISABLE_ALL_MEASUREMENTS);
    }

    public static BLeConfigStethoscopeFragment newInstance(BluetoothDevice bleDevice) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_BLE_DEVICE, bleDevice);
        BLeConfigStethoscopeFragment fragment = new BLeConfigStethoscopeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.seekBarProgress = progress;
        set_update_period_label( progress*10 );
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if( seekBarProgress < PERIOD_MIN_VALUE )
            seekBarProgress = PERIOD_MIN_VALUE;

        if( stethoscopeProfile != null )
            stethoscopeProfile.configurePeriod(seekBarProgress);
        seekBar.setProgress(seekBarProgress);
        set_update_period_label(stethoscopeProfile.getPeriod());
    }

    /**
     * Get BLE stethoscope profile
     * @return {@link BLeAvailableGattProfiles} Stethoscope profile wrapped in a container
     */
    public BLeAvailableGattProfiles getProfiles()
    {
        return profiles;
    }



    private void verify_stethoscope_profile_existence(List<BluetoothGattService> services)
    {
        boolean hasStethoscopeService = false;
        for( BluetoothGattService service : services )
        {
            if( StethoscopeProfile.STETHOSCOPE_SERVICE.equals(service.getUuid()) )
                hasStethoscopeService = true;
        }

        if( !hasStethoscopeService )
        {
            Toast.makeText(appContext,
                    R.string.toast_stethoscope_service_unavailable, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private void init_framework()
    {
        setRetainInstance(true);
        appContext = getActivity().getApplication();
        handler = new Handler();
    }

    private boolean restore_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null )
            return false;
        Log.i("Save", "In restore_saved_instance");
        bleDevice = savedInstanceState.getParcelable(EXTRA_BLE_DEVICE);
        return true;
    }

    private void retrieve_data()
    {Log.i("Save", "In retrieve_data");
        Bundle data = getArguments();
        bleDevice = data.getParcelable(EXTRA_BLE_DEVICE);
    }

    private void init_widgets()
    {
        enableSwitch = (Switch) view.findViewById(R.id.switch_notification_state);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar_period_state);
        periodLabel = (TextView) view.findViewById(R.id.period_state_label);

        enableSwitch.setOnCheckedChangeListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(100);
        set_update_period_label(1000);
        enable_widgets(wereServicesScanned);
    }

    private void load_widgets_state(Bundle savedInstanceState)
    {
        Log.i("SAVED", "In load_widgets_state");
        if( savedInstanceState == null ) return;
        wereServicesScanned = savedInstanceState.getBoolean(EXTRA_SERVICE_SCANNED);
        enable_widgets(wereServicesScanned);
        Log.i("SAVED", "ENDED");
    }

    private void init_broadcast_receivers()
    {
        IntentFilter serviceFilter = new IntentFilter();
        serviceFilter.addAction(BLeGattIOService.ACTION_GATT_SERVICES_DISCOVERED);
        serviceFilter.addAction(BLeGattIOService.ACTION_GATT_CONNECTED);
        serviceFilter.addAction(BLeGattIOService.ACTION_GATT_CONNECTING);
        serviceFilter.addAction(BLeGattIOService.ACTION_GATT_DISCONNECTED);

        broadcaster = LocalBroadcastManager.getInstance(appContext);
        broadcaster.registerReceiver(serviceGattReceiver, serviceFilter);
    }

    private void init_bound_services()
    {
        final Intent bleService = new Intent(appContext, BLeGattIOService.class);
        Log.i("BLEFRAG", bleDevice.toString());
        bleService.putExtra(BLeGattIOService.EXTRA_BLE_DEVICE, bleDevice);
        appContext.bindService(bleService, bleServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void kill_bound_services()
    {
        appContext.unbindService(bleServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(serviceGattReceiver);
    }

    private void enable_widgets(boolean state)
    {
        enableSwitch.setEnabled(state);
        seekBar.setEnabled(state);
    }

    private void display_status(int resId)
    {
        final ActionBar actionBar = getActivity().getActionBar();
        final int timeout = 2000;
        try {
            actionBar.setTitle(resId);
        } catch (NullPointerException e)
        {
            return;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actionBar.setTitle(R.string.app_name);
            }
        }, timeout);
    }

    private void set_update_period_label(double value)
    {
        if( periodLabel == null )
            return;

        final String label = String.format(Locale.getDefault(), "%s [%.2f ms]",
                getString(R.string.label_config_period),
                value);

        periodLabel.setText(label);
    }
}
