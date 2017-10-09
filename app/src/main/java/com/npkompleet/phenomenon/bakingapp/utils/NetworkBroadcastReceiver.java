package com.npkompleet.phenomenon.bakingapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.npkompleet.phenomenon.bakingapp.R;

/**
 * Created by PHENOMENON on 6/1/2017.
 */

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    Context mContext;
    ConnectivityListener mListener;

    public NetworkBroadcastReceiver(ConnectivityListener listener) {
        super();
        mListener= listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext= context;
        if (networkActive()){
            //RecipeListActivity.initLoader();
            Toast.makeText(mContext, mContext.getString(R.string.network_restored), Toast.LENGTH_SHORT).show();
            mListener.onRefresh();
        }
    }

    public boolean networkActive() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public interface ConnectivityListener{
        void onRefresh();
    }
}
