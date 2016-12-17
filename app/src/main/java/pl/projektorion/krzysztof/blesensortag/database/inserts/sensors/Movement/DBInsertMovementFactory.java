package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertMovementFactory extends DBInsertFactoryAbstract {
    public DBInsertMovementFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface create() {
        return new DBInsertMovement(getDbWriter());
    }
}
