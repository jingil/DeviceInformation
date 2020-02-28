package support.fuchsia.deviceinformation.ui.display;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.math.RoundingMode;

import support.fuchsia.deviceinfo.DisplayInfo;
import support.fuchsia.deviceinformation.R;

public class DisplayFragment extends Fragment {

    private DisplayInfo displayInfo;
    private TextView screen_size_txt, screen_width_txt, screen_height_txt, screen_resolution_txt, dpi_width_txt, dpi_height_txt, dpi_density_txt, navigationbar_height_txt, statusbar_height_txt, soft_buttonbar_height_txt, screen_minimum_brightness_txt, screen_maximum_brightness_txt;
    SeekBar screen_maximum_brightness_seekbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_display, container, false);


        displayInfo = new DisplayInfo(getActivity());

        init(root);
        setDisplayDetails();


        displayInfo.getDpiDensity();

        return root;
    }


    private void init(View root) {

        screen_size_txt = (TextView) root.findViewById(R.id.screen_size_txt);
        screen_width_txt = (TextView) root.findViewById(R.id.screen_width_txt);
        screen_height_txt = (TextView) root.findViewById(R.id.screen_height_txt);
        screen_resolution_txt = (TextView) root.findViewById(R.id.screen_resolution_txt);
        dpi_width_txt = (TextView) root.findViewById(R.id.dpi_width_txt);
        dpi_height_txt = (TextView) root.findViewById(R.id.dpi_height_txt);
        dpi_density_txt = (TextView) root.findViewById(R.id.dpi_density_txt);
        navigationbar_height_txt = (TextView) root.findViewById(R.id.navigationbar_height_txt);
        statusbar_height_txt = (TextView) root.findViewById(R.id.statusbar_height_txt);
        soft_buttonbar_height_txt = (TextView) root.findViewById(R.id.soft_buttonbar_height_txt);
        screen_minimum_brightness_txt = (TextView) root.findViewById(R.id.screen_minimum_brightness_txt);
        screen_maximum_brightness_txt = (TextView) root.findViewById(R.id.screen_maximum_brightness_txt);

        screen_maximum_brightness_seekbar = (SeekBar) root.findViewById(R.id.screen_maximum_brightness_seekbar);
    }

    private void setDisplayDetails() {
        screen_size_txt.setText(String.valueOf(round(displayInfo.getScreenSize(), 1)));
        screen_width_txt.setText(String.valueOf(round(displayInfo.getScreenSizeWidth(), 1)));
        screen_height_txt.setText(String.valueOf(round(displayInfo.getScreenSizeHeight(), 1)));
        screen_resolution_txt.setText(String.valueOf(displayInfo.getResolutionHeight() + " X " + displayInfo.getResolutionWidth()));
        dpi_width_txt.setText(String.valueOf(round(displayInfo.getDpiWidth(), 1)));
        dpi_height_txt.setText(String.valueOf(round(displayInfo.getDpiHeight(), 1)));
        dpi_density_txt.setText(String.valueOf(round(displayInfo.getDpiDensity(), 1)));
        navigationbar_height_txt.setText(String.valueOf(round(displayInfo.getNavigationBarHeight(), 2)));
        statusbar_height_txt.setText(String.valueOf(displayInfo.getStatusBarHeight()));
        soft_buttonbar_height_txt.setText(String.valueOf(displayInfo.getSoftButtonsBarHeight()));
        screen_minimum_brightness_txt.setText(String.valueOf(displayInfo.getMinimumScreenBrightnessSetting()));
        screen_maximum_brightness_txt.setText(String.valueOf(displayInfo.getMaximumScreenBrightnessSetting()));

        screen_maximum_brightness_seekbar.setMin(displayInfo.getMinimumScreenBrightnessSetting());
        screen_maximum_brightness_seekbar.setMax(displayInfo.getMaximumScreenBrightnessSetting());

        screen_maximum_brightness_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (checkSystemWritePermission())
                    displayInfo.setBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getActivity()))

                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getActivity()));
            startActivity(intent);
        }
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
