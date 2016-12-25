package pl.projektorion.krzysztof.blesensortag.database.commands;

import android.os.Parcelable;

/**
 * Created by krzysztof on 22.12.16.
 */

public interface DBQueryWithLimitsListenerInterface extends DBQueryParcelableListenerInterface {

    /**
     * Define limit the data should be read.
     * @param startAt - initial position the data reading should begin
     * @param noElements - number of elements to be read (maximally)
     */
    void setLimit(long startAt, long noElements);
}
