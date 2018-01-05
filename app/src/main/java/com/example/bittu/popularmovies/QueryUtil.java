package com.example.bittu.popularmovies;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtil {
    private static String LOG_TAG = QueryUtil.class.getSimpleName();

    private QueryUtil() {
    }

    public static ArrayList<Movies> fetchMovies(String requestUrl) throws JSONException {
        // Create URL object
        URL url = null;
        url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

       ArrayList<Movies> movies = extractMovies(jsonResponse);

        return movies;
    }

    private static ArrayList<Movies> extractMovies(String movieJsonStr) throws JSONException {

        ArrayList<Movies> movies = new ArrayList<Movies>();

        JSONObject forecastJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = forecastJson.getJSONArray("results");

        for (int i = 0;i<movieArray.length();i++){
            JSONObject movieObject = movieArray.getJSONObject(i);
            String movieId = movieObject.getString("id");
            String movieTitle = movieObject.getString("original_title");
            String movieRelease = movieObject.getString("release_date");
            String movieVote = movieObject.getString("vote_average");
            String movieOverview = movieObject.getString("overview");
            String moviePoster = movieObject.getString("poster_path");
            String moviePosterUrl = "http://image.tmdb.org/t/p/w185" + moviePoster;
            movies.add(new Movies(movieId,movieTitle,movieRelease,movieVote,movieOverview,moviePosterUrl));
        }
        return movies;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse ="";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }





}
