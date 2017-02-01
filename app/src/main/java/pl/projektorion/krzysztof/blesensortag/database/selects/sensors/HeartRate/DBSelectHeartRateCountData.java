package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountDataAbstract;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBSelectHeartRateCountData extends DBSelectSensorCountDataAbstract {

    public DBSelectHeartRateCountData() {
        super();
    }

    public DBSelectHeartRateCountData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectHeartRateCountData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectHeartRateCountData> CREATOR = new Creator<DBSelectHeartRateCountData>() {
        @Override
        public DBSelectHeartRateCountData createFromParcel(Parcel source) {
            return new DBSelectHeartRateCountData(source);
        }

        @Override
        public DBSelectHeartRateCountData[] newArray(int size) {
            return new DBSelectHeartRateCountData[size];
        }
    };
}
