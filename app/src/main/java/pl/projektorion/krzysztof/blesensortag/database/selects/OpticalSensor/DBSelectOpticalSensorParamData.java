package pl.projektorion.krzysztof.blesensortag.database.selects.OpticalSensor;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectOpticalSensorParamData extends DBSelectGeneralSensorParamData {
    public DBSelectOpticalSensorParamData() {
        super();
    }

    public DBSelectOpticalSensorParamData(Cursor cursor) {
        super(cursor);
    }

    public DBSelectOpticalSensorParamData(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<DBSelectOpticalSensorParamData> CREATOR = new Creator<DBSelectOpticalSensorParamData>() {
        @Override
        public DBSelectOpticalSensorParamData createFromParcel(Parcel source) {
            return new DBSelectOpticalSensorParamData(source);
        }

        @Override
        public DBSelectOpticalSensorParamData[] newArray(int size) {
            return new DBSelectOpticalSensorParamData[size];
        }
    };
}
