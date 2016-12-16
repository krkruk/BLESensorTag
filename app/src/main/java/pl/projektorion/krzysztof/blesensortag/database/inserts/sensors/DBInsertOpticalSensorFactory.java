package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertOpticalSensorFactory extends DBInsertFactoryAbstract {
    public DBInsertOpticalSensorFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface createRow() {
        return new DBInsertOpticalSensor(getDbWriter());
    }
}
