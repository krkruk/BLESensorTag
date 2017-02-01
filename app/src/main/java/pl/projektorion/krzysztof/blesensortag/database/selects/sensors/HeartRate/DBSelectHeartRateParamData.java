package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBSelectHeartRateParamData extends DBSelectGeneralSensorParamData {
    public DBSelectHeartRateParamData() {
        super();
    }

    public DBSelectHeartRateParamData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectHeartRateParamData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectHeartRateParamData> CREATOR = new Creator<DBSelectHeartRateParamData>() {
        @Override
        public DBSelectHeartRateParamData createFromParcel(Parcel source) {
            return new DBSelectHeartRateParamData(source);
        }

        @Override
        public DBSelectHeartRateParamData[] newArray(int size) {
            return new DBSelectHeartRateParamData[size];
        }
    };
}
