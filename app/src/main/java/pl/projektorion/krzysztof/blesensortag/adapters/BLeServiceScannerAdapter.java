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

    List<BLeServiceScannerAdapterGroupDataContainer> namesOfServices;
    LayoutInflater inflater;

    public BLeServiceScannerAdapter(Context context,
                                    List<BLeServiceScannerAdapterGroupDataContainer> namesOfServices) {
        super();
        this.namesOfServices = namesOfServices == null ?
                new ArrayList<BLeServiceScannerAdapterGroupDataContainer>() : namesOfServices;
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
            container.serviceName = (TextView) view.findViewById(R.id.scan_service_name_state);
            container.serviceUuid = (TextView) view.findViewById(R.id.scan_service_uuid_state);
            view.setTag(container);
        }
        else
            container = (ServiceScannerWidgetsContainer) view.getTag();

        final BLeServiceScannerAdapterGroupDataContainer label = namesOfServices.get(position);

        container.serviceName.setText(label.getServiceName());
        container.serviceUuid.setText(label.getServiceUuid().toString());

        return view;
    }

    public void add(BLeServiceScannerAdapterGroupDataContainer data)
    {
        namesOfServices.add(data);
    }

    public void expand(List<BLeServiceScannerAdapterGroupDataContainer> data)
    {
        namesOfServices.addAll(data);
    }

    private class ServiceScannerWidgetsContainer {
        TextView serviceName;
        TextView serviceUuid;
    }
}
