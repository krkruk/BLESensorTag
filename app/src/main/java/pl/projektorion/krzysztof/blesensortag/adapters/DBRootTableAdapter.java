package pl.projektorion.krzysztof.blesensortag.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecord;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 18.12.16.
 */

public class DBRootTableAdapter extends CursorAdapter {
    List<DBSelectInterface> records;

    public DBRootTableAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.records = new ArrayList<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.adapter_root_table, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textId = (TextView) view.findViewById(R.id.root_id);
        TextView textDate = (TextView) view.findViewById(R.id.root_date);

        DBSelectInterface record = new DBSelectRootRecord(cursor);
        final long recordingStartedAt =
                1000L * (long) record.getData(DBSelectRootRecord.ATTRIBUTE_DATE_SECONDS);
        final long _id = (long) record.getData(DBSelectRootRecord.ATTRIBUTE_ID);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss",
                Locale.getDefault());
        String dateString = formatter.format(new Date(recordingStartedAt));

        textId.setText(String.format(Locale.getDefault(), "%d", _id));
        textDate.setText(dateString);

        records.add(record);
    }

    @Override
    public Object getItem(int position) {
        return records.size() > position ? records.get(position) : null;
    }
}
