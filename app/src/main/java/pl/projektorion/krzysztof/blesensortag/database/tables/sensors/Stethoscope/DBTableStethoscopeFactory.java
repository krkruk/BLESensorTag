package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBTableStethoscopeFactory implements DBTableFactoryInterface {
    @Override
    public DBTableInterface createTable() {
        return new DBTableStethoscope();
    }
}
