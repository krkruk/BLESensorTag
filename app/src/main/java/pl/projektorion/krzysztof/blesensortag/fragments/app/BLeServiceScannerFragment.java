package pl.projektorion.krzysztof.blesensortag.fragments.app;


import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.adapters.BLeServiceScannerAdapterGroupDataContainer;
import pl.projektorion.krzysztof.blesensortag.adapters.BLeServiceScannerExpandableAdapter;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattClientService;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattProfiles;
import pl.projektorion.krzysztof.blesensortag.factories.BLeDataConfigFragmentFactory;
import pl.projektorion.krzysztof.blesensortag.factories.BLeDataProfileFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLeServiceScannerFragment extends Fragment {

    /**
     * {@link BLeServiceScannerFragment} expects to obtain an extra {@link BluetoothDevice}
     * being passed via {@link BLeServiceScannerFragment#newInstance(BluetoothDevice)}
     */
    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.extra.BLE_DEVICE";


    /**
     * Emitted only when the user clicks the element. The effect should be handled externally.
     */
    public final static String ACTION_BLE_SERVICE_CLICKED =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.action.SERVICE_CLICKED";

    /**
     * Associated with {@link BLeServiceScannerFragment#ACTION_BLE_SERVICE_CLICKED}.
     * Each intent contains String value of clicked UUID. The action can be therefore
     * handled externally.
     */
    public final static String EXTRA_BLE_SERVICE_UUID =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.extra.BLE_SERVICE_UUID";


    private BluetoothDevice bleDevice;
    private BLeGattClientService gattService;

    private View view;
    private Context appContext;
    private MenuItem recordAction;
    private ExpandableListView serviceWidgetExpandableList;
    private BLeServiceScannerExpandableAdapter serviceWidgetExpandableAdapter;

    final private Handler handler = new Handler(Looper.getMainLooper());
    private LocalBroadcastManager broadcaster;

    private BLeDataProfileFactory profileFactory;
    private BLeAvailableGattProfiles gattProfiles;
    private BLeDataConfigFragmentFactory configFragFactory;

    private boolean isServiceDiscovered = false;
    private BroadcastReceiver serviceGattReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeGattClientService.ACTION_GATT_CONNECTED.equals(action))
            {
                display_status(R.string.status_connected);
                populate_profile_factory();

                gattService.discoverServices();
            }
            else if(BLeGattClientService.ACTION_GATT_CONNECTING.equals(action))
            {
                display_status(R.string.status_connecting);
            }
            else if(BLeGattClientService.ACTION_GATT_DISCONNECTED.equals(action))
            {
                display_status(R.string.status_disconnected);
            }
            else if(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
            {
                serviceWidgetExpandableAdapter.clear();
                final List<BluetoothGattService> services = gattService.getServices();
                create_profile_factories(services);
                populate_config_fragment_factory();
//                enable_all_notifications();
//                enable_all_measurements();
                populate_adapter();
                isServiceDiscovered = true;
                if( recordAction != null ) recordAction.setEnabled(true);
            }
        }
    };

    private ServiceConnection gattServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gattService = ((BLeGattClientService.BLeGattClientBinder) service)
                    .getService();
            gattService.connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private ExpandableListView.OnGroupExpandListener expandListener =
            new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;
        @Override
        public void onGroupExpand(int groupPosition) {
            BLeServiceScannerAdapterGroupDataContainer data =
                    (BLeServiceScannerAdapterGroupDataContainer)
                            serviceWidgetExpandableAdapter.getGroup(groupPosition);
            collapse_previous_view(groupPosition);

            final UUID uuid = data.getServiceUuid();
            notify_list_expanded(uuid);
            gattProfiles.demandReadProfileValues(uuid);
        }

        private void collapse_previous_view(int groupPosition)
        {
            if( (previousGroup != -1) && (groupPosition != previousGroup) )
                serviceWidgetExpandableList.collapseGroup(previousGroup);
            previousGroup = groupPosition;
        }

        private void notify_list_expanded(UUID uuid)
        {
            Intent bleServiceClicked = new Intent(ACTION_BLE_SERVICE_CLICKED);
            bleServiceClicked.putExtra(EXTRA_BLE_SERVICE_UUID,
                    uuid.toString());
            broadcaster.sendBroadcast(bleServiceClicked);
        }
    };


    public BLeServiceScannerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init_android_framework();
        retrieve_incoming_data();
        assert_ble_device_exists();
        init_adapters();
        init_objects();
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
         * This is an ugly solution but it works...
         */
        if( serviceWidgetExpandableAdapter != null )
            serviceWidgetExpandableAdapter.onActivityRecreated(getFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ble_service_scanner, container, false);
        init_widgets();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_ble_service_scanner, menu);
        init_menu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        kill_bound_services();
        kill_broadcast_receivers();
        super.onDestroy();
    }

    public static BLeServiceScannerFragment newInstance(BluetoothDevice device) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_BLE_DEVICE, device);
        BLeServiceScannerFragment fragment = new BLeServiceScannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void populate_profile_factory()
    {
        profileFactory.initProfiles(gattService);
    }

    private void create_profile_factories(List<BluetoothGattService> services)
    {
        for(BluetoothGattService service : services)
        {
            final UUID serviceUuid = service.getUuid();
            final GenericGattProfileInterface profile = profileFactory
                    .createProfile(serviceUuid);

            gattProfiles.put(serviceUuid, profile);
        }
    }

    private void populate_config_fragment_factory()
    {
        configFragFactory.initConfigFragmentFactory(gattProfiles);
    }

    private void enable_all_notifications()
    {
        gattProfiles.enableAllNotifications();
    }

    private void enable_all_measurements()
    {
        gattProfiles.enableAllMeasurements();
    }

    private void populate_adapter()
    {
        Map<BLeServiceScannerAdapterGroupDataContainer, Fragment> dataEntries
                = new LinkedHashMap<>();

        for(UUID uuid : gattProfiles.keySet())
        {
            final GenericGattProfileInterface profile = gattProfiles.get(uuid);
            final String name = profile.getName();
            if( !name.equals("") ) {
                final BLeServiceScannerAdapterGroupDataContainer data =
                        new BLeServiceScannerAdapterGroupDataContainer(name, uuid);
                dataEntries.put(data, configFragFactory.create(uuid));
            }
        }
        serviceWidgetExpandableAdapter.extend(dataEntries);
        serviceWidgetExpandableAdapter.notifyDataSetChanged();
    }



    private void init_android_framework()
    {
        setRetainInstance(true);
        this.appContext = getActivity().getApplicationContext();
    }

    private void retrieve_incoming_data()
    {
        Bundle bundle = getArguments();
        bleDevice = bundle.getParcelable(EXTRA_BLE_DEVICE);
    }

    private void assert_ble_device_exists()
    {
        if( bleDevice == null ) {
            final Activity activity = getActivity();
            activity.finish();
            Toast.makeText(appContext,
                    R.string.toast_ble_service_scan_device_not_passed,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void init_adapters()
    {
        serviceWidgetExpandableAdapter = new BLeServiceScannerExpandableAdapter(appContext,
                null, getFragmentManager());
    }

    private void init_objects()
    {
        profileFactory = new BLeDataProfileFactory();
        gattProfiles = new BLeAvailableGattProfiles();
        configFragFactory = new BLeDataConfigFragmentFactory();
    }

    private void init_broadcast_receivers()
    {
        IntentFilter serviceFilter = new IntentFilter();
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_SERVICES_DISCOVERED);
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_CONNECTED);
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_CONNECTING);
        serviceFilter.addAction(BLeGattClientService.ACTION_GATT_DISCONNECTED);

        broadcaster = LocalBroadcastManager.getInstance(appContext);
        broadcaster.registerReceiver(serviceGattReceiver, serviceFilter);
    }

    private void init_bound_services()
    {
        Intent initGatt = new Intent(appContext, BLeGattClientService.class);
        initGatt.putExtra(BLeGattClientService.EXTRA_BLE_DEVICE, bleDevice);
        appContext.bindService(initGatt, gattServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void init_widgets()
    {
        serviceWidgetExpandableList = (ExpandableListView) view.findViewById(R.id.listview_device_services);
        serviceWidgetExpandableList.setAdapter(serviceWidgetExpandableAdapter);
        serviceWidgetExpandableList.setOnGroupExpandListener(expandListener);
    }

    private void init_menu(Menu menu)
    {
        recordAction = menu.findItem(R.id.action_record);
        recordAction.setEnabled(isServiceDiscovered);
    }
    private void kill_bound_services()
    {
        appContext.unbindService(gattServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(serviceGattReceiver);
    }

    private void display_status(int resId)
    {
        final ActionBar actionBar = getActivity().getActionBar();
        final int timeout = 2000;
        try {
            actionBar.setTitle(resId);
        } catch (NullPointerException e)
        {
            return;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                actionBar.setTitle(R.string.app_name);
            }
        }, timeout);
    }
}
