package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.widget.TextView;

import pl.projektorion.krzysztof.blesensortag.fragments.BLeServiceScannerFragment;

public class BLeServiceScannerActivity extends Activity {

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescanneractivity.extra.BLE_DEVICE";

    private final static String TAG_SERVICE_SCANNER_TAG =
            "pl.projektorion.krzysztof.blesensortag.fragments.BLeServiceScannerFragment.tag.SERVICE.SCANNER";

    private BluetoothDevice bleDevice;
    private Fragment serviceScannerFragment;

    private TextView labelDeviceName;
    private TextView labelDeviceUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_service_scanner);

        retrieve_intent_data();
        load_saved_instance(savedInstanceState);
        negotiate_service_scanner_fragment();
        init_widgets();
        init_widgets_data();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_BLE_DEVICE, bleDevice);
    }

    private void retrieve_intent_data()
    {
        Intent sentIntent = getIntent();
        this.bleDevice = sentIntent.getParcelableExtra(EXTRA_BLE_DEVICE);
    }

    private void negotiate_service_scanner_fragment()
    {
        FragmentManager fm = getFragmentManager();
        serviceScannerFragment = fm.findFragmentByTag(TAG_SERVICE_SCANNER_TAG);
        if( serviceScannerFragment == null ) {
            FragmentTransaction ft = fm.beginTransaction();
            serviceScannerFragment = BLeServiceScannerFragment.newInstance(bleDevice);
            ft.add(serviceScannerFragment, TAG_SERVICE_SCANNER_TAG);
            ft.replace(R.id.ble_service_scanner_fragment_container,
                    serviceScannerFragment);
            ft.commit();
        }
    }

    private void load_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null )
            return;

        bleDevice = savedInstanceState.getParcelable(EXTRA_BLE_DEVICE);
    }

    private void init_widgets()
    {
        labelDeviceName = (TextView) findViewById(R.id.label_current_device_name);
        labelDeviceUuid = (TextView) findViewById(R.id.label_current_device_address);
    }

    private void init_widgets_data()
    {
        if( bleDevice == null )
            return;

        labelDeviceName.setText(bleDevice.getName());
        labelDeviceUuid.setText(bleDevice.getAddress());
    }
}
