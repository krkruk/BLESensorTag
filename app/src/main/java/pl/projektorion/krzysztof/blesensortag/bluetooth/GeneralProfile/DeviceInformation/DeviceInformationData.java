package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.ProfileStringData;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationData implements ProfileStringData {
    public static final int ATTRIBUTE_DEVINFO_SYSTEM_ID =
            DeviceInformationReadProfile.ATTRIBUTE_DEVINFO_SYSTEM_ID;
    public static final int ATTRIBUTE_DEVINFO_MODEL_NUMBER =
            DeviceInformationReadProfile.ATTRIBUTE_DEVINFO_MODEL_NUMBER;
    public static final int ATTRIBUTE_DEVINFO_SERIAL_NUMBER =
            DeviceInformationReadProfile.ATTRIBUTE_DEVINFO_SERIAL_NUMBER;
    public static final int ATTRIBUTE_DEVINFO_FIRMWARE_REV =
            DeviceInformationReadProfile.ATTRIBUTE_DEVINFO_FIRMWARE_REV;
    public static final int ATTRIBUTE_DEVINFO_HARDWARE_REV =
            DeviceInformationReadProfile.ATTRIBUTE_DEVINFO_HARDWARE_REV;
    public static final int ATTRIBUTE_DEVINFO_SOFTWARE_REV =
            DeviceInformationReadProfile.ATTRIBUTE_DEVINFO_SOFTWARE_REV;
    public static final int ATTRIBUTE_DEVINFO_MANUFACTURER_NAME =
            DeviceInformationReadProfile.ATTRIBUTE_DEVINFO_MANUFACTURER_NAME;

    private String systemId = "";
    private String modelNumber = "";
    private String serialNumber = "";
    private String firmwareRevision = "";
    private String hardwareRevision = "";
    private String softwareRevision = "";
    private String manufacturerName = "";

    public DeviceInformationData() {}

    public DeviceInformationData(BluetoothGattCharacteristic characteristic) {
        setValue(characteristic);
    }

    @Override
    public String getValue(int dataAttribute) {
        switch (dataAttribute)
        {
            case ATTRIBUTE_DEVINFO_SYSTEM_ID:
                return systemId;
            case ATTRIBUTE_DEVINFO_MODEL_NUMBER:
                return modelNumber;
            case ATTRIBUTE_DEVINFO_SERIAL_NUMBER:
                return serialNumber;
            case ATTRIBUTE_DEVINFO_FIRMWARE_REV:
                return firmwareRevision;
            case ATTRIBUTE_DEVINFO_HARDWARE_REV:
                return hardwareRevision;
            case ATTRIBUTE_DEVINFO_SOFTWARE_REV:
                return softwareRevision;
            case ATTRIBUTE_DEVINFO_MANUFACTURER_NAME:
                return manufacturerName;
            default:
                return "";
        }
    }

    @Override
    public void setValue(BluetoothGattCharacteristic characteristic) {
        parse(characteristic);
        Log.i("Values:", String.format("SysId: %s;  Model#: %s;  Serial#: %s;  FirmwareRev: %s" +
                " HardwareRev: %s;  SoftwareRev: %s;  Manufacturer: %s",
                systemId, modelNumber, serialNumber, firmwareRevision, hardwareRevision,
                softwareRevision, manufacturerName));
    }

    private void parse(BluetoothGattCharacteristic characteristic)
    {
        final UUID characteristicUuid = characteristic.getUuid();

        if( DeviceInformationReadProfile.DEVINFO_SYSTEM_ID.equals(characteristicUuid) )
            set_system_id(characteristic.getValue());
        else if( DeviceInformationReadProfile.DEVINFO_MODEL_NUMBER.equals(characteristicUuid) )
            modelNumber = characteristic.getStringValue(0);
        else if( DeviceInformationReadProfile.DEVINFO_SERIAL_NUMBER.equals(characteristicUuid) )
            serialNumber = characteristic.getStringValue(0);
        else if( DeviceInformationReadProfile.DEVINFO_FIRMWARE_REV.equals(characteristicUuid) )
            firmwareRevision = characteristic.getStringValue(0);
        else if( DeviceInformationReadProfile.DEVINFO_HARDWARE_REV.equals(characteristicUuid) )
            hardwareRevision = characteristic.getStringValue(0);
        else if( DeviceInformationReadProfile.DEVINFO_SOFTWARE_REV.equals(characteristicUuid) )
            softwareRevision = characteristic.getStringValue(0);
        else if( DeviceInformationReadProfile.DEVINFO_MANUFACTURER_NAME.equals(characteristicUuid) )
            manufacturerName = characteristic.getStringValue(0);
    }
    private void set_system_id(byte[] data)
    {
        final StringBuilder builder = new StringBuilder();
        for(byte dat : data)
            builder.append(String.format("%02x ", dat));
        final String systemIdText = builder.toString();
        systemId = systemIdText.trim().toUpperCase();
    }
}
