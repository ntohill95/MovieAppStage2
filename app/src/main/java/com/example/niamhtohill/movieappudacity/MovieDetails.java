package com.example.niamhtohill.movieappudacity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by niamhtohill on 24/05/2018.
 */

public class MovieDetails extends AppCompatActivity {
    TextView movieTitle;
    TextView movieRelease;
    TextView movieSynop;
    TextView movieVote;
    ImageView movieImage;
    TextView movieRuntime;
    TableLayout videoTable;
    FloatingActionButton favouriteFab;
    TextView reviewsTv;


    public List<VideoObject> videos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        setTitle(R.string.movie_details);

        movieTitle = findViewById(R.id.movie_title_tv);
        movieImage = findViewById(R.id.movie_image);
        movieRelease = findViewById(R.id.movie_release_tv);
        movieSynop = findViewById(R.id.movie_synop);
        movieVote = findViewById(R.id.movie_vote);
        movieRuntime = findViewById(R.id.movie_duration_tv);
        favouriteFab = findViewById(R.id.favourite_fab);
        reviewsTv = findViewById(R.id.movie_review);

        final Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String movieTitleStr = bundle.getString("movieTitle");
            String movieImageStr = bundle.getString("movieImage");
            String movieReleaseStr = bundle.getString("movieRelease");
            String movieSynopsisStr = bundle.getString("movieSynop");
            String movieVoteStr = bundle.getString("movieVote");
            final String movieId = bundle.getString("movieID");
            String movieRuntimeStr =bundle.getString("movieRuntime");
            movieVoteStr = movieVoteStr + "/10";
            SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy");
            String reformatted = "";
            try{
                reformatted = desiredFormat.format((jsonFormat.parse(movieReleaseStr)));
            }catch (ParseException e){
                Log.e("PARSE ERROR", e.getMessage());
            }
            new VideoLoader(MovieDetails.this, movieId).execute();

            movieTitle.setText(movieTitleStr);
            movieRelease.setText(reformatted);
            movieSynop.setText(movieSynopsisStr);
            movieVote.setText(movieVoteStr);
            movieRuntime.setText(movieRuntimeStr + " mins");
            String link = "http://image.tmdb.org/t/p/w185";
            link = link + movieImageStr;
            Picasso.with(this).load(link).into(movieImage);
            final Movie movie = new Movie(movieTitleStr,movieReleaseStr,movieSynopsisStr,Double.parseDouble(bundle.getString("movieVote")),movieImageStr,Integer.parseInt(movieId),movieRuntimeStr);
            //if user taps review textView
            reviewsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MovieDetails.this, ReviewActivity.class);
                    intent.putExtra("movieId", movieId);
                    startActivity(intent);
                }
            });

            favouriteFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //user has favourited movie
                    if(favouriteFab.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.heart).getConstantState()){
                        favouriteFab.setImageResource(R.drawable.heart_full);
                        MoviesDbHelper.AddToFavourites(MovieDetails.this, movie);
                    }//user un-favourites a movie
                    else {
                        favouriteFab.setImageResource(R.drawable.heart);
                        MoviesDbHelper.DeleteFromFavourites(MovieDetails.this, Integer.parseInt(movieId));
                    }

                }
            });
        }
    }
    public void setVideoList(List videos){
        this.videos = videos;
        displayVideoTable();
    }

    public void displayVideoTable(){
        videoTable = findViewById(R.id.table_layout);
        for(int i=0 ; i < videos.size();i++) {
            View videoTableRow = LayoutInflater.from(this).inflate(R.layout.table_row, null, false);
            TextView trailerTextView = videoTableRow.findViewById(R.id.trailer_name_tv);
            final VideoObject videoObject = videos.get(i);
            trailerTextView.setText(videoObject.getVideoName());
            videoTable.addView(videoTableRow);
            videoTableRow.setClickable(true);
            videoTableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  String baseUrl = "https://www.youtube.com/watch?v=";
                  String videoKey = videoObject.getVideoKey();
                  String url = baseUrl+videoKey;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
        }
    }
}
