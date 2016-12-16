package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBTableIRTemperatureFactory implements DBTableFactoryInterface {
    @Override
    public DBTableInterface createTable() {
        return new DBTableIRTemperature();
    }
}
