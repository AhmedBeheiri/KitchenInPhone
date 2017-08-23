package com.ahmed_beheiri.kitcheninphone;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.ahmed_beheiri.kitcheninphone.database.RecipeContract;


public class Recipes implements Parcelable {
    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel source) {
            return new Recipes(source);
        }


        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };


    private String ID;
    private String Title;
    private String publisher;
    private String imageurl;
    private String sourceurl;
    private double Rate;
    private String ingredients;

    public Recipes(String id, String title, String publisher, String imageurl, String sourceurl, double rate, String ingredients) {
        ID = id;
        Title = title;
        this.publisher = publisher;
        this.imageurl = imageurl;
        this.sourceurl = sourceurl;
        Rate = rate;
        this.ingredients = ingredients;
    }

    public Recipes(String id, String title, String publisher, String imageurl, String sourceurl, double rate) {
        ID = id;
        Title = title;
        this.publisher = publisher;
        this.imageurl = imageurl;
        this.sourceurl = sourceurl;
        Rate = rate;
    }

    public Recipes(String Id, String source_url, String Ingredients) {
        ID = Id;
        sourceurl = source_url;
        this.ingredients = Ingredients;
    }

    protected Recipes(Parcel in) {

        ID = in.readString();
        Title = in.readString();
        this.publisher = in.readString();
        this.imageurl = in.readString();
        Rate = in.readDouble();
        this.sourceurl = in.readString();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.Title);
        dest.writeString(this.publisher);
        dest.writeString(this.imageurl);
        dest.writeDouble(this.Rate);
        dest.writeString(this.ingredients);
        dest.writeString(this.sourceurl);
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public double getRate() {
        return Rate;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }


    public boolean saveToFavourits(Context context) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(RecipeContract.RecipeEntry.ID, this.ID);
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_TITLE, this.Title);
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_IMAGE, this.imageurl);
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_PUBLISHER, this.publisher);
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS, this.ingredients);
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_SOURCEURL, this.sourceurl);
        contentValues.put(RecipeContract.RecipeEntry.COLUMN_USER_RATING, this.Rate);
        if (context.getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, contentValues) != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetlist);
            RecipeWidget.updateAppWidget(context, appWidgetManager, appWidgetIds);
            Toast.makeText(context, R.string.ADDED_FAVOURITE, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, R.string.ADDED_FAVOURITE_ERROR, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean removeFromFavourite(Context context) {
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(this.ID)).build();

        long movieDeleted = context.getContentResolver().delete(uri, null, null);
        if (movieDeleted > 0) {
            Toast.makeText(context, R.string.REMOVED_FAVOURITE, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, R.string.REMOVED_FAVOURITE_ERROR, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean isFavourited(Context context) {
        Cursor cursor = context.getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                new String[]{RecipeContract.RecipeEntry.ID},
                RecipeContract.RecipeEntry.ID + "=?",
                new String[]{this.ID}, null);
        if (cursor != null) {
            boolean favorited = cursor.getCount() > 0;
            cursor.close();
            return favorited;
        }
        return false;
    }

}
