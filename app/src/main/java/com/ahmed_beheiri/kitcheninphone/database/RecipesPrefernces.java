package com.ahmed_beheiri.kitcheninphone.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ahmed_beheiri.kitcheninphone.R;


public class RecipesPrefernces {
    public static String getPreferredSorting(Context context) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String Sort = context.getString(R.string.pref_sorting);
        String defaultSorting = context.getString(R.string.pref_sorting_default);
        return prefs.getString(Sort, defaultSorting);
    }
}
