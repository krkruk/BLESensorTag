package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBSelectStethoscopeParamData extends DBSelectGeneralSensorParamData {
    public DBSelectStethoscopeParamData() {
        super();
    }

    public DBSelectStethoscopeParamData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectStethoscopeParamData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectStethoscopeParamData> CREATOR = new Creator<DBSelectStethoscopeParamData>() {
        @Override
        public DBSelectStethoscopeParamData createFromParcel(Parcel source) {
            return new DBSelectStethoscopeParamData(source);
        }

        @Override
        public DBSelectStethoscopeParamData[] newArray(int size) {
            return new DBSelectStethoscopeParamData[size];
        }
    };
}
