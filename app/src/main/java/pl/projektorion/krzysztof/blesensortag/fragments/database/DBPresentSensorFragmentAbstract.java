package pl.projektorion.krzysztof.blesensortag.fragments.database;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidity;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidityCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidityCountData;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * Created by krzysztof on 25.12.16.
 */

public abstract class DBPresentSensorFragmentAbstract extends Fragment
implements ServiceDataReceiver.ReceiverListener {
    public static final String EXTRA_ROOT_RECORD =
            "pl.projektorion.krzysztof.blesensortag.fragments.database.extra.ROOT_RECORD";

    public static final String EXTRA_SENSOR_RECORD =
            "pl.projektorion.krzysztof.blesensortag.fragments.database.extra.SENSOR_RECORD";

    private Context context;
    private ServiceDataReceiver dataCounterReceiver;

    private DBSelectInterface rootRecord;
    private DBSelectInterface sensorRecord;
    private long availableRecords = 0;

    private DBSelectOnChartFlingListener flingListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init_objects();
        acquire_data();
    }

    @Override
    public void onStart() {
        super.onStart();

        request_record_count();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
        {
            List<? extends DBSelectInterface > data = resultData
                    .getParcelableArrayList(DBSelectIntentService.EXTRA_RESULT);
            if( data == null || data.size() <= 0) return;
            availableRecords = (long) data.get(0).getData(attribute_count());
            flingListener = new DBSelectOnChartFlingListener(availableRecords, 5);
            Log.i("COUNTED", "Val: " + availableRecords);
        }

        request_new_data();
    }

    public long getAvailableRecords() {
        return availableRecords;
    }

    public static DBPresentHumidityFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentHumidityFragment fragment = new DBPresentHumidityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected void init_objects()
    {
        context = getActivity().getApplicationContext();
        dataCounterReceiver = new ServiceDataReceiver(new Handler());
        dataCounterReceiver.setListener(this);
    }

    protected void acquire_data()
    {
        final Bundle bundle = getArguments();
        rootRecord = bundle.getParcelable(EXTRA_ROOT_RECORD);
        sensorRecord = bundle.getParcelable(EXTRA_SENSOR_RECORD);
    }

    protected void request_record_count()
    {
        final Intent intent = new Intent(context, DBSelectIntentService.class);
        intent.putExtra(DBSelectIntentService.EXTRA_RESULT_RECEIVER, dataCounterReceiver);
        intent.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, data_counter_instance());
        getActivity().startService(intent);
    }

    protected void request_new_data()
    {
        final Intent intent = new Intent(context, DBSelectIntentService.class);
        intent.putExtra(DBSelectIntentService.EXTRA_RESULT_RECEIVER, data_receiver());
        intent.putExtra(DBSelectIntentService.EXTRA_SENSOR_DATA_SELECT, data_instance());
        getActivity().startService(intent);
    }

    /**
     * Return an instance of a query that demands counting available records to be displayed
     * @return {@link DBQueryParcelableListenerInterface} Query
     */
    protected abstract DBQueryParcelableListenerInterface data_counter_instance();

    /**
     * Return a numeric value of the value that holds
     * {@link pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorCountDataAbstract#ATTRIBUTE_COUNT}
     * @return int
     */
    protected abstract int attribute_count();

    /**
     * Pass an instance of {@link ResultReceiver}. Remember to set a listener
     * to obtain a response data
     * @return {@link ResultReceiver} that is to be sent to a working thread. Listener
     * then handles the data
     */
    protected abstract ResultReceiver data_receiver();

    /**
     * Pass an instance of a query that requests for data. May include limits
     * on an output
     * @return {@link DBQueryParcelableListenerInterface} Query, possibly with limits
     */
    protected abstract DBQueryParcelableListenerInterface data_instance();

    /**
     * Handle data that is received from the database
     * @param data {@link DBSelectInterface} and its children depending on which
     *                                      instance is demanded.
     */
    protected abstract void apply_data(List<? extends DBSelectInterface> data);
}
