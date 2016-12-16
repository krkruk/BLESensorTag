package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertIRTemperatureFactory extends DBInsertFactoryAbstract {
    public DBInsertIRTemperatureFactory(DBRowWriter dbWriter, String tableName) {
        super(dbWriter, tableName);
    }

    @Override
    public DBInsertInterface createRow() {
        return new DBInsertIRTemperature(getDbWriter(), getTableName());
    }
}
