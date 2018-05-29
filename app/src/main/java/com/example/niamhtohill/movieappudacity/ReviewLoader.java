package com.example.niamhtohill.movieappudacity;

import android.os.AsyncTask;

import java.util.List;

public class ReviewLoader extends AsyncTask<String, Void, List<ReviewObject>> {
    String movieId;

    public ReviewActivity activity;
    public List<ReviewObject> reviews;

    public ReviewLoader(ReviewActivity activity, String movieId){
        this.activity = activity;
        this.movieId=movieId;
    }

    @Override
    protected List<ReviewObject> doInBackground(String... strings) {
        if(movieId==null){
            return null;
        }
        reviews = QueryUtils.getVideoReview(movieId);
        return reviews;
    }

    @Override
    protected void onPostExecute(List<ReviewObject> reviewObjects) {
        super.onPostExecute(reviewObjects);
        activity.setReviewList(reviewObjects);
    }
}
