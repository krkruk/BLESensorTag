package pl.projektorion.krzysztof.blesensortag.fragments.presentation.CustomProfile;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile.StethoscopeModel;
import pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile.StethoscopeProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattModelService;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.fragments.AbstractObservableFragmentFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class StethoscopeFragment extends Fragment {

    private static final String PRESENT_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.CustomProfile.tag.PRESENT_FRAGMENT";


    private Context context;
    private View view;
    private Fragment presentFragment;

    private LocalBroadcastManager broadcaster;
    private BLeGattModelService bleModelService;
    private StethoscopeModel stethoscopeModel;

    private BroadcastReceiver bleModelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( bleModelService == null ) {
                Log.d("StethoModel", "Could not connect to stethoscopeModel service");
                return;
            }

            stethoscopeModel = new StethoscopeModel();
            bleModelService.setModel(stethoscopeModel.getDataUuid(), stethoscopeModel);
            negotiate_fragment(stethoscopeModel);
        }
    };

    private ServiceConnection bleServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleModelService = ((BLeGattModelService.BLeGattModelBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public StethoscopeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_android_framework();
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stethoscope, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        kill_bound_services();
        kill_broadcast_receivers();
        super.onDestroy();
    }

    public BLeAvailableGattModels getModels()
    {
        return bleModelService != null
                ? bleModelService.getModels()
                : null;
    }

    private void init_android_framework()
    {
        setRetainInstance(true);
        context = getActivity().getApplicationContext();
    }

    private void init_broadcast_receivers()
    {
        IntentFilter serviceModelFilter = new IntentFilter();
        serviceModelFilter.addAction(BLeGattModelService.ACTION_GATT_MODELS_CREATED);

        broadcaster = LocalBroadcastManager.getInstance(context);
        broadcaster.registerReceiver(bleModelReceiver, serviceModelFilter);
    }

    private void init_bound_services()
    {
        final Intent bleIntent = new Intent(context, BLeGattModelService.class);
        context.bindService(bleIntent, bleServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void kill_bound_services()
    {
        context.unbindService(bleServiceConnection);
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(bleModelReceiver);
    }

    private void negotiate_fragment(Observable observable)
    {
        FragmentManager fm = getFragmentManager();
        presentFragment = fm.findFragmentByTag(PRESENT_FRAGMENT_TAG);

        if( presentFragment == null )
        {
            final AbstractObservableFragmentFactory factory =
                    new StethoscopePresentObservableFragment(observable);

            presentFragment = factory.create();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(presentFragment, PRESENT_FRAGMENT_TAG);
            ft.replace(R.id.present_plot_stethoscope_container, presentFragment);
            ft.commit();
        }
    }
}
