package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbpresent_sensor);
        acquire_data();
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
}
