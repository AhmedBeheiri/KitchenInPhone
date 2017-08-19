package com.ahmed_beheiri.kitcheninphone.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.ahmed_beheiri.kitcheninphone.database.RecipeContract.RecipeEntry.TABLE_NAME;


public class RecipeProvider extends ContentProvider {
    public static final int RECIPE = 100;
    public static final int RECIPE_WITH_ID = 101;

    private static final UriMatcher sMatcher = buildUriMatcher();

    private RecipeDB Helper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RecipeContract.RECIPE_PATH, RECIPE);

        matcher.addURI(authority, RecipeContract.RECIPE_PATH + "/#", RECIPE_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        Helper = new RecipeDB(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = Helper.getReadableDatabase();
        int match = sMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case RECIPE:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Unknown uri" + uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = Helper.getWritableDatabase();
        int match = sMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RECIPE:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = Helper.getWritableDatabase();
        int match = sMatcher.match(uri);
        int recipedeleted;

        switch (match) {
            case RECIPE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                recipedeleted = db.delete(TABLE_NAME, "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (recipedeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return recipedeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
