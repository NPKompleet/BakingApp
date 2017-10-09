package com.npkompleet.phenomenon.bakingapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;

/**
 * Created by PHENOMENON on 10/5/2017.
 */

public class MediaReceiver extends BroadcastReceiver {
    MediaListener mediaListener;

    public MediaReceiver(){}

    public MediaReceiver(MediaListener listener){
        super();
        mediaListener= listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MediaButtonReceiver.handleIntent(mediaListener.getMediaSession(), intent);
    }

    public interface MediaListener{
        MediaSessionCompat getMediaSession();
    }
}
