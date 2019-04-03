package com.sistec.helperClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Spinner;

import com.sistec.sistecstudents.R;
import com.tapadoo.alerter.Alerter;

import java.lang.reflect.Field;

public class MyHelperClass {

    private static ProgressDialog pd;

    public static void showProgress(Activity activity, String title, String msg) {
        pd = new ProgressDialog(activity);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(title);
        pd.setMessage(msg);
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    public static void hideProgress() {
        pd.dismiss();
    }

    public static void showAlerter(Activity activity, String title, String msg, int iconResId) {
        Alerter.create(activity)
                .setText(msg)
                .setTitle(title)
                .setIcon(iconResId)
                .setBackgroundColorRes(R.color.colorPrimaryDark)
                .setDuration(4000)
                .enableSwipeToDismiss()
                .show();
    }

    public static void setSpinnerHeight(Spinner spinner) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(600);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "UNKNOWN";
        }
    }
}
