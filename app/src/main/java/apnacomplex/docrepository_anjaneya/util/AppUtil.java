package apnacomplex.docrepository_anjaneya.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import apnacomplex.docrepository_anjaneya.R;
import apnacomplex.docrepository_anjaneya.dialog.NetworkError_Dialog;
import apnacomplex.docrepository_anjaneya.network.NetworkUtil;


/**
 * Created by Ruthvik on 01-09-2016.
 */

public final class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    private AppUtil() {

    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isConnectedToNetwork = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            isConnectedToNetwork = networkInfo.isConnected();
        }
        return isConnectedToNetwork;
    }

    public static ProgressDialog showProgressBar(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void dismissProgressBar(ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }


    public static void checkNetworkShowDialog(final Context mContext) {
        if (!AppUtil.isNetworkAvailable(mContext)) {
            final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
            networkError_dialog.showDialog(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppUtil.isNetworkAvailable(mContext)) {
                        networkError_dialog.dismissDialog();
                    } else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

        }
    }
}