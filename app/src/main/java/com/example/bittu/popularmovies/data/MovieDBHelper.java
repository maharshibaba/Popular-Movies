package com.example.bittu.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bittu.popularmovies.data.MovieContract.DetailEntry;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_DETAIL_TABLE = "CREATE TABLE " + DetailEntry.TABLE_NAME + " (" +
                DetailEntry._ID + " INTEGER PRIMARY KEY," +
                DetailEntry.MOVIE_ID + " TEXT UNIQUE,"
                + DetailEntry.TITLE + " TEXT NOT NULL," +
                DetailEntry.SYNOPSIS + " TEXT NOT NULL," +
                DetailEntry.RELEASE_DATE + " TEXT NOT NULL," +
                DetailEntry.VOTE + " TEXT NOT NULL," +
                DetailEntry.POSTER_URL + " TEXT NOT NULL" +
                " );";

        db.execSQL(SQL_CREATE_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
