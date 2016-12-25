package pl.projektorion.krzysztof.blesensortag.fragments.database;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBSelectIntentService;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryWithLimitsListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidity;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidityCount;
import pl.projektorion.krzysztof.blesensortag.database.selects.Humidity.DBSelectHumidityCountData;
import pl.projektorion.krzysztof.blesensortag.utils.ServiceDataReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBPresentHumidityFragment extends Fragment
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
    private ServiceDataReceiver.ReceiverListener dataListener = new ServiceDataReceiver.ReceiverListener() {
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if( resultCode == DBSelectIntentService.EXTRA_RESULT_CODE )
            {
                List<? extends DBSelectInterface> data = resultData
                        .getParcelableArrayList(DBSelectIntentService.EXTRA_RESULT);
                if( data == null ) return;
                apply_data(data);
            }
        }
    };

    public DBPresentHumidityFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dbpresent_humidity, container, false);
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

    protected DBQueryParcelableListenerInterface data_counter_instance()
    {
        return new DBSelectHumidityCount(rootRecord);
    }

    protected int attribute_count()
    {
        return DBSelectHumidityCountData.ATTRIBUTE_COUNT;
    }

    protected ResultReceiver data_receiver()
    {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(dataListener);
        return dataReceiver;
    }

    protected DBQueryParcelableListenerInterface data_instance()
    {
        DBQueryWithLimitsListenerInterface query = new DBSelectHumidity(rootRecord, sensorRecord);
        query.setLimit(flingListener.getStartAt(), flingListener.getMaxElementsPerLoad());
        return query;
    }

    protected void apply_data(List<? extends DBSelectInterface> data)
    {
        for(DBSelectInterface echo : data)
            Log.i("Data", echo.toString());
    }
}
