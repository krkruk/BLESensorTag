package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeConfigStethoscopeFragment;

public class BLePresentStethoscopeActivity extends Activity {

    public static final String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.BLE_DEVICE";

    private static final String STETHOSCOPE_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.FRAGMENT_TAG";

    private BluetoothDevice bleDevice;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_present_stethoscope);
        retrieve_data();
        negotiate_fragment();
    }

    private void retrieve_data()
    {
        final Intent intent = getIntent();
        bleDevice = intent.getParcelableExtra(EXTRA_BLE_DEVICE);
    }

    private void negotiate_fragment()
    {
        final FragmentManager fm = getFragmentManager();
        fragment = fm.findFragmentByTag(STETHOSCOPE_FRAGMENT_TAG);
        if( fragment == null )
        {
            fragment = BLeConfigStethoscopeFragment.newInstance(bleDevice);
            final FragmentTransaction ft = fm.beginTransaction();
            ft.add(fragment, STETHOSCOPE_FRAGMENT_TAG);
            ft.replace(R.id.present_stethoscope_frag_container, fragment);
            ft.commit();
        }
    }
}
