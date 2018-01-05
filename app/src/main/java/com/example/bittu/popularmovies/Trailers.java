package com.example.bittu.popularmovies;


public class Trailers {
    private String mUrl;
    private String mName;

    public Trailers(String url, String name) {
        mUrl=url;
        mName=name;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getName() {
        return mName;
    }


}
