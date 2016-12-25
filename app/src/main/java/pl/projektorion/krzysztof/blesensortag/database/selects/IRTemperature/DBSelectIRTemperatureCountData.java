package pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorCountDataAbstract;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectIRTemperatureCountData extends DBSelectSensorCountDataAbstract {
    public DBSelectIRTemperatureCountData() {
        super();
    }

    public DBSelectIRTemperatureCountData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectIRTemperatureCountData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectIRTemperatureCountData> CREATOR = new Creator<DBSelectIRTemperatureCountData>() {
        @Override
        public DBSelectIRTemperatureCountData createFromParcel(Parcel source) {
            return new DBSelectIRTemperatureCountData(source);
        }

        @Override
        public DBSelectIRTemperatureCountData[] newArray(int size) {
            return new DBSelectIRTemperatureCountData[size];
        }
    };
}
