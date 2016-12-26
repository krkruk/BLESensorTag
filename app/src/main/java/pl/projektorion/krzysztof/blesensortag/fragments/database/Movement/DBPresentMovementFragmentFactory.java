package pl.projektorion.krzysztof.blesensortag.fragments.database.Movement;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;

/**
 * Created by krzysztof on 26.12.16.
 */

public class DBPresentMovementFragmentFactory implements FragmentFactory {
    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;

    public DBPresentMovementFragmentFactory(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        this.rootRecord = rootRecord;
        this.sensorRecord = sensorRecord;
    }

    @Override
    public Fragment create() {
        return DBPresentMovementFragment.newInstance(rootRecord, sensorRecord);
    }
}
