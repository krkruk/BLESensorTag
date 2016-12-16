package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity;

import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBTableHumidityParamFactory implements DBTableFactoryInterface {
    @Override
    public DBTableInterface createTable() {
        return new DBTableHumidityParam();
    }
}
