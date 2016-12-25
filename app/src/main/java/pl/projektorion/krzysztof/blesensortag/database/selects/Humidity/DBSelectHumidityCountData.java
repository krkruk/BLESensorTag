package pl.projektorion.krzysztof.blesensortag.database.selects.Humidity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorCountDataAbstract;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectHumidityCountData extends DBSelectSensorCountDataAbstract {

    public DBSelectHumidityCountData() {
        super();
    }

    public DBSelectHumidityCountData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectHumidityCountData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectHumidityCountData> CREATOR = new Creator<DBSelectHumidityCountData>() {
        @Override
        public DBSelectHumidityCountData createFromParcel(Parcel source) {
            return new DBSelectHumidityCountData(source);
        }

        @Override
        public DBSelectHumidityCountData[] newArray(int size) {
            return new DBSelectHumidityCountData[size];
        }
    };
}
