package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Barometer;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectBarometerParamData extends DBSelectGeneralSensorParamData {

    public DBSelectBarometerParamData() {
        super();
    }

    public DBSelectBarometerParamData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectBarometerParamData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectBarometerParamData> CREATOR = new Creator<DBSelectBarometerParamData>() {
        @Override
        public DBSelectBarometerParamData createFromParcel(Parcel source) {
            return new DBSelectBarometerParamData(source);
        }

        @Override
        public DBSelectBarometerParamData[] newArray(int size) {
            return new DBSelectBarometerParamData[size];
        }
    };
}
