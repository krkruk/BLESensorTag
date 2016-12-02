package pl.projektorion.krzysztof.blesensortag.database.rows.sensors;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRowFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRowInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBRowBarometerFactory extends DBRowFactoryAbstract {
    public DBRowBarometerFactory(DBRowWriter dbWriter, String tableName) {
        super(dbWriter, tableName);
    }

    @Override
    public DBRowInterface createRow() {
        return new DBRowBarometer(getDbWriter(), getTableName());
    }
}
