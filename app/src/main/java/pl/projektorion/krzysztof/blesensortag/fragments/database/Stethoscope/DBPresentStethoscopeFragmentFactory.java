package pl.projektorion.krzysztof.blesensortag.fragments.database.Stethoscope;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBPresentStethoscopeFragmentFactory implements FragmentFactory {
    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;

    public DBPresentStethoscopeFragmentFactory(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        this.rootRecord = rootRecord;
        this.sensorRecord = sensorRecord;
    }

    @Override
    public Fragment create() {
        return DBPresentStethoscopeFragment.newInstance(rootRecord, sensorRecord);
    }
}
