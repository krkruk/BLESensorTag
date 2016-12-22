package pl.projektorion.krzysztof.blesensortag.database.commands;

import android.os.Parcelable;

/**
 * Created by krzysztof on 22.12.16.
 */

public interface DBQueryWithLimitsListenerInterface extends DBQueryListenerInterface, Parcelable {

    void setLimit(long startAt, long noElements);
}
