package com.ahmed_beheiri.kitcheninphone.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RecipeDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "recipe.db";

    private static final int DATABASE_VERSION = 3;

    public RecipeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE = "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_PUBLISHER + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_SOURCEURL + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_USER_RATING + " REAL NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        onCreate(db);

    }
}
