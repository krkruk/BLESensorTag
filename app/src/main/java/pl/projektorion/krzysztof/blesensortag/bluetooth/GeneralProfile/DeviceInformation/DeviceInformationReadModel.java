package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ProfileStringData;

import static pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile.DEVINFO_FIRMWARE_REV;
import static pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile.DEVINFO_HARDWARE_REV;
import static pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile.DEVINFO_MANUFACTURER_NAME;
import static pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile.DEVINFO_MODEL_NUMBER;
import static pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile.DEVINFO_SERIAL_NUMBER;
import static pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile.DEVINFO_SOFTWARE_REV;
import static pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation.DeviceInformationReadProfile.DEVINFO_SYSTEM_ID;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationReadModel extends Observable
        implements GenericGattReadModelInterface {
    private Set<UUID> attributeUuids;
    private ProfileStringData deviceInfoData;

    public DeviceInformationReadModel() {
        attributeUuids = new HashSet<>();
        init();
        deviceInfoData = new DeviceInformationData();
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        deviceInfoData.setValue(characteristic);
        setChanged();
        notifyObservers(deviceInfoData);
        clearChanged();
    }

    @Override
    public boolean hasCharacteristic(BluetoothGattCharacteristic characteristic) {
        return attributeUuids.contains(characteristic.getUuid());
    }

    @Override
    public Object getData() {
        return null;
    }

    private void init()
    {
        attributeUuids.add(DEVINFO_SYSTEM_ID);
        attributeUuids.add(DEVINFO_MODEL_NUMBER);
        attributeUuids.add(DEVINFO_SERIAL_NUMBER);
        attributeUuids.add(DEVINFO_FIRMWARE_REV);
        attributeUuids.add(DEVINFO_HARDWARE_REV);
        attributeUuids.add(DEVINFO_SOFTWARE_REV);
        attributeUuids.add(DEVINFO_MANUFACTURER_NAME);
    }
}