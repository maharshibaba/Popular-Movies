package com.example.bittu.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.bittu.popularmovies.data.MovieContract.DetailEntry;
import com.squareup.picasso.Picasso;

public class FavAdapter extends CursorAdapter {
    public FavAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {

        ImageView posterImage = (ImageView) view.findViewById(R.id.imageView);
        String posterUlr = cursor.getString(cursor.getColumnIndex(DetailEntry.POSTER_URL));
        Picasso.with(view.getContext()).load(posterUlr).into(posterImage);
        posterImage.setAdjustViewBounds(true);

    }


}
