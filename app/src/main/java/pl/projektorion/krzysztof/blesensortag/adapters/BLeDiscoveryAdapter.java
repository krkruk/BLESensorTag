package pl.projektorion.krzysztof.blesensortag.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.models.BLeDeviceModel;

/**
 * Created by krzysztof on 26.10.16.
 */

public class BLeDiscoveryAdapter extends BaseAdapter {

    private Context context;
    private List<BLeDeviceModel> devices;
    private LayoutInflater inflater;

    private static class BLeDiscoveryWidgetHolder {
        public TextView deviceName;
        public TextView deviceRssi;
        public TextView deviceAddress;
        public TextView deviceData;

        public void setDeviceRssiValue(int deviceRssi)
        {
            String label = "RSSI: " + Integer.toString(deviceRssi);
            this.deviceRssi.setText(label);
        }
    }

    public BLeDiscoveryAdapter(Context context, List<BLeDeviceModel> devices) {
        super();
        this.context = context;
        this.devices = devices != null ? devices :
            new ArrayList<BLeDeviceModel>();

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( devices.size() <= 0 )
            return convertView;

        View view = convertView;
        BLeDiscoveryWidgetHolder holder = new BLeDiscoveryWidgetHolder();

        if( convertView == null )
        {
            view = inflater.inflate(R.layout.adapter_ble_device_discovery_layout, null);
            holder.deviceName = (TextView) view.findViewById(R.id.label_ble_device_name);
            holder.deviceRssi = (TextView) view.findViewById(R.id.label_ble_device_rssi);
            holder.deviceAddress = (TextView) view.findViewById(R.id.label_ble_device_address);
            holder.deviceData = (TextView) view.findViewById(R.id.label_ble_device_data);
            view.setTag(holder);
        }
        else
            holder = (BLeDiscoveryWidgetHolder) view.getTag();

        final BLeDeviceModel tempData = devices.get(position);
        BluetoothDevice btDevice = tempData.getDevice();
        holder.deviceName.setText(btDevice.getName());
        holder.setDeviceRssiValue(tempData.getRssi());
        holder.deviceAddress.setText(btDevice.getAddress());
//        String textData = new String(tempData.getBroadcastData(),
//                Charset.defaultCharset());
        String textData = tempData.getBroadcastData().toString();
        holder.deviceData.setText(textData);
        return view;
    }

    public void addDevice(BLeDeviceModel device)
    {
        this.devices.add(device);
    }

    public void addDevices(List<BLeDeviceModel> devices)
    {
        this.devices.addAll(devices);
    }

    public List<BLeDeviceModel> getDevices() { return devices; }

    public BluetoothDevice getBLeDevice(int position ) { return this.devices.get(position).getDevice(); }

    public boolean contains(BLeDeviceModel device) {
        return this.devices.contains(device);
    }

    public void clear()
    {
        devices.clear();
    }
}
