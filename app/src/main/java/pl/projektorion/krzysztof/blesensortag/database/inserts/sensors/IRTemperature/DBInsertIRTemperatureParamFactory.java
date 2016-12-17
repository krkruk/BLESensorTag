package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamFactoryAbstract;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertIRTemperatureParamFactory extends DBInsertParamFactoryAbstract {
    public DBInsertIRTemperatureParamFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface create() {
        return new DBInsertIRTemperatureParam(getDbWriter());
    }
}
