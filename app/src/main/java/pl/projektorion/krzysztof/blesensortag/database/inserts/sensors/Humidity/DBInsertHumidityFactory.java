package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Humidity;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactoryAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBInsertHumidityFactory extends DBInsertFactoryAbstract {
    public DBInsertHumidityFactory(DBRowWriter dbWriter) {
        super(dbWriter);
    }

    @Override
    public DBInsertInterface create() {
        return new DBInsertHumidity(getDbWriter());
    }
}
