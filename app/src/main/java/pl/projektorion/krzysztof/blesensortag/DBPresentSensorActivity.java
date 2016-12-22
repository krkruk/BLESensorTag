package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometer;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidity;
import pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature.DBSelectIRTemperature;
import pl.projektorion.krzysztof.blesensortag.database.selects.OpticalSensor.DBSelectOpticalSensor;

public class DBPresentSensorActivity extends Activity {

    public static final String EXTRA_ROOT_RECORD =
            "pl.projektorion.krzysztof.blesensortag.extra.ROOT_RECORD";

    public static final String EXTRA_SENSOR_RECORD =
            "pl.projektorion.krzysztof.blesensortag.extra.SENSOR_RECORD";

    public static final String EXTRA_SENSOR_LABEL =
            "pl.projektorion.krzysztof.blesensortag.extra.SENSOR_LABEL";

    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;
    private String sensorLabel;

    private TextView sensorPresentationLabel;
    private FrameLayout fragmentSink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbpresent_sensor);

        if( !restore_saved_instance(savedInstanceState) )
            acquire_data();
        init_widgets();

//        final Intent serviceBarometer = new Intent(this, DBSelectIntentService.class);
//        final DBSelectBarometer barometer = new DBSelectBarometer(rootRecord, sensorRecord);
//        serviceBarometer.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, barometer);
//        startService(serviceBarometer);

        final DBSelectOpticalSensor sensor = new DBSelectOpticalSensor(rootRecord, sensorRecord);
        final Intent intent = new Intent(this, DBSelectIntentService.class);
        intent.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, sensor);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
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
        Log.i("TableName", sensorLabel);
    }

    private void init_widgets()
    {
        sensorPresentationLabel = (TextView) findViewById(R.id.sensor_presentation_label);
        fragmentSink = (FrameLayout) findViewById(R.id.db_presentation_container);

        sensorPresentationLabel.setText(sensorLabel);
    }
}
