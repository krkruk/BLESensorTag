package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.fragments.app.DBSensorDisplayFragment;

public class DBSelectSensorActivity extends Activity {

    public static final String EXTRA_ROOT_RECORD_DATA =
            "pl.projektorion.krzysztof.blesensortag.extra.ROOT_RECORD_DATA";

    private static final String SENSOR_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.SENSOR_FRAGMENT";

    private Fragment fragment;
    private DBSelectInterface rootRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbselect_sensor);

        acquire_data();
        negotiate_fragment();
    }

    private void acquire_data()
    {
        final Intent rootIntent = getIntent();
        rootRecord = rootIntent.getParcelableExtra(EXTRA_ROOT_RECORD_DATA);
    }

    private void negotiate_fragment()
    {
        FragmentManager fm = getFragmentManager();
        fragment = fm.findFragmentByTag(SENSOR_FRAGMENT_TAG);
        if( fragment == null )
        {
            fragment = DBSensorDisplayFragment.newInstance(rootRecord);
            ((DBSensorDisplayFragment)fragment).setIntentClass(DBPresentSensorActivity.class);
            ((DBSensorDisplayFragment)fragment).setIntentExtraRootRecord(
                    DBPresentSensorActivity.EXTRA_ROOT_RECORD);
            ((DBSensorDisplayFragment)fragment).setIntentExtraSensorRecord(
                    DBPresentSensorActivity.EXTRA_SENSOR_RECORD);
            ((DBSensorDisplayFragment)fragment).setIntentExtraSensorLabel(
                    DBPresentSensorActivity.EXTRA_SENSOR_LABEL);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(fragment, SENSOR_FRAGMENT_TAG);
            ft.replace(R.id.sensor_frame_layout, fragment);
            ft.commit();
        }
    }
}
