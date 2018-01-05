package com.example.bittu.popularmovies;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bittu.popularmovies.data.MovieContract;

import org.json.JSONException;

import java.util.ArrayList;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    String baseUrl;
    MovieAdapter mMovieAdapter;
    GridView gridView;
    MenuItem favItem;
    FavAdapter mFavAdapter;
    String LOG_CAT = MainActivity.class.getSimpleName();

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri movieUri);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.moviesList);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favCheckbox:
                if (!item.isChecked()) {
                    getLoaderManager().initLoader(0, null, this);
                    mFavAdapter = new FavAdapter(getActivity(), null);
                    gridView.setAdapter(mFavAdapter);
                    item.setChecked(true);
                } else if (item.isChecked()) {
                    baseUrl = "http://api.themoviedb.org/3/movie/popular?api_key=e6dfe794cce9a31940e46676883d8321";
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(baseUrl);
                    item.setChecked(false);

                }
                return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu);
        favItem = menu.findItem(R.id.favCheckbox);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinnerOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data networ

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            if (!favItem.isChecked()) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Uri.Builder urlBuilder = new Uri.Builder();
                        urlBuilder.scheme(getString(R.string.url_scheme))
                                .authority(getString(R.string.base_url))
                                .appendPath("3")
                                .appendPath("movie");
                        DownloadTask downloadTask = new DownloadTask();
                        if (position == 0) {
                            urlBuilder.appendPath(getString(R.string.category_popular));
                        }
                        if (position == 1) {
                            urlBuilder.appendPath(getString(R.string.category_top));
                        }
                        urlBuilder.appendQueryParameter("api_key", getString(R.string.API_KEY));  //Put Your own api key 
                        downloadTask.execute(urlBuilder.toString());
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        } else {
            Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateUI(ArrayList<Movies> movies) {
        mMovieAdapter = new MovieAdapter(getActivity(), movies);
        gridView.setAdapter(mMovieAdapter);

    }

    public class DownloadTask extends AsyncTask<String, Void, ArrayList<Movies>> {
        @Override
        protected ArrayList<Movies> doInBackground(String... urls) {
            ArrayList<Movies> movies = null;
            try {
                movies = QueryUtil.fetchMovies(urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            updateUI(movies);
        }
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MovieContract.DetailEntry._ID,
                MovieContract.DetailEntry.MOVIE_ID,
                MovieContract.DetailEntry.TITLE,
                MovieContract.DetailEntry.SYNOPSIS,
                MovieContract.DetailEntry.RELEASE_DATE,
                MovieContract.DetailEntry.VOTE,
                MovieContract.DetailEntry.POSTER_URL};

        return new android.content.CursorLoader(getActivity(),
                MovieContract.DetailEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, final Cursor data) {
        mFavAdapter.swapCursor(data);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("id", data.getString(data.getColumnIndex(MovieContract.DetailEntry.MOVIE_ID)));
                i.putExtra("title", data.getString(data.getColumnIndex(MovieContract.DetailEntry.TITLE)));
                i.putExtra("synopsis", data.getString(data.getColumnIndex(MovieContract.DetailEntry.SYNOPSIS)));
                i.putExtra("releaseDate", data.getString(data.getColumnIndex(MovieContract.DetailEntry.RELEASE_DATE)));
                i.putExtra("vote", data.getString(data.getColumnIndex(MovieContract.DetailEntry.VOTE)));
                i.putExtra("poster", data.getString(data.getColumnIndex(MovieContract.DetailEntry.POSTER_URL)));
                startActivity(i);
            }
        });
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}
