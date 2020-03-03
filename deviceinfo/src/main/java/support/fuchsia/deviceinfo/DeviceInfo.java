package support.fuchsia.deviceinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

public class DeviceInfo {
    private Activity activity;

    public DeviceInfo(Activity activity) {
        this.activity = activity;
    }

    public String getDeviceSerialNumber() {
        @SuppressLint("HardwareIds") String serial = Build.SERIAL;
        return serial;
    }

    public String getDeviceModelNumber() {
        @SuppressLint("HardwareIds") String model = Build.MODEL;
        return model;
    }

    public String getDeviceID() {
        @SuppressLint("HardwareIds") String id = Build.ID;
        return id;
    }

    public String getDeviceManufacturer() {
        @SuppressLint("HardwareIds") String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }

    public String getDeviceBrand() {
        @SuppressLint("HardwareIds") String brand = Build.BRAND;
        return brand;
    }

    public String getDeviceType() {
        @SuppressLint("HardwareIds") String type = Build.TYPE;
        return type;
    }

    public String getDeviceUser() {
        @SuppressLint("HardwareIds") String user = Build.USER;
        return user;
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public String getSDKName() {
        return Build.VERSION.RELEASE;
    }

    public String getDeviceBoard() {
        return Build.BOARD;
    }

    public String getDeviceHost() {
        return Build.HOST;
    }

    public String getDeviceFingerprintInfo() {
        return Build.FINGERPRINT;
    }

    public String getCPU1Info() {
        return Build.CPU_ABI;
    }

    public String getCPU2Info() {
        return Build.CPU_ABI2;
    }

    public String getDisplayInfo() {
        return Build.DISPLAY;
    }

    public String getHardwareInfo() {
        return Build.HARDWARE;
    }

    public String getProductName() {
        return Build.PRODUCT;
    }

    public String getMacAddress() {
        // add this permission to manifest if using this function : <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
        WifiManager manager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert manager != null;
        @SuppressLint("MissingPermission") WifiInfo info = manager.getConnectionInfo();
        @SuppressLint("HardwareIds") String address = info.getMacAddress();
        return address;
    }

}
