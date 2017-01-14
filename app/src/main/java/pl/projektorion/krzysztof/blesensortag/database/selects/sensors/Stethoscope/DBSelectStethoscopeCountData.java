package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountDataAbstract;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBSelectStethoscopeCountData extends DBSelectSensorCountDataAbstract {
    public DBSelectStethoscopeCountData() {
        super();
    }

    public DBSelectStethoscopeCountData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectStethoscopeCountData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectStethoscopeCountData> CREATOR = new Creator<DBSelectStethoscopeCountData>() {
        @Override
        public DBSelectStethoscopeCountData createFromParcel(Parcel source) {
            return new DBSelectStethoscopeCountData(source);
        }

        @Override
        public DBSelectStethoscopeCountData[] newArray(int size) {
            return new DBSelectStethoscopeCountData[size];
        }
    };
}
