package com.ahmed_beheiri.kitcheninphone.database;

import android.net.Uri;
import android.provider.BaseColumns;


public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.ahmed_beheiri.kitcheninphone";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String RECIPE_PATH = "recipe";

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(RECIPE_PATH)
                .build();

        public static final String TABLE_NAME = "recipe";

        public static final String ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PUBLISHER = "publisher";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_SOURCEURL = "source_url";
    }
}
