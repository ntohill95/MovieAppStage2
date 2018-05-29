package com.example.niamhtohill.movieappudacity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUri();
    static final int MOVIE = 100;
    private MoviesDbHelper moviesDbHelper;

    static UriMatcher buildUri(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.MOVIE_PATH, MOVIE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        moviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case MOVIE:{
                cursor = moviesDbHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri : "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case MOVIE:{
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            }default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)){
            case MOVIE: {
                long id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if(id >0){
                    returnUri = MoviesContract.MoviesEntry.buildMovieUri(id);
                }else {
                    throw new SQLException("Failed to insert row to :" +uri);
                }
                break;
            }default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        int moviesDeleted;
        if(null ==selection) {
            selection = "1";
        }
        switch (uriMatcher.match(uri)){
            case MOVIE:{
                moviesDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();
        int rowsUpdated;
        switch (uriMatcher.match(uri)){
            case MOVIE:{
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if(rowsUpdated !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
