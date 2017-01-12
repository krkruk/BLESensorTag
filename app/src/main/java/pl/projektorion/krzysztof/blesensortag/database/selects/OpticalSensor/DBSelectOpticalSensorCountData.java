package pl.projektorion.krzysztof.blesensortag.database.selects.OpticalSensor;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountDataAbstract;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectOpticalSensorCountData extends DBSelectSensorCountDataAbstract {
    public DBSelectOpticalSensorCountData() {
        super();
    }

    public DBSelectOpticalSensorCountData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectOpticalSensorCountData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectOpticalSensorCountData> CREATOR = new Creator<DBSelectOpticalSensorCountData>() {
        @Override
        public DBSelectOpticalSensorCountData createFromParcel(Parcel source) {
            return new DBSelectOpticalSensorCountData(source);
        }

        @Override
        public DBSelectOpticalSensorCountData[] newArray(int size) {
            return new DBSelectOpticalSensorCountData[size];
        }
    };
}
