package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBTableHumidityParamFactory implements DBTableFactoryInterface {
    @Override
    public DBTableInterface createTable() {
        return new DBTableHumidityParam();
    }
}
