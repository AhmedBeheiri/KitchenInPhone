package com.ahmed_beheiri.kitcheninphone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ahmed_beheiri.kitcheninphone.database.RecipeContract;

import static com.ahmed_beheiri.kitcheninphone.database.RecipeContract.BASE_CONTENT_URI;
import static com.ahmed_beheiri.kitcheninphone.database.RecipeContract.RECIPE_PATH;


public class ListViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mcontext;
        Cursor mcursor;

        public ListRemoteViewsFactory(Context applicationcontext) {
            mcontext = applicationcontext;

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            Uri RECIPE_URI = BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();
            if (mcursor != null) mcursor.close();
            mcursor = mcontext.getContentResolver().query(RECIPE_URI, null, null, null, null);

        }

        @Override
        public void onDestroy() {
            mcursor.close();

        }

        @Override
        public int getCount() {
            if (mcursor == null) return 0;
            return mcursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (mcursor == null || mcursor.getCount() == 0)
                return null;

            mcursor.moveToPosition(position);
            int Idindex = mcursor.getColumnIndex(RecipeContract.RecipeEntry.ID);
            int sourceurlindex = mcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SOURCEURL);
            int Titleindex = mcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_TITLE);
            int publisherindex = mcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_PUBLISHER);
            int rateindex = mcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_USER_RATING);
            int Imageindex = mcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE);
            double rate = mcursor.getDouble(rateindex);
            String imageurl = mcursor.getString(Imageindex);
            String id = mcursor.getString(Idindex);
            String sourceurl = mcursor.getString(sourceurlindex);
            String Title = mcursor.getString(Titleindex);
            String publisher = mcursor.getString(publisherindex);

            Recipes recipes = new Recipes(id, Title, publisher, imageurl, sourceurl, rate);

            RemoteViews remoteViews = new RemoteViews(mcontext.getPackageName(), R.layout.list_row);
            remoteViews.setTextViewText(R.id.heading, Title);
            remoteViews.setTextViewText(R.id.content, publisher);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("recipe", recipes);
            remoteViews.setOnClickFillInIntent(R.id.heading, fillInIntent);
            return remoteViews;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
