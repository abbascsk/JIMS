package com.cybersoft.jims_collector;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Globariables extends Application
{
//    public static String BASE_URL = "http://bgitijarat.com/jims_api/api/";
    public static String BASE_URL = "http://api.proact-pos.com/jims_api/api/";

    public static String SP_C_ID = "C_ID";
    public static String SP_FULLNAME = "Name";
    public static String SP_BALANCE = "Balance";

    public static int PSN_READ_PH_ST = 5253;

    public boolean hasPermission(AppCompatActivity act, String permission)
    {

        if (ContextCompat.checkSelfPermission(act, permission) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }

        return false;
    }

    public boolean NetConnected()
    {
        boolean connected = false;

        ConnectivityManager conMan = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
        {
            connected = true;
        }

        return connected;
    }

    public String AppVersion()
    {
        try
        {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return "N/A";
        }
    }

    /*public String GetIMEI()
    {
        try
        {
            TelephonyManager mTelephonyMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            return mTelephonyMgr.getDeviceId();
        }
        catch (SecurityException ex)
        {
            ex.printStackTrace();
            return "";
        }
    }*/

    public void SetSharedPrefs(String key, String value)
    {
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        prefs.edit().putString(key, value).commit();
    }

    public String GetSharedPrefs(String key)
    {
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        return prefs.getString(key, "N/A");
    }

    public static String DateLongToString(Long dateInt, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format,
                Locale.ENGLISH);
        Date newDate = new Date(dateInt);
        String ddate = formatter.format(newDate);

        return ddate;
    }

    public static String DateToString(Date date, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format,
                Locale.ENGLISH);
        return formatter.format(date);
    }

    public static Date StringToDate(String date, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        Date newdate = null;

        try
        {
            newdate = formatter.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return newdate;
    }

    public static long DateStringToLong(String date, String format)
    {
        long dt = 0;

        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
            Date msgdate;
            msgdate = formatter.parse(date);
            dt = msgdate.getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dt;
    }

    public String getStringResourceByName(String aString)
    {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    public static AlertDialog myDialog(Context context, String title, String content)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(content)
                .setTitle(title);
        return builder.create();
    }

    public static AlertDialog myDialog(Context context, String title, String content,
                                       String positiveBtnName, String negativebtnName,
                                       DialogInterface.OnClickListener positiveBtn, DialogInterface.OnClickListener negativeBtn)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(content)
                .setTitle(title)
                .setPositiveButton(positiveBtnName, positiveBtn)
                .setNegativeButton(negativebtnName, negativeBtn);

        return builder.create();
    }

    public static AlertDialog myListDialog(Context context, String[] data, String title,
                                           DialogInterface.OnClickListener onclick)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(data, onclick);

        return builder.create();
    }

    public static AlertDialog myMultiChoiceDialog(Context context, String[] data, String title,
                                                  String positiveBtnName, String negativebtnName,
                                                  DialogInterface.OnClickListener positiveBtn,
                                                  DialogInterface.OnClickListener negativeBtn,
                                                  DialogInterface.OnMultiChoiceClickListener multichoice)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setPositiveButton(positiveBtnName, positiveBtn)
                .setNegativeButton(negativebtnName, negativeBtn)
                .setMultiChoiceItems(data, null, multichoice);

        return builder.create();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    fun tintImage(bitmap: Bitmap, color: Int): Bitmap {
//    val paint = Paint()
//    paint.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN))
//    val bitmapResult = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(bitmapResult)
//    canvas.drawBitmap(bitmap, 0f, 0f, paint)
//    return bitmapResult
//}

}
