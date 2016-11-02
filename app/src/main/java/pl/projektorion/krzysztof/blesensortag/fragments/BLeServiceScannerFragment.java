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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.adapters.BLeServiceScannerAdapter;
import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattClientService;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.SimpleKeysProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class BLeServiceScannerFragment extends Fragment {

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.extra.BLE_DEVICE";


    public final static String ACTION_BLE_SERVICE_CLICKED =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.action.SERVICE_CLICKED";

    public final static String EXTRA_BLE_SERVICE_UUID =
            "pl.projektorion.krzysztof.blesensortag.bleservicescannerfragment.extra.BLE_SERVICE_UUID";


    private BluetoothDevice bleDevice;
    private BLeGattClientService gattService;

    private View view;
    private Context appContext;
    private ListView serviceWidgetList;
    private BLeServiceScannerAdapter serviceWidgetAdapter;

    final private Handler handler = new Handler(Looper.getMainLooper());
    private LocalBroadcastManager broadcaster;

    private BroadcastReceiver serviceGattReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BLeGattClientService.ACTION_GATT_CONNECTED.equals(action))
            {
                display_status(R.string.status_connected);
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
                List<BluetoothGattService> services = gattService.getServices();
                List<String> servicesString = new ArrayList<>();
                for(BluetoothGattService service : services)
                    servicesString.add(service.getUuid().toString());
                serviceWidgetAdapter.expand(servicesString);
                serviceWidgetAdapter.notifyDataSetChanged();
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

    AdapterView.OnItemClickListener serviceListListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String strUuid = (String)serviceWidgetAdapter.getItem(position);

            Intent bleServiceClicked = new Intent(ACTION_BLE_SERVICE_CLICKED);
            bleServiceClicked.putExtra(EXTRA_BLE_SERVICE_UUID, strUuid);
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
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ble_service_scanner, container, false);
        init_widgets();
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
        Intent initGatt = new Intent(appContext, BLeGattClientService.class);
        initGatt.putExtra(BLeGattClientService.EXTRA_BLE_DEVICE, bleDevice);
        appContext.bindService(initGatt, gattServiceConnection, Context.BIND_AUTO_CREATE);
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

    private void init_widgets()
    {
        serviceWidgetList = (ListView) view.findViewById(R.id.listview_device_services);
        serviceWidgetList.setAdapter(serviceWidgetAdapter);
        serviceWidgetList.setOnItemClickListener(serviceListListener);
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
