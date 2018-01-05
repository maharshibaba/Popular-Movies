package com.example.bittu.popularmovies;


public class Movies {
    private String mId;
    private String mTitle;
    private String mRelease;
    private String mVote;
    private String mOverView;
    private String mPoster;

    public Movies(String id, String title, String release, String vote, String overview, String poster) {
        mId = id;
        mTitle = title;
        mRelease = release;
        mVote = vote;
        mOverView = overview;
        mPoster = poster;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getRelease() {
        return mRelease;
    }

    public String getVote() {
        return mVote;
    }

    public String getOverView() {
        return mOverView;
    }

    public String getPoster() {
        return mPoster;
    }
}
