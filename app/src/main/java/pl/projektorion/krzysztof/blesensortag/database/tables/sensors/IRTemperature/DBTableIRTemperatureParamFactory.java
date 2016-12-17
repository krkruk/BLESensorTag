package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBTableIRTemperatureParamFactory implements DBTableFactoryInterface {
    @Override
    public DBTableInterface createTable() {
        return new DBTableIRTemperatureParam();
    }
}
