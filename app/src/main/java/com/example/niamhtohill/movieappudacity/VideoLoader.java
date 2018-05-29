package com.example.niamhtohill.movieappudacity;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.List;

public class VideoLoader extends AsyncTask<String, Void, List<VideoObject>> {
    String movieId;

    public MovieDetails activity;
    public List<VideoObject> videos;

    public VideoLoader(MovieDetails activity, String movieId){
        this.activity =activity;
        this.movieId=movieId;
    }

    @Override
    protected List<VideoObject> doInBackground(String... strings) {
        if(movieId==null){
            return null;
        }
        videos = QueryUtils.getTrailerVideos(movieId);

        return videos;
    }

    @Override
    protected void onPostExecute(List<VideoObject> videoObjects) {
        super.onPostExecute(videoObjects);
        activity.setVideoList(videoObjects);
    }
}
