package pl.projektorion.krzysztof.blesensortag.fragments.database;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;

import java.util.List;

import pl.projektorion.krzysztof.blesensortag.DBPresentSensorActivity;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountDataAbstract;
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

    private long availableRecords = 0;
    private long readMaxRecordsPerLoad = 20;
    private DBSelectOnChartFlingListener flingListener;


    private Runnable onFlingTask = new Runnable() {
        @Override
        public void run() {
            request_new_data();
        }
    };

    /**
     * {@link DBSelectInterface} root record that holds crucial data
     * for retrieving data from database
     */
    protected DBSelectInterface rootRecord;

    /**
     * {@link DBSelectInterface} sensor record that holds crucial data
     * for retrieving data from database
     */
    protected DBSelectInterface sensorRecord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        init_objects();
        acquire_data();
    }

    @Override
    public void onStart() {
        super.onStart();
        request_record_count();
    }

    /**
     * Receive information about available records that may be loaded.
     * If any additional behavior has to be implemented, call the super
     * beforehand.
     * @param resultCode {@link DBSelectIntentService#EXTRA_RESULT_CODE}
     * @param resultData {@link DBSelectIntentService#EXTRA_RESULT}. The bundle contains
     *                                                             a List<? extends DBSelectInterface>
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
        {
            List<? extends DBSelectInterface > data = resultData
                    .getParcelableArrayList(DBSelectIntentService.EXTRA_RESULT);
            if( data == null || data.size() <= 0) return;
            availableRecords = (long) data.get(0).getData(attribute_count());
            flingListener = new DBSelectOnChartFlingListener(
                    availableRecords, readMaxRecordsPerLoad);
            flingListener.setTask(onFlingTask);

            final Intent recordsParam = new Intent(DBPresentSensorActivity.ACTION_SENSOR_PARAMS);
            recordsParam.putExtra(DBPresentSensorActivity.EXTRA_NUMBER_OF_RECORDS, availableRecords);
            LocalBroadcastManager.getInstance(context).sendBroadcast(recordsParam);
        }
        request_new_data();
    }

    /**
     * Get numeric value of how many records are related with
     * the selected parameter of the param record.
     * @return
     */
    public long getAvailableRecords() {
        return availableRecords;
    }

    /**
     * Get an offset the query should limit the output
     * @return
     */
    public long getStartAt()
    {
        return flingListener.getStartAt();
    }

    /**
     * Get maximum number of records to be loaded per single
     * database connection
     * @return
     */
    public long getMaxRecordsPerLoad()
    {
        return flingListener == null
                ? readMaxRecordsPerLoad
                : flingListener.getMaxElementsPerLoad();
    }

    /**
     * Return a listener
     * @return {@link DBSelectOnChartFlingListener} listener
     */
    public DBSelectOnChartFlingListener getFlingListener() {
        return flingListener;
    }

    /**
     * Change a default value of maximum records to be loaded per single
     * database connection
     * @param readMaxRecordsPerLoad new value of records to be loaded per
     *                              single database connection
     */
    public void setReadMaxRecordsPerLoad(long readMaxRecordsPerLoad) {
        this.readMaxRecordsPerLoad = readMaxRecordsPerLoad;
        if( flingListener != null )
            flingListener.setMaxElementsPerLoad(readMaxRecordsPerLoad);
    }

    private void init_objects()
    {
        context = getActivity().getApplicationContext();
        dataCounterReceiver = new ServiceDataReceiver(new Handler());
        dataCounterReceiver.setListener(this);
    }

    private void acquire_data()
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
     * Get query that allows reading all values of the particular reading
     * @return {@link DBQueryParcelableListenerInterface} query to be sent
     * to the working thread
     */
    public abstract DBQueryParcelableListenerInterface getExportQuery();

    /**
     * Return an instance of a query that demands counting of all available records to be displayed
     * @return {@link DBQueryParcelableListenerInterface} Query
     */
    protected abstract DBQueryParcelableListenerInterface data_counter_instance();

    /**
     * Return a numeric value of the value that holds
     * {@link DBSelectSensorCountDataAbstract#ATTRIBUTE_COUNT}
     * @return int
     */
    protected abstract int attribute_count();

    /**
     * Pass an instance of {@link ResultReceiver}. Remember to set a listener
     * to obtain a response data
     * @return {@link ResultReceiver} that is to be sent to a working thread. ResultListener
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
