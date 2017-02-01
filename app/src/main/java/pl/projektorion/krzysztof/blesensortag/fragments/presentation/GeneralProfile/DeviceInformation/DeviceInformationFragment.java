package pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation;


import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.ProfileStringData;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceInformationFragment extends Fragment
    implements Observer {

    private static final String EXTRA_SYSTEM_ID =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.extra.SYSTEM_ID";

    private static final String EXTRA_MODEL_NUMBER =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.extra.MODEL_NUMBER";

    private static final String EXTRA_SERIAL_NUMBER =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.extra.SERIAL_NUMBER";

    private static final String EXTRA_FIRMWARE_REV =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.extra.FIRMWARE_REV";

    private static final String EXTRA_HARDWARE_REV =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.extra.HARDWARE_REV";

    private static final String EXTRA_SOFTWARE_REV =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.extra.SOFTWARE_REV";

    private static final String EXTRA_MANUFACTURER =
            "pl.projektorion.krzysztof.blesensortag.fragments.presentation.GeneralProfile.DeviceInformation.extra.MANUFACTURER";

    private View view;
    private TextView labelSystemId;
    private TextView labelModelNumber;
    private TextView labelSerialNumber;
    private TextView labelFirmwareRev;
    private TextView labelHardwareRev;
    private TextView labelSoftwareRev;
    private TextView labelManufacturer;

    private Observable observable;
    private Handler handler;

    public DeviceInformationFragment() {
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileStringData data = (ProfileStringData) arg;
        if( handler == null ) return;
        final String systemId = data.getValue(DeviceInformationData.ATTRIBUTE_DEVINFO_SYSTEM_ID);
        final String modelNumber = data.getValue(DeviceInformationData.ATTRIBUTE_DEVINFO_MODEL_NUMBER);
        final String serialNumber = data.getValue(DeviceInformationData.ATTRIBUTE_DEVINFO_SERIAL_NUMBER);
        final String firmwareRev = data.getValue(DeviceInformationData.ATTRIBUTE_DEVINFO_FIRMWARE_REV);
        final String hardwareRev = data.getValue(DeviceInformationData.ATTRIBUTE_DEVINFO_HARDWARE_REV);
        final String softwareRev = data.getValue(DeviceInformationData.ATTRIBUTE_DEVINFO_SOFTWARE_REV);
        final String manufacturer = data.getValue(DeviceInformationData.ATTRIBUTE_DEVINFO_MANUFACTURER_NAME);

        handler.post(new Runnable() {
            @Override
            public void run() {
                labelSystemId.setText(systemId);
                labelModelNumber.setText(modelNumber);
                labelSerialNumber.setText(serialNumber);
                labelFirmwareRev.setText(firmwareRev);
                labelHardwareRev.setText(hardwareRev);
                labelSoftwareRev.setText(softwareRev);
                labelManufacturer.setText(manufacturer);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_information, container, false);
        init_widgets();
        load_saved_instance(savedInstanceState);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_SYSTEM_ID, labelSystemId.getText().toString());
        outState.putString(EXTRA_MODEL_NUMBER, labelModelNumber.getText().toString());
        outState.putString(EXTRA_SERIAL_NUMBER, labelSerialNumber.getText().toString());
        outState.putString(EXTRA_FIRMWARE_REV, labelFirmwareRev.getText().toString());
        outState.putString(EXTRA_HARDWARE_REV, labelHardwareRev.getText().toString());
        outState.putString(EXTRA_SOFTWARE_REV, labelSoftwareRev.getText().toString());
        outState.putString(EXTRA_MANUFACTURER, labelManufacturer.getText().toString());
    }

    @Override
    public void onDestroy() {
        if( observable != null )
            observable.deleteObserver(this);
        super.onDestroy();
    }

    private void init_widgets()
    {
        labelSystemId = (TextView) view.findViewById(R.id.system_id_name);
        labelModelNumber = (TextView) view.findViewById(R.id.model_number_name);
        labelSerialNumber = (TextView) view.findViewById(R.id.serial_number_name);
        labelFirmwareRev = (TextView) view.findViewById(R.id.firmware_rev_name);
        labelHardwareRev = (TextView) view.findViewById(R.id.harware_rev_name);
        labelSoftwareRev = (TextView) view.findViewById(R.id.software_rev_name);
        labelManufacturer = (TextView) view.findViewById(R.id.manufacturer_name);
    }

    private void load_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null ) return;
        labelSystemId.setText(savedInstanceState.getString(EXTRA_SYSTEM_ID));
        labelModelNumber.setText(savedInstanceState.getString(EXTRA_MODEL_NUMBER));
        labelSerialNumber.setText(savedInstanceState.getString(EXTRA_SERIAL_NUMBER));
        labelFirmwareRev.setText(savedInstanceState.getString(EXTRA_FIRMWARE_REV));
        labelHardwareRev.setText(savedInstanceState.getString(EXTRA_HARDWARE_REV));
        labelSoftwareRev.setText(savedInstanceState.getString(EXTRA_SOFTWARE_REV));
        labelManufacturer.setText(savedInstanceState.getString(EXTRA_MANUFACTURER));
    }
}
