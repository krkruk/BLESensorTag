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
public class DBPresentHumidityFragment extends DBPresentSensorFragmentAbstract {

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
    }

    public static DBPresentHumidityFragment newInstance(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD, rootRecord);
        args.putParcelable(EXTRA_SENSOR_RECORD, sensorRecord);
        DBPresentHumidityFragment fragment = new DBPresentHumidityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dbpresent_humidity, container, false);
    }

    @Override
    protected DBQueryParcelableListenerInterface data_counter_instance()
    {
        return new DBSelectHumidityCount(rootRecord);
    }

    @Override
    protected int attribute_count()
    {
        return DBSelectHumidityCountData.ATTRIBUTE_COUNT;
    }

    @Override
    protected ResultReceiver data_receiver()
    {
        ServiceDataReceiver dataReceiver = new ServiceDataReceiver(new Handler());
        dataReceiver.setListener(dataListener);
        return dataReceiver;
    }

    @Override
    protected DBQueryParcelableListenerInterface data_instance()
    {
        DBQueryWithLimitsListenerInterface query = new DBSelectHumidity(rootRecord, sensorRecord);
        query.setLimit(super.getStartAt(), super.getMaxRecordsPerLoad());
        return query;
    }

    @Override
    protected void apply_data(List<? extends DBSelectInterface> data)
    {
        for(DBSelectInterface echo : data)
            Log.i("Data", echo.toString());
    }
}
