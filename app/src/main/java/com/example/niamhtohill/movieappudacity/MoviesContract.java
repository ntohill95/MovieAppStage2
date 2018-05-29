package com.example.niamhtohill.movieappudacity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    //to prevent accidentally instantiating constructor class make it private
    private MoviesContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.niamhtohill.movieappudacity";
    public static final Uri BASE_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String MOVIE_PATH = "movie";

    public static class MoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(MOVIE_PATH).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +MOVIE_PATH;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_IMAGE = "image_url";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RATE = "vote";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_RUNTIME = "runtime";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

}
