package support.fuchsia.deviceinfo;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

public class CPUInfo {
    private Activity activity;

    public CPUInfo(Activity activity) {
        this.activity = activity;
    }

    public static String getCPUDetails() {

        ProcessBuilder processBuilder;
        String cpuDetails = "";
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        InputStream is;
        Process process;
        byte[] bArray;
        bArray = new byte[1024];

        try {
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            is = process.getInputStream();

            while (is.read(bArray) != -1) {
                cpuDetails = cpuDetails + new String(bArray);   //Stroing all the details in cpuDetails
            }
            is.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return cpuDetails;
    }


}
