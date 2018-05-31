package com.example.niamhtohill.movieappudacity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;
    public MoviesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "+
                MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "+
                MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE + " TEXT, "+
                MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_RATE + " INTEGER, "+
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT )";
                //MoviesContract.MoviesEntry.COLUMN_RUNTIME + " TEXT );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }

    public static void AddToFavourites(Context context, Movie movie){
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID , movie.getMovieID());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, movie.getMovieTitle());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE, movie.getMoviePosterUrl());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW, movie.getMovieSynopsis());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATE, movie.getMovieVoteAverage());
        values.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getMovieReleaseDate());
        //values.put(MoviesContract.MoviesEntry.COLUMN_RUNTIME, movie.getMovieRunTime());

        context.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, values);
    }

    public static int DeleteFromFavourites(Context context, int id){
        return context.getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?", new String[]{Integer.toString(id)});
    }

    public static List<Movie> ShowFavouriteMovies (Context context){
        Cursor cursor;
        try{
            cursor = context.getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null,null,null,null);
        }catch (Exception e){
            Log.e("Cursor Error ", e.getMessage());
            return null;
        }
        int numberOfRows = cursor.getCount();
        List<Movie> movies= new ArrayList<>();
        Movie movie = null;
        String movieTitle;
        String movieImage;
        String movieDate;
        String movieOverview;
        Double movieVote;
        Integer movieId;
        String movieRuntime;

        cursor.moveToFirst();

        for (int i =0; i< numberOfRows; i++){
            movieTitle = cursor.getString(cursor.getColumnIndex("name"));
            movieImage = cursor.getString(cursor.getColumnIndex("image_url"));
            movieDate = cursor.getString(cursor.getColumnIndex("date"));
            movieOverview = cursor.getString(cursor.getColumnIndex("overview"));
            movieVote = cursor.getDouble(cursor.getColumnIndex("vote"));
            movieId = cursor.getInt(cursor.getColumnIndex("id"));
            //movieRuntime=cursor.getString(cursor.getColumnIndex("runtime"));

            movie.setMovieTitle(movieTitle);
            movie.setMoviePosterUrl(movieImage);
            movie.setMovieReleaseDate(movieDate);
            movie.setMovieSynopsis(movieOverview);
            movie.setMovieVoteAverage(movieVote);
            movie.setMovieID(movieId);
            //movie.setMovieRunTime(movieRuntime);

            cursor.moveToNext();
            movies.add(movie);
        }
        cursor.close();
        return movies;
    }

    public static boolean isMovieFavouited(Context context, Movie movie){
        List<Movie> favouritedMovies = ShowFavouriteMovies(context);
        if(favouritedMovies.contains(movie)){
            return true;
        }else {
            return false;
        }
    }
}
