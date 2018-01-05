package com.example.bittu.popularmovies;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bittu.popularmovies.data.MovieContract;
import com.example.bittu.popularmovies.data.MovieDBHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;


public class DetailFragment extends Fragment {

    private static final String LOG_CAT = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";
    Uri mUri;
    Uri mContentUri;
    String synopsis;
    String id;
    String title;
    String releaseDate;
    String vote;
    String poster;
    Button favouriteButton;
    LinearLayout trailerListView;
    LinearLayout reviewsListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favouriteButton = (Button) getActivity().findViewById(R.id.favourite);
        TextView titleView = (TextView) getActivity().findViewById(R.id.title);
        TextView synopsisView = (TextView) getActivity().findViewById(R.id.synopsis);
        TextView releaseView = (TextView) getActivity().findViewById(R.id.release_date);
        TextView voteView = (TextView) getActivity().findViewById(R.id.rating);
        ImageView posterView = (ImageView) getActivity().findViewById(R.id.poster);
        TextView readMoreView = (TextView) getActivity().findViewById(R.id.read_more);
        trailerListView = (LinearLayout) getActivity().findViewById(R.id.trailerListView);
        reviewsListView = (LinearLayout) getActivity().findViewById(R.id.reviewListView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
            Intent i = getActivity().getIntent();
            id = i.getStringExtra("id");
            title = i.getStringExtra("title");
            synopsis = i.getStringExtra("synopsis");
            releaseDate = i.getStringExtra("releaseDate");
            vote = i.getStringExtra("vote");
            poster = i.getStringExtra("poster");

            favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markAsFavourite();
                }
            });
            readMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Showdialog();
                }
            });
            titleView.setText(title);
            synopsisView.setText(synopsis);
            releaseView.setText(releaseDate);
            voteView.setText(vote);
            Picasso.with(getActivity()).load(poster).into(posterView);


            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute("https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=e6dfe794cce9a31940e46676883d8321&language=en-US");

            DownloadTask1 downloadTask1 = new DownloadTask1();
            downloadTask1.execute("https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=e6dfe794cce9a31940e46676883d8321&language=en-US&page=1");
        }
    }

    void markAsFavourite() {
        mContentUri = ContentUris.withAppendedId(MovieContract.DetailEntry.CONTENT_URI, Long.parseLong(id));
        Log.i(LOG_CAT, mContentUri.toString());
        ContentValues values = new ContentValues();
        values.put(MovieContract.DetailEntry.MOVIE_ID, Integer.parseInt(id));
        values.put(MovieContract.DetailEntry.TITLE, title);
        values.put(MovieContract.DetailEntry.SYNOPSIS, synopsis);
        values.put(MovieContract.DetailEntry.RELEASE_DATE, releaseDate);
        values.put(MovieContract.DetailEntry.VOTE, vote);
        values.put(MovieContract.DetailEntry.POSTER_URL, poster);

        if (mContentUri != null) {
            MovieDBHelper db = new MovieDBHelper(getActivity());
            Cursor cursor=db.getReadableDatabase().rawQuery("select * from detail where movieId = "+id,null);
//          Cursor cursor = getActivity().getContentResolver().query(MovieContract.DetailEntry.CONTENT_URI, new String[]{MovieContract.DetailEntry.MOVIE_ID},id,null,null);
            if (cursor.getCount() <=0) {
                Uri newUri = getActivity().getContentResolver().insert(MovieContract.DetailEntry.CONTENT_URI, values);
                // Show a toast message depending on whether or not the insertion was successful.
                if (newUri == null) {

                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Marked this movie as favourite",
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                int newId=getActivity().getContentResolver().delete(MovieContract.DetailEntry.CONTENT_URI, "movieId=?", new String[]{id});
                if(newId != -1){
                    Toast.makeText(getActivity(), "Removed this movie as favourite",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    void Showdialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle("SYNOPSIS");

        alertDialogBuilder.setMessage(synopsis);


        // set dialog message
        alertDialogBuilder.setCancelable(false).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int id) {
                dialog.cancel();

            }
        });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    public void updateTrailerUI(ArrayList<Trailers> trailer) {
        for (int i = 0; i < trailer.size(); i++) {
            /**
             * inflate items/ add items in linear layout instead of listview
             */
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getActivity().getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.trailer_list_item, null);
            /**
             * getting id of row.xml
             */
            TextView mFirstName = (TextView) mLinearView
                    .findViewById(R.id.trailer_text_view);
            ImageView image = (ImageView) mLinearView.findViewById(R.id.play_iamge);

            /**
             * set item into row
             */
            final String fName = trailer.get(i).getName();
            mFirstName.setText(fName);

            image.setImageResource(R.drawable.ic_play_arrow_black_48dp);

            final String trailerUrl = trailer.get(i).getUrl();

            /**
             * add view in top linear
             */

            trailerListView.addView(mLinearView);

            /**
             * get item row on click
             *
             */
            mLinearView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
                }
            });
        }
    }

    public void updateReviewsUI(ArrayList<Reviews> reviews) {
        for (int i = 0; i < reviews.size(); i++) {
            /**
             * inflate items/ add items in linear layout instead of listview
             */
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getActivity().getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.review_list_item, null);
            /**
             * getting id of row.xml
             */
            final TextView reviewContent = (TextView) mLinearView
                    .findViewById(R.id.review_text_view);
            TextView reviewAuthor = (TextView) mLinearView
                    .findViewById(R.id.author_text);

            /**
             * set item into row
             */
            final String fContent = reviews.get(i).getContent();
            reviewContent.setText(fContent);

            final String fAuthor = reviews.get(i).getName();
            reviewAuthor.setText("-" + fAuthor);

            /**
             * add view in top linear
             */

            reviewsListView.addView(mLinearView);

            /**
             * get item row on click
             *
             */
            mLinearView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());
                    alertDialogBuilder.setTitle("REVIEW");

                    alertDialogBuilder.setMessage(fContent);


                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            dialog.cancel();

                        }
                    });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            });
        }
    }


    public class DownloadTask extends AsyncTask<String, Void, ArrayList<Trailers>> {
        @Override
        protected ArrayList<Trailers> doInBackground(String... urls) {
            ArrayList<Trailers> trailer = null;
            try {
                trailer = TrailerQueryUtils.fetchMovies(urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trailer;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailers> trailers) {
            updateTrailerUI(trailers);
        }
    }

    public class DownloadTask1 extends AsyncTask<String, Void, ArrayList<Reviews>> {
        @Override
        protected ArrayList<Reviews> doInBackground(String... urls) {
            ArrayList<Reviews> review = null;
            try {
                review = ReviewsQueryUtil.fetchMovies(urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return review;
        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> review) {
            updateReviewsUI(review);
        }
    }
}
