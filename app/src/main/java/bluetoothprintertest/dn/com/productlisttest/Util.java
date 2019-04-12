package bluetoothprintertest.dn.com.productlisttest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Util {

    public static void log(String message) {
        Log.e("Log", message);
    }

    public static String read(Context ctx, String name, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sp.getString(name, defValue);
    }

    public static int read(Context ctx, String name, int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sp.getInt(name, defValue);
    }

    public static void write(Context ctx, String name, String value) {
        SharedPreferences sp = ctx.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(name, value);
        e.commit();
    }

    public static void write(Context ctx, String name, int value) {
        SharedPreferences sp = ctx.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(name, value);
        e.commit();
    }

    public static void show(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context ctx, int messageId) {
        Toast.makeText(ctx, messageId, Toast.LENGTH_SHORT).show();
    }
}
