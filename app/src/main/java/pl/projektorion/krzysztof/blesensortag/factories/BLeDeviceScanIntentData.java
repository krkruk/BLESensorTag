package pl.projektorion.krzysztof.blesensortag.factories;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.BLePresentStethoscopeActivity;
import pl.projektorion.krzysztof.blesensortag.BLeServiceScannerActivity;
import pl.projektorion.krzysztof.blesensortag.utils.Pair;

/**
 * Created by krzysztof on 13.01.17.
 */

public class BLeDeviceScanIntentData {
    private List<Pair< String, Class<?> > > newActivityParams;

    public BLeDeviceScanIntentData() {
        this.newActivityParams = new ArrayList<>();
        init_data();
    }

    public int size() { return  newActivityParams.size(); }

    public Pair<String, Class<?> > get(int id)
    {
        return id < size() ? newActivityParams.get(id) : null;
    }

    private void init_data()
    {
        newActivityParams.add( new Pair<String, Class<?>>(          //Stethoscope
                BLePresentStethoscopeActivity.EXTRA_BLE_DEVICE, BLePresentStethoscopeActivity.class));
        newActivityParams.add( new Pair<String, Class<?>>(          //All sensors
                BLeServiceScannerActivity.EXTRA_BLE_DEVICE, BLeServiceScannerActivity.class) );
    }
}
