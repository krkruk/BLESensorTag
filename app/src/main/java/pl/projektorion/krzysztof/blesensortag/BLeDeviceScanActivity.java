package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeDiscoveryFragment;

public class BLeDeviceScanActivity extends Activity {

    private static final String BLE_DISCOVERY_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.BLE_DISCOVERY_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_device_scan);
        negotiate_fragment();
    }

    private void negotiate_fragment()
    {
        FragmentManager fm = getFragmentManager();
        Fragment bleDiscovery = fm.findFragmentByTag(BLE_DISCOVERY_FRAGMENT_TAG);
        if( bleDiscovery == null )
        {
            bleDiscovery = BLeDiscoveryFragment.newInstance(
                    BLeServiceScannerActivity.EXTRA_BLE_DEVICE,
                    BLeServiceScannerActivity.class);

            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(bleDiscovery, BLE_DISCOVERY_FRAGMENT_TAG);
            transaction.replace(R.id.containter_ble_discovery, bleDiscovery);
            transaction.commit();
        }
    }
}
