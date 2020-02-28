package support.fuchsia.deviceinformation.ui.display;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private TextView screen_size_txt, screen_width_txt, screen_height_txt, screen_resolution_txt, dpi_width_txt, dpi_height_txt, dpi_density_txt, navigationbar_height_txt, statusbar_height_txt, soft_buttonbar_height_txt, screen_minimum_brightness_txt, screen_maximum_brightness_txt, screen_refresh_rate_txt, orientation_txt;
    private SeekBar screen_brightness_seekbar;
    private Button screen_max_brightness_app_btn, screen_min_brightness_app_btn, screen_reset_brightness_app_btn, orientation_portrait_btn, orientation_landscape_btn, change_orientation_btn;

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
        screen_refresh_rate_txt = (TextView) root.findViewById(R.id.screen_refresh_rate_txt);
        orientation_txt = (TextView) root.findViewById(R.id.orientation_txt);


        screen_brightness_seekbar = (SeekBar) root.findViewById(R.id.screen_brightness_seekbar);

        screen_max_brightness_app_btn = (Button) root.findViewById(R.id.screen_max_brightness_app_btn);
        screen_min_brightness_app_btn = (Button) root.findViewById(R.id.screen_min_brightness_app_btn);
        screen_reset_brightness_app_btn = (Button) root.findViewById(R.id.screen_reset_brightness_app_btn);
        orientation_portrait_btn = (Button) root.findViewById(R.id.orientation_portrait_btn);
        orientation_landscape_btn = (Button) root.findViewById(R.id.orientation_landscape_btn);
        change_orientation_btn = (Button) root.findViewById(R.id.change_orientation_btn);
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
        screen_refresh_rate_txt.setText(String.valueOf(displayInfo.getRefreshRate()));
        orientation_txt.setText(String.valueOf(displayInfo.getOrientation()));


        screen_brightness_seekbar.setMin(displayInfo.getMinimumScreenBrightnessSetting());
        screen_brightness_seekbar.setMax(displayInfo.getMaximumScreenBrightnessSetting());

        screen_brightness_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (checkSystemWritePermission())
                    displayInfo.setBrightnessSystem(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        screen_min_brightness_app_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo.setMinBrightnessForActivity();
            }
        });
        screen_max_brightness_app_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo.setMaxBrightnessForActivity();
            }
        });
        screen_reset_brightness_app_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo.resetBrightnessForActivity();
            }

        });
        orientation_portrait_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo.setOrientation(DisplayInfo.ORIENTATION_PORTRAIT);
            }

        });
        orientation_landscape_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo.setOrientation(DisplayInfo.ORIENTATION_LANDSCAPE);
            }

        });
        change_orientation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayInfo.changeScreenOrientation();
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
