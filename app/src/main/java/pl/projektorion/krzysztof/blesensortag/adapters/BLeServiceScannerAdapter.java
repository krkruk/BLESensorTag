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

/**
 * Created by krzysztof on 31.10.16.
 */

public class BLeServiceScannerAdapter extends BaseAdapter {

    List<String> namesOfServices;
    LayoutInflater inflater;

    public BLeServiceScannerAdapter(Context context, List<String> namesOfServices) {
        super();
        this.namesOfServices = namesOfServices == null ? new ArrayList<String>() : namesOfServices;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return namesOfServices.size();
    }

    @Override
    public Object getItem(int position) {
        return namesOfServices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ServiceScannerWidgetsContainer container = new ServiceScannerWidgetsContainer();

        if( convertView == null )
        {
            view = inflater.inflate(R.layout.adapter_ble_service_scanner, null);
            container.serviceName = (TextView) view.findViewById(R.id.scan_service_name_list);
            view.setTag(container);
        }
        else
            container = (ServiceScannerWidgetsContainer) view.getTag();

        final String label = namesOfServices.get(position);

        container.serviceName.setText(label);

        return view;
    }

    public void add(String data)
    {
        namesOfServices.add(data);
    }

    public void expand(List<String> data)
    {
        namesOfServices.addAll(data);
    }

    private class ServiceScannerWidgetsContainer {
        TextView serviceName;
    }
}
