package pl.projektorion.krzysztof.blesensortag.adapters;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.R;

/**
 * Created by krzysztof on 31.10.16.
 */

public class BLeServiceScannerExpandableAdapter extends BaseExpandableListAdapter {

    private List<BluetoothGattService> services;
    private Context context;
    private LayoutInflater inflater;

    public BLeServiceScannerExpandableAdapter(Context context, List<BluetoothGattService> services) {
        super();
        this.context = context;
        this.services = services == null ? new ArrayList<BluetoothGattService>() : services;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return services.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return services.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        ServiceScannerGroupWidgetContainer container = new ServiceScannerGroupWidgetContainer();

        if( convertView == null )
        {
            view = inflater.inflate(R.layout.adapter_group_ble_service_scanner, null);
            container.serviceName = (TextView) view.findViewById(R.id.scan_service_name_state);
            container.serviceUuid = (TextView) view.findViewById(R.id.scan_service_uuid);
            view.setTag(container);
        }
        else
            container = (ServiceScannerGroupWidgetContainer) view.getTag();

        final String serviceName = "No name";
        final BluetoothGattService bLeService = services.get(groupPosition);
        final UUID serviceUUID = bLeService.getUuid();

        container.serviceName.setText(serviceName);
        container.serviceUuid.setText(serviceUUID.toString());
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        final BluetoothGattService service = services.get(groupPosition);
        return service.getCharacteristics().size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        final BluetoothGattService service = services.get(groupPosition);
        final List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        return characteristics.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ServiceScannerChildWidgetContainer container = new ServiceScannerChildWidgetContainer();

        if( convertView == null )
        {
            view = inflater.inflate(R.layout.adapter_child_ble_service_scanner, null);
            container.characteristicUuid = (TextView)
                    view.findViewById(R.id.scan_characteristic_uuid);
            view.setTag(container);
        }
        else
            container = (ServiceScannerChildWidgetContainer) view.getTag();

        final BluetoothGattCharacteristic currentCharacteristic =
                (BluetoothGattCharacteristic) getChild(groupPosition, childPosition);

        final String characteristicUuid = currentCharacteristic.getUuid().toString();

        container.characteristicUuid.setText(characteristicUuid);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void addGroupData(List<BluetoothGattService> services)
    {
        this.services = services;
    }


    private class ServiceScannerGroupWidgetContainer {
        public TextView serviceName;
        public TextView serviceUuid;
    }

    private class ServiceScannerChildWidgetContainer {
        public TextView characteristicUuid;
    }
}
