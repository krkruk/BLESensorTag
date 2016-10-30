package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;

import pl.projektorion.krzysztof.blesensortag.fragments.BLeServiceScannerFragment;

public class BLeServiceScannerActivity extends Activity {

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescanneractivity.extra.BLE_DEVICE";

    private BluetoothDevice bleDevice;
    private BLeServiceScannerFragment serviceScannerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_service_scanner);

        retrieve_intent_data();
        negotiate_service_scanner_fragment();
    }

    private void retrieve_intent_data()
    {
        Intent sentIntent = getIntent();
        this.bleDevice = sentIntent.getParcelableExtra(EXTRA_BLE_DEVICE);
    }

    private void negotiate_service_scanner_fragment()
    {
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        serviceScannerFragment = BLeServiceScannerFragment.newInstance(bleDevice);
        fragTransaction.replace(R.id.ble_service_scanner_fragment_container,
                serviceScannerFragment);
        fragTransaction.commit();
    }
}
