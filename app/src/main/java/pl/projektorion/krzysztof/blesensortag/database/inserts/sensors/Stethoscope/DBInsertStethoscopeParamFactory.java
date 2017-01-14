package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Stethoscope;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertInterface;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBInsertStethoscopeParamFactory extends DBInsertParamFactoryAbstract {
    public DBInsertStethoscopeParamFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface create() {
        return new DBInsertStethoscopeParam(getDbWriter());
    }
}
