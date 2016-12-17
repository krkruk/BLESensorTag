package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBTableOpticalSensorFactory implements DBTableFactoryInterface {
    @Override
    public DBTableInterface createTable() {
        return new DBTableOpticalSensor();
    }
}
