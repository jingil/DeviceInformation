package support.fuchsia.deviceinfo;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Window;

public class DisplayInfo {

    Activity activity;

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

    public void setBrightness(int value) {

        Settings.System.putInt(activity.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, value);

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
