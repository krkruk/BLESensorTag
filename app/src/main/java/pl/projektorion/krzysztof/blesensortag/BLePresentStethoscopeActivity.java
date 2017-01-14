package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeConfigStethoscopeFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.CustomProfile.StethoscopeFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.CustomProfile.StethoscopePresentFragment;

public class BLePresentStethoscopeActivity extends Activity {

    public static final String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.BLE_DEVICE";

    private static final String STETHOSCOPE_PRESENT_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.FRAGMENT_PRESENT_TAG";

    private static final String STETHOSCOPE_CONFIG_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.FRAGMENT_CONFIG_TAG";

    private BluetoothDevice bleDevice;

    private Fragment presentationFragment;
    private Fragment configFragment;

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
        presentationFragment = fm.findFragmentByTag(STETHOSCOPE_PRESENT_FRAGMENT_TAG);
        configFragment = fm.findFragmentByTag(STETHOSCOPE_CONFIG_FRAGMENT_TAG);

        if( presentationFragment == null )
        {
            presentationFragment = new StethoscopeFragment();
            final FragmentTransaction ft = fm.beginTransaction();
            ft.add(presentationFragment, STETHOSCOPE_PRESENT_FRAGMENT_TAG);
            ft.replace(R.id.present_stethoscope_frag_container, presentationFragment);
            ft.commit();
        }
        if( configFragment == null )
        {
            configFragment = BLeConfigStethoscopeFragment.newInstance(bleDevice);
            final FragmentTransaction ft = fm.beginTransaction();
            ft.add(configFragment, STETHOSCOPE_CONFIG_FRAGMENT_TAG);
            ft.replace(R.id.config_stethoscope_frag_container, configFragment);
            ft.commit();
        }
    }
}
