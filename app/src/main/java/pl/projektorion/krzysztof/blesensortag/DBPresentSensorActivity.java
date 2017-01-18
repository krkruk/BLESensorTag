package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import pl.projektorion.krzysztof.blesensortag.database.DBCSVIntentService;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecordData;
import pl.projektorion.krzysztof.blesensortag.factories.DBPresentSensorDescriptionFactory;
import pl.projektorion.krzysztof.blesensortag.factories.DBPresentSensorFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorDescriptionDefaultFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.database.DBPresentSensorFragmentAbstract;


public class DBPresentSensorActivity extends Activity {

    public static final String EXTRA_ROOT_RECORD =
            "pl.projektorion.krzysztof.blesensortag.extra.ROOT_RECORD";

    public static final String EXTRA_SENSOR_RECORD =
            "pl.projektorion.krzysztof.blesensortag.extra.SENSOR_RECORD";

    public static final String EXTRA_SENSOR_LABEL =
            "pl.projektorion.krzysztof.blesensortag.extra.SENSOR_LABEL";

    public static final String NEGOTIATE_DESCRIPTION_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.DESCRIPTION_FRAGMENT_TAG";

    public static final String NEGOTIATE_PRESENTATION_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.PRESENTATION_FRAGMENT_TAG";

    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;
    private String sensorLabel;

    private FrameLayout descriptionSink;
    private FrameLayout presentationSink;

    private DBPresentSensorDescriptionFactory descriptionFragFactory;
    private DBPresentSensorFactory presentationFragFactory;
    private Fragment descriptionFrag;
    private Fragment presentationFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbpresent_sensor);

        if( !restore_saved_instance(savedInstanceState) )
            acquire_data();
        init_objects();
        init_widgets();

        negotiate_description_fragment();
        negotiate_presentation_fragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_db_present_sensor_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        outState.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        outState.putString(EXTRA_SENSOR_LABEL, sensorLabel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_csv_export:
                request_export_csv();
                break;
            default:
                return false;
        }
        return true;
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
        descriptionFragFactory = new DBPresentSensorDescriptionFactory(sensorLabel);
        presentationFragFactory = new DBPresentSensorFactory(rootRecord, sensorRecord);
    }

    private void init_widgets()
    {
        presentationSink = (FrameLayout) findViewById(R.id.db_presentation_container);
        descriptionSink = (FrameLayout) findViewById(R.id.db_description_container);
    }



    private void negotiate_description_fragment()
    {
        FragmentManager fm = getFragmentManager();
        descriptionFrag = fm.findFragmentByTag(NEGOTIATE_DESCRIPTION_FRAGMENT_TAG);
        if( descriptionFrag == null )
        {
            descriptionFrag = descriptionFragFactory.create(sensorLabel);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(descriptionFrag, NEGOTIATE_DESCRIPTION_FRAGMENT_TAG);
            ft.replace(R.id.db_description_container, descriptionFrag);
            ft.commit();
        }
    }

    private void negotiate_presentation_fragment()
    {
        FragmentManager fm = getFragmentManager();
        presentationFrag = fm.findFragmentByTag(NEGOTIATE_PRESENTATION_FRAGMENT_TAG);
        if( presentationFrag == null )
        {
            presentationFrag = presentationFragFactory.create(sensorLabel);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(presentationFrag, NEGOTIATE_PRESENTATION_FRAGMENT_TAG);
            ft.replace(R.id.db_presentation_container, presentationFrag);
            ft.commit();
        }
    }

    private void request_export_csv()
    {
        DBPresentSensorFragmentAbstract frag;
        try {
            frag = (DBPresentSensorFragmentAbstract) presentationFrag;
        } catch (ClassCastException e)
        {
            Toast.makeText(this, R.string.toast_export_csv_start_error, Toast.LENGTH_LONG).show();
            return;
        }

        final Intent exportCsvService = new Intent(this, DBCSVIntentService.class);
        exportCsvService.putExtra(DBCSVIntentService.EXTRA_DATE_SECONDS,
                (long) rootRecord.getData(DBSelectRootRecordData.ATTRIBUTE_DATE_SECONDS));
        exportCsvService.putExtra(DBCSVIntentService.EXTRA_SENSOR_NAME, sensorLabel);
        exportCsvService.putExtra(DBCSVIntentService.EXTRA_QUERY, frag.getExportQuery());
        startService(exportCsvService);
    }
}
