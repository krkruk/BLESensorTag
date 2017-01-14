package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.IRTemperature;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectIRTemperatureParamData extends DBSelectGeneralSensorParamData {
    public DBSelectIRTemperatureParamData() {
        super();
    }

    public DBSelectIRTemperatureParamData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectIRTemperatureParamData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectIRTemperatureParamData> CREATOR = new Creator<DBSelectIRTemperatureParamData>() {
        @Override
        public DBSelectIRTemperatureParamData createFromParcel(Parcel source) {
            return new DBSelectIRTemperatureParamData(source);
        }

        @Override
        public DBSelectIRTemperatureParamData[] newArray(int size) {
            return new DBSelectIRTemperatureParamData[size];
        }
    };
}
