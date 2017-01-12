package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.factories.DBPresentSensorFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.Movement.DBPresentMovementFragment;


public class DBPresentSensorActivity extends Activity {

    public static final String EXTRA_ROOT_RECORD =
            "pl.projektorion.krzysztof.blesensortag.extra.ROOT_RECORD";

    public static final String EXTRA_SENSOR_RECORD =
            "pl.projektorion.krzysztof.blesensortag.extra.SENSOR_RECORD";

    public static final String EXTRA_SENSOR_LABEL =
            "pl.projektorion.krzysztof.blesensortag.extra.SENSOR_LABEL";

    public static final String NEGOTIATE_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.NEGOTIATE_FRAGMENT";

    public static final String ACTION_SENSOR_PARAMS =
            "pl.projektorion.krzysztof.blesensortag.action.SENSOR_PARAMS";

    public static final String EXTRA_NUMBER_OF_RECORDS =
            "pl.projektorion.krzysztof.blesensortag.extra.NUMBER_OF_RECORDS";

    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;
    private String sensorLabel;

    private TextView sensorPresentationLabel;
    private TextView sensorRecordNumberLabel;
    private FrameLayout fragmentSink;

    private DBPresentSensorFactory fragmentFactory;
    private Fragment fragment;

    private LocalBroadcastManager broadcastManager;

    /*
    * Display number of records available to read. It is optional.e
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final long numberOfRecords = intent.getLongExtra(EXTRA_NUMBER_OF_RECORDS, 0);
            final String nOfRecLabel = String.format(Locale.getDefault(), "%s: %d",
                    getString(R.string.label_total_records), numberOfRecords);

            if( sensorRecordNumberLabel != null )
                sensorRecordNumberLabel.setText(nOfRecLabel);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbpresent_sensor);

        if( !restore_saved_instance(savedInstanceState) )
            acquire_data();
        init_objects();
        init_widgets();
        init_broadcast_receivers();

        negotiate_fragment();
    }

    @Override
    protected void onDestroy() {
        kill_broadcast_receivers();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        outState.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        outState.putString(EXTRA_SENSOR_LABEL, sensorLabel);
    }

    private boolean restore_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null )
            return false;
        rootRecord = savedInstanceState.getParcelable(EXTRA_ROOT_RECORD);
        sensorRecord = savedInstanceState.getParcelable(EXTRA_SENSOR_RECORD);
        sensorLabel = savedInstanceState.getString(EXTRA_SENSOR_LABEL);
        return true;
    }

    private void acquire_data() throws NullPointerException
    {
        final Intent intent = getIntent();
        rootRecord = intent.getParcelableExtra(EXTRA_ROOT_RECORD);
        sensorRecord = intent.getParcelableExtra(EXTRA_SENSOR_RECORD);
        sensorLabel = intent.getStringExtra(EXTRA_SENSOR_LABEL);

        if( rootRecord == null || sensorRecord == null || sensorLabel == null)
            throw new NullPointerException("No data acquired. Pass EXTRAS!");

        Log.i("Data", rootRecord.toString());
        Log.i("Sensor", sensorRecord.toString());
        Log.i("Sensor name", sensorLabel);
    }

    private void init_objects()
    {
        fragmentFactory = new DBPresentSensorFactory(rootRecord, sensorRecord);
    }

    private void init_widgets()
    {
        sensorPresentationLabel = (TextView) findViewById(R.id.sensor_presentation_label);
        sensorRecordNumberLabel = (TextView) findViewById(R.id.sensor_record_number_label);
        fragmentSink = (FrameLayout) findViewById(R.id.db_presentation_container);

        sensorPresentationLabel.setText(sensorLabel);
    }

    private void init_broadcast_receivers()
    {
        broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.registerReceiver(receiver, new IntentFilter(ACTION_SENSOR_PARAMS));
    }

    private void negotiate_fragment()
    {
        FragmentManager fm = getFragmentManager();
        fragment = fm.findFragmentByTag(NEGOTIATE_FRAGMENT_TAG);
        if( fragment == null )
        {
            fragment = fragmentFactory.create(sensorLabel);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(fragment, NEGOTIATE_FRAGMENT_TAG);
            ft.replace(R.id.db_presentation_container, fragment);
            ft.commit();
        }
    }

    private void kill_broadcast_receivers()
    {
        broadcastManager.unregisterReceiver(receiver);
    }
}
