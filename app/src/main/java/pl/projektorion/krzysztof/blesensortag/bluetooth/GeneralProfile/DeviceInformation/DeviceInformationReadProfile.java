package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeCharacteristicReadCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.GenericGattReadProfileInterface;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationReadProfile implements GenericGattReadProfileInterface {
    public static final int ATTRIBUTE_DEVINFO_SYSTEM_ID = 0b1;
    public static final int ATTRIBUTE_DEVINFO_MODEL_NUMBER = 0b10;
    public static final int ATTRIBUTE_DEVINFO_SERIAL_NUMBER = 0b100;
    public static final int ATTRIBUTE_DEVINFO_FIRMWARE_REV = 0b1000;
    public static final int ATTRIBUTE_DEVINFO_HARDWARE_REV = 0b10000;
    public static final int ATTRIBUTE_DEVINFO_SOFTWARE_REV = 0b100000;
    public static final int ATTRIBUTE_DEVINFO_MANUFACTURER_NAME = 0b1000000;


    public static final UUID DEVICE_INFORMATION_SERVICE =
            UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_SYSTEM_ID =
            UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_MODEL_NUMBER =
            UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_SERIAL_NUMBER =
            UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_FIRMWARE_REV =
            UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_HARDWARE_REV =
            UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_SOFTWARE_REV =
            UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
    public static final UUID DEVINFO_MANUFACTURER_NAME =
            UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");

    private Map<Integer, UUID> attrToUuidProfiles;

    private BLeGattIO gattIO;
    private BluetoothGattService service;

    @SuppressLint("UseSparseArrays")
    public DeviceInformationReadProfile(BLeGattIO gattIO) {
        this.gattIO = gattIO;
        attrToUuidProfiles = new HashMap<>();
        init();
    }

    @Override
    public void demandReadCharacteristics(int attributeToBeRead) {
        service = getService();
        if( service == null ) return;

        for( int attributeId : attrToUuidProfiles.keySet() )
        {
            final UUID attributeUuid = attrToUuidProfiles.get(attributeId);
            if( isMatched(attributeToBeRead, attributeId) )
                queue_characteristic_reading(attributeUuid);
        }
    }

    @Override
    public String getName() {
        return AppContext.getContext().getString(R.string.profile_device_information);
    }

    @Override
    public boolean isService(UUID serviceUuid) {
        return DEVICE_INFORMATION_SERVICE.equals(serviceUuid);
    }

    private void init()
    {
        attrToUuidProfiles.put(ATTRIBUTE_DEVINFO_SYSTEM_ID, DEVINFO_SYSTEM_ID);
        attrToUuidProfiles.put(ATTRIBUTE_DEVINFO_MODEL_NUMBER, DEVINFO_MODEL_NUMBER);
        attrToUuidProfiles.put(ATTRIBUTE_DEVINFO_SERIAL_NUMBER, DEVINFO_SERIAL_NUMBER);
        attrToUuidProfiles.put(ATTRIBUTE_DEVINFO_FIRMWARE_REV, DEVINFO_FIRMWARE_REV);
        attrToUuidProfiles.put(ATTRIBUTE_DEVINFO_HARDWARE_REV, DEVINFO_HARDWARE_REV);
        attrToUuidProfiles.put(ATTRIBUTE_DEVINFO_SOFTWARE_REV, DEVINFO_SOFTWARE_REV);
        attrToUuidProfiles.put(ATTRIBUTE_DEVINFO_MANUFACTURER_NAME, DEVINFO_MANUFACTURER_NAME);
    }

    private BluetoothGattService getService()
    {
        return gattIO.getService(DEVICE_INFORMATION_SERVICE);
    }

    private boolean isMatched(int value, int compareTo)
    {
        return (value & compareTo) != 0;
    }

    private void queue_characteristic_reading(UUID uuidToBeRead)
    {
        final BluetoothGattCharacteristic characteristicToBeRead =
                service.getCharacteristic(uuidToBeRead);
        gattIO.addRead(new BLeCharacteristicReadCommand(gattIO, characteristicToBeRead));
    }
}
