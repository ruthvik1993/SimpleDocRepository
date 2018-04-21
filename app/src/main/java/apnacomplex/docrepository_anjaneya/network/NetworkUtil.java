package apnacomplex.docrepository_anjaneya.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abhilash on 01-09-2016.
 */

public final class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static String getRequestBodyOfJson(HashMap map) {
        JSONObject jsonObject = new JSONObject();

        if (map != null && map.size() != 0) {
            Iterator myVeryOwnIterator = map.keySet().iterator();

            try {


                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();

                    Object obj = map.get(key);
                    System.out.print(obj.getClass());
                    if (obj.getClass().equals(String.class)) {
                        jsonObject.put(key, (String) obj);
                    } else if (obj.getClass().equals(Integer.class)) {
                        jsonObject.put(key, (Integer) obj);
                    } else if (obj.getClass().equals(Float.class)) {
                        jsonObject.put(key, (Float) obj);
                    } else if (obj.getClass().equals(boolean.class)) {
                        jsonObject.put(key, (boolean) obj);
                    } else if (obj.getClass().equals(Double.class)) {
                        jsonObject.put(key, (Double) obj);
                    }
                }
            } catch (JSONException e) {

            }
        }

        return jsonObject.toString();

    }
}