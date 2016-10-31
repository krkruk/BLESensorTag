package pl.projektorion.krzysztof.blesensortag.fragments;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.adapters.BLeServiceScannerAdapter;
import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeServiceScannerService;
import pl.projektorion.krzysztof.blesensortag.bluetooth.receivers.BLeServiceReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLeServiceScannerFragment extends Fragment {

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.extra.BLE_DEVICE";

    final private Handler handler = new Handler(Looper.getMainLooper());
    private BLeServiceReceiver serviceDataReceiver = new BLeServiceReceiver(handler);
    private View view;
    private Context appContext;
    private BluetoothDevice bleDevice;
    private LocalBroadcastManager broadcastManager;
    private TextView labelDeviceName;
    private TextView labelDeviceAddress;
    private ListView serviceWidgetList;
    private BLeServiceScannerAdapter serviceWidgetAdapter;


    private BLeServiceScannerService scannerService;
    private ServiceConnection scannerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            scannerService = ((BLeServiceScannerService.BLeServiceScannerBinder) service)
                    .getService();
            scannerService.connect();
            display_status(R.string.status_connecting);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private BroadcastReceiver statusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BLeServiceScannerService.ACTION_BLE_CONNECTED.equals(action))
            {
                display_status(R.string.status_connected);
                scannerService.discoverServices();
                display_status(R.string.status_ble_service_scanning);
            }
            else if(BLeServiceScannerService.ACTION_BLE_DISCONNECTED.equals(action))
            {
                display_status(R.string.status_disconnected);
            }
            else if(BLeServiceScannerService.ACTION_BLE_SERVICES_FOUND.equals(action))
            {
                display_status(R.string.status_ble_services_found);
                List<BluetoothGattService> services = scannerService.getServices();
                final List<String> uuids = new ArrayList<>();
                for(BluetoothGattService service : services)
                    uuids.add(service.getUuid().toString());
                serviceWidgetAdapter.expand(uuids);
                serviceWidgetAdapter.notifyDataSetChanged();
            }
        }
    };

    private BLeServiceReceiver.ReceiverListener serviceDataListener =
            new BLeServiceReceiver.ReceiverListener() {
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
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
        init_bound_services();
        init_broadcast_receivers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ble_service_scanner, container, false);
        init_widgets();
        init_label_values();
        return view;
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

    private void init_android_framework()
    {
        setRetainInstance(true);
        this.appContext = getActivity().getApplicationContext();
        serviceDataReceiver.setListener(serviceDataListener);
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
        serviceWidgetAdapter = new BLeServiceScannerAdapter(appContext, null);
    }

    private void init_bound_services()
    {
        final Intent scanService = new Intent(appContext, BLeServiceScannerService.class);
        scanService.putExtra(BLeServiceScannerService.EXTRA_RESULT_RECEIVER,
                serviceDataReceiver);
        scanService.putExtra(BLeServiceScannerService.EXTRA_BLE_DEVICE, bleDevice);
        appContext.bindService(scanService,
                scannerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void init_broadcast_receivers()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BLeServiceScannerService.ACTION_BLE_CONNECTED);
        filter.addAction(BLeServiceScannerService.ACTION_BLE_DISCONNECTED);
        filter.addAction(BLeServiceScannerService.ACTION_BLE_SERVICES_FOUND);
        broadcastManager = LocalBroadcastManager.getInstance(appContext);
        broadcastManager.registerReceiver(statusReceiver, filter);
    }

    private void init_widgets()
    {
        labelDeviceAddress = (TextView) view.findViewById(R.id.label_current_device_address);
        labelDeviceName = (TextView) view.findViewById(R.id.label_current_device_name);
        serviceWidgetList = (ListView) view.findViewById(R.id.listview_device_services);
        serviceWidgetList.setAdapter(serviceWidgetAdapter);
    }

    private void init_label_values()
    {
        labelDeviceName.setText(bleDevice.getName());
        labelDeviceAddress.setText(bleDevice.getAddress());

    }

    private void kill_bound_services()
    {
        appContext.unbindService(scannerServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        broadcastManager.unregisterReceiver(statusReceiver);
    }

    private void display_status(String status)
    {
        final ActionBar actionBar = getActivity().getActionBar();
        final int timeout = 2000;
        try {
            actionBar.setTitle(status);
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
