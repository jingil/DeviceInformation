package support.fuchsia.deviceinfo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

public class SensorInfo {

    private Activity activity;
    private List<Sensor> sensorList;

    public SensorInfo(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<String> getSensorList() {

        SensorManager mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager != null) {
            sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL); //get list of sensors
        }
        ArrayList<String> sensorNames = new ArrayList<String>(); //list of sensor names

        //iterate through list of sensors, get name of each and append to list of names
        for (int i = 0; i < sensorList.size(); i++) {
            String sensor = sensorList.get(i).getName();
            if (sensor == null || sensor.equals(""))
                continue;
            sensorNames.add(sensor);
        }

        return sensorNames;
    }


}
