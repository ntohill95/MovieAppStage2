package com.example.niamhtohill.movieappudacity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
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

public class MovieDetails extends AppCompatActivity{
    TextView movieTitle;
    TextView movieRelease;
    TextView movieSynop;
    TextView movieVote;
    ImageView movieImage;
    TextView movieRuntime;
    TableLayout videoTable;
    TableRow videoTableRow;
    TextView trailerTextView;

    //public HashMap<String, List<VideoObject>> videos = new HashMap<>();
    public List<VideoObject> videos = new ArrayList<>();

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
        //videoTable = findViewById(R.id.table_layout);

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

            new VideoLoader(MovieDetails.this, movieId).execute();

            System.out.println("*******HOW MANY VIDEOS" + videos.size());

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
    public void setVideoList(List videos){
        System.out.println("IN SET VIDEO LIST*******");
        this.videos = videos;
        System.out.println("*******VIDEOS*********"+videos.size());
        displayVideoTable();
    }

    public void displayVideoTable(){
        videoTable = findViewById(R.id.table_layout);
        //videoTableRow = findViewById(R.id.table_row);
        //trailerTextView =findViewById(R.id.trailer_name_tv);
        for(int i=0 ; i < videos.size();i++) {
            View videoTableRow = LayoutInflater.from(this).inflate(R.layout.table_row, null, false);
            TextView trailerTextView = videoTableRow.findViewById(R.id.trailer_name_tv);
            final VideoObject videoObject = videos.get(i);
            trailerTextView.setText(videoObject.getVideoName());
            videoTable.addView(videoTableRow);
            //videoTableRow = LayoutInflater.from(this, inflate(R.layout.table_row,getParent(),false));
            //videoTable.addView(videoTableRow, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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
