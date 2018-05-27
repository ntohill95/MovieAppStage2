package com.example.niamhtohill.movieappudacity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by niamhtohill on 24/05/2018.
 */

public class MovieDetails extends AppCompatActivity{
    TextView movieTitle;
    TextView movieRelease;
    TextView movieSynop;
    TextView movieVote;
    ImageView movieImage;
    TextView movieRuntime;
    TableLayout videoTable;

    public static String month ="";
    public static String day ="";
    public static String year ="";

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
        videoTable = findViewById(R.id.table_layout);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String movieTitleStr = bundle.getString("movieTitle");
            String movieImageStr = bundle.getString("movieImage");
            String movieReleaseStr = bundle.getString("movieRelease");
            String movieSynopsisStr = bundle.getString("movieSynop");
            String movieVoteStr = bundle.getString("movieVote");
            String movieId = bundle.getString("movieID");
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
            movieTitle.setText(movieTitleStr);
            movieRelease.setText(reformatted);
            movieSynop.setText(movieSynopsisStr);
            movieVote.setText(movieVoteStr);
            movieRuntime.setText(movieRuntimeStr + " mins");
            String link = "http://image.tmdb.org/t/p/w185";
            link = link + movieImageStr;
            Picasso.with(this).load(link).into(movieImage);

        }
    }
}
