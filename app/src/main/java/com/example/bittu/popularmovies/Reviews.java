package com.example.bittu.popularmovies;


public class Reviews {
    private String mContent;
    private String mName;

    public Reviews(String content, String name) {
        mContent=content;
        mName=name;
    }

    public String getContent() {
        return mContent;
    }

    public String getName() {
        return mName;
    }

}
