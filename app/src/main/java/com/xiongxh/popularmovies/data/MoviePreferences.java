package com.xiongxh.popularmovies.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.xiongxh.popularmovies.R;
import com.xiongxh.popularmovies.utilities.ConstantsUtils;

public final class MoviePreferences {

    //public static String sortType;

    public static String getPreferredSortType(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForSorting = context.getString(R.string.sorting_key);
        String defaultSorting = context.getString(R.string.pref_sorting_default);

        return sp.getString(keyForSorting, defaultSorting);
    }

    public static void updateFavoriteMessage(String favoriteTag, String movieTitle, Context context){

        String message = "";

        if (favoriteTag.equals(ConstantsUtils.FAVORITE_TAG)){
            message = movieTitle + ConstantsUtils.MESSAGE_REMOVE_FAVORITE;
        } else {
            message = movieTitle + ConstantsUtils.MESSAGE_ADD_FAVORITE;
        }

        Toast favoriteMessage = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        favoriteMessage.show();
    }

}
