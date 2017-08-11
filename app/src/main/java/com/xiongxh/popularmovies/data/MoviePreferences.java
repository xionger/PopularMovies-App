package com.xiongxh.popularmovies.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.xiongxh.popularmovies.R;

public final class MoviePreferences {

    //public static String sortType;

    public static String getPreferredSortType(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForSorting = context.getString(R.string.sorting_key);
        String defaultSorting = context.getString(R.string.pref_sorting_default);

        return sp.getString(keyForSorting, defaultSorting);
    }

}
