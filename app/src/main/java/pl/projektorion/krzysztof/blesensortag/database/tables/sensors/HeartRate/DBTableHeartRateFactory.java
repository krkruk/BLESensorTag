package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBTableHeartRateFactory implements DBTableFactoryInterface {

    @Override
    public DBTableInterface createTable() {
        return new DBTableHeartRate();
    }
}
