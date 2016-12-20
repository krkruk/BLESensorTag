package pl.projektorion.krzysztof.blesensortag.database.selects.Humidity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectHumidityParamData extends DBSelectGeneralSensorParamData {

    public DBSelectHumidityParamData() {
        super();
    }

    public DBSelectHumidityParamData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectHumidityParamData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectHumidityParamData> CREATOR = new Creator<DBSelectHumidityParamData>() {
        @Override
        public DBSelectHumidityParamData createFromParcel(Parcel source) {
            return new DBSelectHumidityParamData(source);
        }

        @Override
        public DBSelectHumidityParamData[] newArray(int size) {
            return new DBSelectHumidityParamData[size];
        }
    };
}
