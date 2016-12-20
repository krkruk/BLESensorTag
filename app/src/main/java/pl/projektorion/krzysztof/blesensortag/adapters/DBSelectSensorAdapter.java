package pl.projektorion.krzysztof.blesensortag.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryListenerInterface;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectSensorAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<DBQueryListenerInterface> listeners;

    public DBSelectSensorAdapter(Context context) {
        super();
        this.listeners = new ArrayList<>();
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listeners.size();
    }

    @Override
    public Object getItem(int position) {
        return listeners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView dbSelectLabel;
        if( convertView == null )
        {
            view = inflater.inflate(R.layout.adapter_db_select_sensor, null);
            dbSelectLabel = (TextView)view.findViewById(R.id.db_select_sensor_label);
            view.setTag(dbSelectLabel);
        }
        else
            dbSelectLabel = (TextView) view.getTag();

        final String label = listeners.get(position).getLabel();
        dbSelectLabel.setText(label);

        return view;
    }

    public void setData(List<DBQueryListenerInterface> listeners)
    {
        notifyDataSetInvalidated();
        this.listeners.addAll(listeners);
        notifyDataSetChanged();
    }

    public void clear()
    {
        notifyDataSetInvalidated();
        listeners.clear();
        notifyDataSetChanged();
    }
}
