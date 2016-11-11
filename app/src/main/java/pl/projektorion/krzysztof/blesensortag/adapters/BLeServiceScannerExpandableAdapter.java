package pl.projektorion.krzysztof.blesensortag.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.fragments.FragmentFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.config.NullConfigFragment;

/**
 * Created by krzysztof on 31.10.16.
 */

public class BLeServiceScannerExpandableAdapter extends BaseExpandableListAdapter {

    private Map<BLeServiceScannerAdapterGroupDataContainer, FragmentFactory> services;
    private List<BLeServiceScannerAdapterGroupDataContainer> groupData;
    private Context context;
    private LayoutInflater inflater;
    private FragmentManager fm;

    public BLeServiceScannerExpandableAdapter(
            Context context,
            Map<BLeServiceScannerAdapterGroupDataContainer, FragmentFactory> services,
            FragmentManager fm) {
        super();
        this.context = context;
        this.services = services == null ?
                new LinkedHashMap<BLeServiceScannerAdapterGroupDataContainer, FragmentFactory>()
                : services;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fm = fm;
        update_group_data();
    }

    @Override
    public int getGroupCount() {
        return services.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
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
            container.serviceName = (TextView) view.findViewById(R.id.scan_service_name);
            container.serviceUuid = (TextView) view.findViewById(R.id.scan_service_uuid);
            view.setTag(container);
        }
        else
            container = (ServiceScannerGroupWidgetContainer) view.getTag();

        BLeServiceScannerAdapterGroupDataContainer data =
                groupData.get(groupPosition);
        String serviceName = "None";
        String serviceUuid = "Address";
        if( data != null ){
            serviceName = data.getServiceName();
            serviceUuid = data.getServiceUuid().toString();
        }

        container.serviceName.setText(serviceName);
        container.serviceUuid.setText(serviceUuid);
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return services.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        final BLeServiceScannerAdapterGroupDataContainer group =
                groupData.get(groupPosition);
        return services.get(group);
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
            container.configurationFragment = (FrameLayout)
                    view.findViewById(R.id.child_fragment_container);
            view.setTag(container);
        }
        else
            container = (ServiceScannerChildWidgetContainer) view.getTag();

        final FragmentFactory factory = (FragmentFactory) getChild(groupPosition, childPosition);
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag = new NullConfigFragment();
        if( factory != null ) frag = factory.create();
        if( frag == null ) frag = new NullConfigFragment();
        ft.replace(R.id.child_fragment_container, frag);
        ft.commit();

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void extend(Map<BLeServiceScannerAdapterGroupDataContainer, FragmentFactory> services)
    {
        this.services.putAll(services);
        update_group_data();
    }

    public void clear()
    {
        services.clear();
        groupData.clear();
    }
    private void update_group_data()
    {
        this.groupData = new ArrayList<>(this.services.keySet());
    }

    private class ServiceScannerGroupWidgetContainer {
        public TextView serviceName;
        public TextView serviceUuid;
    }

    private class ServiceScannerChildWidgetContainer {
        public FrameLayout configurationFragment;
    }
}
