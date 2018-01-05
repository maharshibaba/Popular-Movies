package com.example.bittu.popularmovies.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.bittu.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DETAIL = "movie";

    public static final class DetailEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DETAIL);

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAIL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAIL;

        public static final String TABLE_NAME = "detail";

        public static final String _ID = BaseColumns._ID;

        public static final String MOVIE_ID = "movieId";

        public static final String TITLE = "title";

        public static final String SYNOPSIS = "synopsis";

        public static final String RELEASE_DATE= "releaseDate";

        public static final String VOTE = "vote";

        public static final String POSTER_URL ="poster";


    }
}
