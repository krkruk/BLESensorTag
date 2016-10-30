package pl.projektorion.krzysztof.blesensortag.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.BLeServiceScannerActivity;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.adapters.BLeDiscoveryAdapter;
import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeDiscoveryService;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.models.BLeDeviceModel;


public class BLeDiscoveryFragment extends Fragment {

    private static final int SCAN_TIMEOUT = 10000;
    private final int BLE_ENABLE_REQUEST = 54682;

    private BLeDiscoveryAdapter bleDiscoveryAdapter;
    private List<BluetoothDevice> bleDeviceList;
    private ListView deviceList;
    private View view;
    private Context appContext;
    private BLeDiscoveryService bLeDiscoveryService;
    private LocalBroadcastManager localBroadcastManager;
    private boolean isServiceBound = false;

    private MenuItem scanButton;

    //callbacks
    private BroadcastReceiver bleDiscoveryServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeDiscoveryService.ACTION_BLE_NOT_SUPPORTED.equals(action)){
                close_app();
            } else
            if(BLeDiscoveryService.ACTION_BLE_DEVICE_FOUND.equals(action)){
                final BluetoothDevice device = intent.getParcelableExtra(
                        BLeDiscoveryService.EXTRA_BLE_DEVICE);
                final int rssi = intent.getIntExtra(
                        BLeDiscoveryService.EXTRA_BLE_DEVICE_RSSI, 0);
                final byte[] data = intent.getByteArrayExtra(
                        BLeDiscoveryService.EXTRA_BLE_DEVICE_SCAN_DATA);

                if(!bleDeviceList.contains(device))
                    bleDeviceList.add(device);
                else return;

                final BLeDeviceModel model = new BLeDeviceModel(device, rssi, data);
                bleDiscoveryAdapter.addDevice(model);
                bleDiscoveryAdapter.notifyDataSetChanged();

            } else
            if(BLeDiscoveryService.ACTION_BLE_DISCOVERY_STARTED.equals(action)){
                set_mode_stop_scanning();
                Toast.makeText(context, R.string.toast_ble_scanning_started,
                        Toast.LENGTH_SHORT).show();
            } else
            if(BLeDiscoveryService.ACTION_BLE_DISCOVERY_STOPPED.equals(action)){
                Toast.makeText(context, R.string.toast_ble_scanning_stopped,
                        Toast.LENGTH_SHORT).show();
                set_mode_start_scanning();
            }
        }
    };

    private ServiceConnection localServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BLeDiscoveryService.BLeDiscoveryServiceBinder binder =
                    (BLeDiscoveryService.BLeDiscoveryServiceBinder) service;
            bLeDiscoveryService = binder.getService();
            isServiceBound = true;
            initBluetooth();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }

        private void initBluetooth()
        {
            final Intent initBLE = bLeDiscoveryService.initBluetoothLe();
            if( initBLE != null )
                startActivityForResult(initBLE, BLE_ENABLE_REQUEST);
        }
    };

    private AdapterView.OnItemClickListener onBLeDeviceItemClickListener =
            new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice currentDevice = bleDiscoveryAdapter.getBLeDevice(position);
            Intent startBLeServiceScanner = new Intent(appContext, BLeServiceScannerActivity.class);
            startBLeServiceScanner.putExtra(
                    BLeServiceScannerActivity.EXTRA_BLE_DEVICE, currentDevice);

            bLeDiscoveryService.stopBLeScan();
            set_mode_start_scanning();
            clear_local_data_containers();

            startActivity(startBLeServiceScanner);
        }
    };

    public BLeDiscoveryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_blediscovery, container, false);
        setHasOptionsMenu(true);
        init_widgets();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_ble_discovery_menu, menu);
        init_menu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_android_framework();
        init_data_containers();
        init_broadcast_receivers();
        init_bind_services();
    }

    @Override
    public void onDestroy() {
        kill_broadcast_receivers();
        kill_bind_services();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case BLE_ENABLE_REQUEST:
                on_ble_activity_started(resultCode, data);
                break;
            default: break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id)
        {
            case R.id.action_ble_scan:
                set_mode_stop_scanning();
                bLeDiscoveryService.startBLeScan(SCAN_TIMEOUT);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public static BLeDiscoveryFragment newInstance() {
        Bundle args = new Bundle();
        BLeDiscoveryFragment fragment = new BLeDiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void close_app()
    {
        getActivity().finish();
    }

    private void init_broadcast_receivers()
    {
        final IntentFilter localFilter = new IntentFilter();
        localFilter.addAction(BLeDiscoveryService.ACTION_BLE_NOT_SUPPORTED);
        localFilter.addAction(BLeDiscoveryService.ACTION_BLE_DEVICE_FOUND);
        localFilter.addAction(BLeDiscoveryService.ACTION_BLE_DISCOVERY_STARTED);
        localFilter.addAction(BLeDiscoveryService.ACTION_BLE_DISCOVERY_STOPPED);

        localBroadcastManager = LocalBroadcastManager.getInstance(appContext);
        localBroadcastManager.registerReceiver(bleDiscoveryServiceReceiver, localFilter);
    }

    private void init_bind_services()
    {
        final Intent startBLEDiscoveryService = new Intent(appContext,
                BLeDiscoveryService.class);
        appContext.bindService(startBLEDiscoveryService, localServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private void init_menu(Menu menu)
    {
        this.scanButton = menu.findItem(R.id.action_ble_scan);
        if(bLeDiscoveryService.isScanning())
            set_mode_stop_scanning();
    }

    private void init_android_framework()
    {
        setRetainInstance(true);

        final Activity activity = getActivity();
        try {
            this.appContext = activity.getApplicationContext();
        } catch (NullPointerException e) {
            Log.d(Constant.BLEDEV_ERR_TAG, Constant.CONTEXT_ERR);
            activity.finish();
        }
    }

    private void init_data_containers()
    {
        bleDeviceList = new ArrayList<>();
        bleDiscoveryAdapter = new BLeDiscoveryAdapter(appContext, null);
    }

    private void init_widgets()
    {
        deviceList = (ListView) view.findViewById(R.id.listview_ble_devices_found);
        deviceList.setAdapter(bleDiscoveryAdapter);
        deviceList.setOnItemClickListener(onBLeDeviceItemClickListener);
    }

    private void kill_broadcast_receivers()
    {
        localBroadcastManager.unregisterReceiver(bleDiscoveryServiceReceiver);
    }

    private void kill_bind_services()
    {
        appContext.unbindService(localServiceConnection);
    }

    @SuppressWarnings("unused")
    private void on_ble_activity_started(int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Activity.RESULT_OK:
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(appContext, R.string.toast_ble_must_be_enabled,
                        Toast.LENGTH_SHORT).show();
                close_app();
                break;
            default: break;
        }
    }

    private void set_mode_start_scanning()
    {
        scanButton.setTitle(R.string.action_ble_scan_start_label);
    }

    private void set_mode_stop_scanning()
    {
        if(!bLeDiscoveryService.isScanning())
            clear_local_data_containers();

        scanButton.setTitle(R.string.action_ble_scan_stop_label);
    }

    private void clear_local_data_containers()
    {
        bleDeviceList.clear();
        bleDiscoveryAdapter.clear();
        bleDiscoveryAdapter.notifyDataSetChanged();
    }
}
