package support.fuchsia.deviceinfo;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

public class DisplayInfo {

    private Activity activity;

    public static final int ORIENTATION_LANDSCAPE = 0;
    public static final int ORIENTATION_PORTRAIT = 1;

    public DisplayInfo(Activity activity) {
        this.activity = activity;
    }


    public double getScreenSizeWidth() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.widthPixels / dm.xdpi;
    }

    public double getScreenSizeHeight() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.heightPixels / dm.ydpi;
    }

    public double getScreenSize() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);
    }

    public int getResolutionWidth() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int getResolutionHeight() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public double getDpiWidth() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.xdpi;
    }

    public double getDpiHeight() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.ydpi;
    }

    public double getDpiDensity() {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    public final String getDensity() {

        String densityStr = "";
        final int density = activity.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                densityStr = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                densityStr = "MDPI";
                break;
            case DisplayMetrics.DENSITY_TV:
                densityStr = "TVDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                densityStr = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                densityStr = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_400:
                densityStr = "XMHDPI";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                densityStr = "XXHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                densityStr = "XXXHDPI";
                break;
            default:
                break;
        }
        return densityStr;
    }


    public final float getRefreshRate() {
        WindowManager wm = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        if (display != null) {
            return display.getRefreshRate();
        } else return 0;
    }

    public final int[] getDisplayXYCoordinates(final MotionEvent event) {
        int[] coordinates = new int[2];
        coordinates[0] = 0;
        coordinates[1] = 0;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            coordinates[0] = (int) event.getX();
            coordinates[1] = (int) event.getY();
        }
        return coordinates;
    }

    public int getNavigationBarHeight() {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        return titleBarHeight;
    }

    public int getSoftButtonsBarHeight() {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public int getMinimumScreenBrightnessSetting() {
        final Resources res = Resources.getSystem();
        int id = res.getIdentifier("config_screenBrightnessSettingMinimum", "integer", "android"); // API17+
        if (id == 0)
            id = res.getIdentifier("config_screenBrightnessDim", "integer", "android"); // lower API levels
        if (id != 0) {
            try {
                return res.getInteger(id);
            } catch (Resources.NotFoundException e) {
                // ignore
            }
        }
        return 0;
    }

    public int getMaximumScreenBrightnessSetting() {
        final Resources res = Resources.getSystem();
        final int id = res.getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android");  // API17+
        if (id != 0) {
            try {
                return res.getInteger(id);
            } catch (Resources.NotFoundException e) {
                // ignore
            }
        }
        return 255;
    }

    public void setBrightnessSystem(int value) {

        Settings.System.putInt(activity.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, value);

    }

    public void setMaxBrightnessForActivity() {

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        activity.getWindow().setAttributes(params);

    }

    public void setMinBrightnessForActivity() {

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
        activity.getWindow().setAttributes(params);

    }

    public void resetBrightnessForActivity() {

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        activity.getWindow().setAttributes(params);

    }

    public String getOrientation() {

        final int orientation = activity.getResources().getConfiguration().orientation;
        switch (orientation) {

            case 2:
                return "ORIENTATION_LANDSCAPE";

            case 1:
                return "ORIENTATION_PORTRAIT";

            case 0:
                return "ORIENTATION_SQUARE";
            default:
                return "ERROR";

        }


    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void setOrientation(int orientation) {

        switch (orientation) {
            case ORIENTATION_LANDSCAPE:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case ORIENTATION_PORTRAIT:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default:
                break;
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void changeScreenOrientation() {
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
        if (Settings.System.getInt(activity.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }, 4000);
        }

    }

    public void toggleFullscreen() {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags ^= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
    }

    public void enterFullscreen() {
        // Jellybean and up, new hotness
        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
       /* ActionBar actionBar = activity.();
        actionBar.hide();*/
    }

    public void exitFullscreen() {
        // Jellybean and up, new hotness
        View decorView = activity.getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        /* ActionBar actionBar = activity.getActionBar();
        actionBar.show();*/
    }


    private String densityDpiToString(int densityDpi) {
        String str;
        switch (densityDpi) {
            case 120:
                str = "ldpi";
                break;
            case 160:
                str = "mdpi";
                break;
            case 213:
                str = "tvdpi";
                break;
            case 240:
                str = "hdpi";
                break;
            case 320:
                str = "xhdpi";
                break;
            case 480:
                str = "xxhdpi";
                break;
            case 640:
                str = "xxxhdpi";
                break;
            default:
                str = "N/A";
                break;
        }
        return densityDpi + " (" + str + ")";
    }

    private String getResolutionString(int rw, int rh) {
        return rw > rh ? "-" + rw + "x" + rh : "-" + rh + "x" + rw;
    }

    private String getSmallestWidthString(int dipWidth, int dipHeight) {
        StringBuilder stringBuilder = new StringBuilder("-sw");
        if (dipWidth >= dipHeight) {
            dipWidth = dipHeight;
        }
        return stringBuilder.append(dipWidth).append("dp").toString();
    }


}
