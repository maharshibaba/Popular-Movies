package com.example.bittu.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends ArrayAdapter<Movies> {
    public MovieAdapter(Context context, ArrayList<Movies> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_movie, parent, false);
        }
        final Movies currentMovie = getItem(position);


        ImageView image = (ImageView) listItemView.findViewById(R.id.imageView);
        image.setAdjustViewBounds(true);
        if (image == null) {
            image.setVisibility(View.GONE);
        }

        Picasso.with(getContext()).load(currentMovie.getPoster()).into(image);
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),DetailActivity.class);
                i.putExtra("id",currentMovie.getId());
                i.putExtra("title",currentMovie.getTitle());
                i.putExtra("synopsis",currentMovie.getOverView());
                i.putExtra("releaseDate",currentMovie.getRelease());
                i.putExtra("vote",currentMovie.getVote());
                i.putExtra("poster",currentMovie.getPoster());
                getContext().startActivity(i);
            }
        });

        return listItemView;
    }

}