package com.xiongxh.popularmovies.sync;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class MovieSyncUtils {
    public static void startImmediateSync(@NonNull final Context context){
        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
