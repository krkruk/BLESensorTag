package pl.projektorion.krzysztof.blesensortag.fragments.database.HeartRate;

import android.app.Fragment;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBPresentHeartRateFagmentFactory implements FragmentFactory {
    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;

    public DBPresentHeartRateFagmentFactory(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        this.rootRecord = rootRecord;
        this.sensorRecord = sensorRecord;
    }

    @Override
    public Fragment create() {
        return DBPresentHeartRateFragment.newInstance(rootRecord, sensorRecord);
    }
}
