package pl.projektorion.krzysztof.blesensortag.factories;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerParam;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidityParam;
import pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature.DBSelectIRTemperatureParam;
import pl.projektorion.krzysztof.blesensortag.database.selects.Movement.DBSelectMovementParam;
import pl.projektorion.krzysztof.blesensortag.database.selects.OpticalSensor.DBSelectOpticalSensorParam;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBFactoryParamSelects {
    private DBSelectInterface rootRecord;
    private List<DBQueryListenerInterface> queryListeners;

    public DBFactoryParamSelects(DBSelectInterface rootRecord) {
        this.rootRecord = rootRecord;
        init_query_listeners();
    }

    public List<DBQueryListenerInterface> getQueryListeners()
    {
        return queryListeners;
    }

    private void init_query_listeners()
    {
        queryListeners = new ArrayList<>();

        queryListeners.add(new DBSelectBarometerParam(rootRecord));
        queryListeners.add(new DBSelectHumidityParam(rootRecord));
        queryListeners.add(new DBSelectIRTemperatureParam(rootRecord));
        queryListeners.add(new DBSelectMovementParam(rootRecord));
        queryListeners.add(new DBSelectOpticalSensorParam(rootRecord));
    }
}
