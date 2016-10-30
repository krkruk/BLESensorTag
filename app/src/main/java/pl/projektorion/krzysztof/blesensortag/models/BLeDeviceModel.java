package pl.projektorion.krzysztof.blesensortag.models;

import android.bluetooth.BluetoothDevice;

/**
 * Created by krzysztof on 26.10.16.
 */

public class BLeDeviceModel {

    private BluetoothDevice device;
    private int rssi;
    private byte[] broadcastData;


    public BLeDeviceModel(BluetoothDevice device, int rssi, byte[] broadcastData) {
        this.device = device;
        this.rssi = rssi;
        this.broadcastData = broadcastData;
    }

    public BluetoothDevice getDevice()
    {
        return device;
    }

    public void setDevice(BluetoothDevice device)
    {
        this.device = device;
    }

    public int getRssi()
    {
        return rssi;
    }

    public void setRssi(int rssi)
    {
        this.rssi = rssi;
    }

    public byte[] getBroadcastData()
    {
        return broadcastData;
    }

    public void setBroadcastData(byte[] broadcastData)
    {
        this.broadcastData = broadcastData;
    }
}
