package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamFactoryAbstract;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertOpticalSensorParamFactory extends DBInsertParamFactoryAbstract {
    public DBInsertOpticalSensorParamFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface create() {
        return new DBInsertOpticalSensorParam(getDbWriter());
    }
}
