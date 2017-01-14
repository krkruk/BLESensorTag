package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Stethoscope;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertInterface;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBInsertStethoscopeFactory extends DBInsertFactoryAbstract {

    public DBInsertStethoscopeFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface create() {
        return new DBInsertStethoscope(getDbWriter());
    }
}
