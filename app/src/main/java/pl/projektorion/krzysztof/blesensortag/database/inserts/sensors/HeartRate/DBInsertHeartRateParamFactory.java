package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.HeartRate;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertInterface;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBInsertHeartRateParamFactory extends DBInsertParamFactoryAbstract {
    public DBInsertHeartRateParamFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface create() {
        return new DBInsertHeartRateParam(getDbWriter());
    }
}
