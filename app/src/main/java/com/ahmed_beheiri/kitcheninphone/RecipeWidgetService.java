package com.ahmed_beheiri.kitcheninphone;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.ahmed_beheiri.kitcheninphone.database.RecipeContract.RecipeEntry;

import static com.ahmed_beheiri.kitcheninphone.database.RecipeContract.BASE_CONTENT_URI;
import static com.ahmed_beheiri.kitcheninphone.database.RecipeContract.RECIPE_PATH;


public class RecipeWidgetService extends IntentService {
    public static final String ACTION_GET_DATA = "com.ahmed_beheiri.kitcheninphone.RecipeWidgetService.GET_DATA";

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    public static void startActionGetData(Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_GET_DATA);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(ACTION_GET_DATA)) {
                handleActionGetData();
            }
        }

    }


    private void handleActionGetData() {
        Uri RecipeUri = BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();
        Cursor cursor = getContentResolver().query(RecipeUri, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int titleindex = cursor.getColumnIndex(RecipeEntry.COLUMN_TITLE);
            int publisherindex = cursor.getColumnIndex(RecipeEntry.COLUMN_PUBLISHER);
            String title = cursor.getString(titleindex);
            String publisher = cursor.getString(publisherindex);
            cursor.close();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetlist);
        RecipeWidget.updateAppWidget(this, appWidgetManager, appWidgetIds);
    }
}
