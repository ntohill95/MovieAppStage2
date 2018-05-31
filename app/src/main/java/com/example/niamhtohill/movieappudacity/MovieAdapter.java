package com.example.niamhtohill.movieappudacity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niamhtohill on 24/05/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    public List<Movie> movies;
    private LayoutInflater layoutInflater;

    public MovieAdapter(Context context, List<Movie> movies){
        this.movies = movies;
        this.context=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView movieImage;

        private Context context;

        public MyViewHolder(View view){
            super(view);
            movieImage = view.findViewById(R.id.movie_grid_image);
            context = view.getContext();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        itemView.setFocusable(true);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Movie movie = movies.get(position);
        String imageUrl = movie.getMoviePosterUrl();
        //w185 recommended for most phones
        String link = "http://image.tmdb.org/t/p/w185";
        link = link + imageUrl;
        Picasso.with(context).load(link).into(holder.movieImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieTitle = movie.getMovieTitle();
                String movieRelease = movie.getMovieReleaseDate();
                String movieImage = movie.getMoviePosterUrl();
                String movieSynopsis = movie.getMovieSynopsis();
                String movieVote = movie.getMovieVoteAverage().toString();
                String movieID = movie.getMovieID().toString();

                Intent intent = new Intent(context, MovieDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("movieTitle", movieTitle);
                bundle.putString("movieRelease",movieRelease);
                bundle.putString("movieSynop",movieSynopsis);
                bundle.putString("movieImage",movieImage);
                bundle.putString("movieVote",movieVote);
                bundle.putString("movieID", movieID);

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addAll(List<Movie> movieArrayList){
        movies.clear();
        movies.addAll(movieArrayList);
        notifyDataSetChanged();
    }
}
