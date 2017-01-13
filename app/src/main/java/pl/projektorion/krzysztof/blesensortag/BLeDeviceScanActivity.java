package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import pl.projektorion.krzysztof.blesensortag.factories.BLeDeviceScanIntentData;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeDiscoveryFragment;
import pl.projektorion.krzysztof.blesensortag.utils.Pair;

public class BLeDeviceScanActivity extends Activity
    implements DialogInterface.OnClickListener {

    private static final String BLE_DISCOVERY_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.BLE_DISCOVERY_FRAGMENT";

    private static final String EXTRA_MODE_ID =
            "pl.projektorion.krzysztof.blesensortag.extra.MODE_ID";

    private AlertDialog modeSelectionDialog;
    private String[] deviceModeLabels;
    private BLeDeviceScanIntentData bLeDeviceScanIntentData;
    private int modeId = -1;
    private Pair<String, Class<?> > newActivityParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_device_scan);
        init_objects();
        load_saved_instance(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( modeId == -1 ) modeSelectionDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_MODE_ID, modeId);
    }

    @Override
    protected void onDestroy() {
        if( modeSelectionDialog != null ) modeSelectionDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if( deviceModeLabels == null )
            return;
        Log.i("Selected", deviceModeLabels[which]);
        modeId = which;

        if( can_negotiate_fragment() ) negotiate_fragment();
        else {
            Toast.makeText(this, R.string.toast_invalid_ble_scan_data, Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    private void init_objects()
    {
        deviceModeLabels = getResources().getStringArray(R.array.dialog_ble_detect_mode);
        modeSelectionDialog = create_dialog();
        modeSelectionDialog.setCanceledOnTouchOutside(false);
        bLeDeviceScanIntentData = new BLeDeviceScanIntentData();
    }

    private void load_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null )
            return;

        modeId = savedInstanceState.getInt(EXTRA_MODE_ID);
    }

    private boolean can_negotiate_fragment()
    {
        newActivityParam = bLeDeviceScanIntentData.get(modeId);
        return newActivityParam != null && modeId < bLeDeviceScanIntentData.size();
    }

    private void negotiate_fragment()
    {
        FragmentManager fm = getFragmentManager();
        Fragment bleDiscovery = fm.findFragmentByTag(BLE_DISCOVERY_FRAGMENT_TAG);
        if( bleDiscovery == null )
        {
            bleDiscovery = BLeDiscoveryFragment.newInstance(
                    newActivityParam.first(),
                    newActivityParam.second());

            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(bleDiscovery, BLE_DISCOVERY_FRAGMENT_TAG);
            transaction.replace(R.id.containter_ble_discovery, bleDiscovery);
            transaction.commit();
        }
    }

    private AlertDialog create_dialog()
    {
        return new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_ble_detect_title)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        BLeDeviceScanActivity.this.finish();
                    }
                })
                .setItems(R.array.dialog_ble_detect_mode, this)
                .create();
    }
}
