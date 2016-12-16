package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertMovementFactory extends DBInsertFactoryAbstract {
    public DBInsertMovementFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface createRow() {
        return new DBInsertMovement(getDbWriter());
    }
}
