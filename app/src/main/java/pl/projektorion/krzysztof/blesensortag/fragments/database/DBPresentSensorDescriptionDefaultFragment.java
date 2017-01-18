package pl.projektorion.krzysztof.blesensortag.fragments.database;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentSensorDescriptionDefaultFragment extends Fragment {

    public static final String EXTRA_SENSOR_LABEL =
            "pl.projektorion.krzysztof.blesensortag.fragments";

    private View view;
    private Context appContext;

    private TextView sensorPresentationLabel;
    private TextView sensorRecordNumberLabel;

    private LocalBroadcastManager broadcastManager;

    private String sensorLabel;

    /*
    * Display number of records available to read. It is optional.e
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final long numberOfRecords = intent.getLongExtra(DBPresentSensorFragmentAbstract.EXTRA_NUMBER_OF_RECORDS, 0);
            final String nOfRecLabel = String.format(Locale.getDefault(), "%s: %d",
                    getString(R.string.label_total_records), numberOfRecords);

            if( sensorRecordNumberLabel != null )
                sensorRecordNumberLabel.setText(nOfRecLabel);
        }
    };

    public DBPresentSensorDescriptionDefaultFragment() {
        // Required empty public constructor
    }

    public static DBPresentSensorDescriptionDefaultFragment newInstance(String sensorLabel) {

        Bundle args = new Bundle();
        args.putString(EXTRA_SENSOR_LABEL, sensorLabel);
        DBPresentSensorDescriptionDefaultFragment fragment = new DBPresentSensorDescriptionDefaultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_objects();
        if( !restore_saved_instance(savedInstanceState) )
            acquire_data();
        init_broadcast_receivers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbpresent_sensor_description_default, container, false);
        init_widgets();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_SENSOR_LABEL, sensorLabel);
    }

    @Override
    public void onDestroy() {
        kill_broadcast_receivers();
        super.onDestroy();
    }

    private void init_objects()
    {
        appContext = getActivity().getApplicationContext();
    }

    private boolean restore_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null )
            return false;
        sensorLabel = savedInstanceState.getString(EXTRA_SENSOR_LABEL);
        return true;
    }

    private void acquire_data()
    {
        final Bundle bundle = getArguments();
        sensorLabel = bundle.getString(EXTRA_SENSOR_LABEL);
    }

    private void init_widgets()
    {
        sensorPresentationLabel = (TextView) view.findViewById(R.id.sensor_presentation_label);
        sensorRecordNumberLabel = (TextView) view.findViewById(R.id.sensor_record_number_label);

        sensorPresentationLabel.setText(sensorLabel);
    }

    private void init_broadcast_receivers()
    {
        broadcastManager = LocalBroadcastManager.getInstance(appContext);
        broadcastManager.registerReceiver(receiver, new IntentFilter(DBPresentSensorFragmentAbstract.ACTION_SENSOR_PARAMS));
    }

    private void kill_broadcast_receivers()
    {
        broadcastManager.unregisterReceiver(receiver);
    }
}
