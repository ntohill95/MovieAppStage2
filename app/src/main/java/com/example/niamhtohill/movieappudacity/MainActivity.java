package com.example.niamhtohill.movieappudacity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String MOVIES_URL_LINK = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String MOVIES_URL_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    final static String API_KEY = "1119711545cd4fbc29520df875c8d677";
    public static  String MOVIES_URL_LINK_FINAL = MOVIES_URL_LINK+API_KEY;

    public static final int MOVIE_ID =1;
    public RecyclerView movieGrid;
    public MovieAdapter movieAdapter;
    public TextView noInternet;
    public View loadingBar;
    public HashMap<String, List<VideoObject>> videos = new HashMap<>();
    public List<VideoObject> videoObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check internet connectivity
        boolean connectionStatus = checkInternet();
        if(connectionStatus){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_ID,null, this).forceLoad();
        }else{
            loadingBar = findViewById(R.id.progressBar);
            loadingBar.setVisibility(View.GONE);
            noInternet = findViewById(R.id.no_connection_tv);
            noInternet.setVisibility(View.VISIBLE);
        }
        movieGrid = findViewById(R.id.grid);
        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        movieGrid.setLayoutManager(layoutManager);
        movieGrid.setItemAnimator(new DefaultItemAnimator());
        movieGrid.setAdapter(movieAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_button){
            final String popular = "Most Popular";
            final String rated = "Highest Rated";
            final String cancel = "Cancel";
            final String favourites = "Favourites";
            final String [] options = {popular,rated,favourites,cancel};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if the user selects "popular" from the settings menu
                    if(options[which] == popular){
                        //need to create a new URL for popular
                        MOVIES_URL_LINK_FINAL = MOVIES_URL_LINK+API_KEY;
                        boolean connectionStatus = checkInternet();
                        //check is there a connection to reorder movies
                        if(connectionStatus){
                            getLoaderManager().restartLoader(0,null,MainActivity.this).forceLoad();
                            noInternet = findViewById(R.id.no_connection_tv);
                            noInternet.setVisibility(View.INVISIBLE);
                        }else{
                            //if no connection then stop loading and display TV to user
                            loadingBar = findViewById(R.id.progressBar);
                            loadingBar.setVisibility(View.GONE);
                            noInternet = findViewById(R.id.no_connection_tv);
                            noInternet.setVisibility(View.VISIBLE);
                            //TODO checking if .removeAllViews() compensates for .clear() not working
                            movieGrid.removeAllViews();
                            //movieAdapter.clear();
                        }
                    }
                    //if the user selects "highest rated" from the menu
                    if(options[which]==rated){
                        //need to create a new URL for rated
                        MOVIES_URL_LINK_FINAL = MOVIES_URL_RATED+API_KEY;
                        boolean connectionStatus = checkInternet();
                        //check is there a connection to reorder movies
                        if(connectionStatus){
                            getLoaderManager().restartLoader(0,null,MainActivity.this).forceLoad();
                            noInternet = findViewById(R.id.no_connection_tv);
                            noInternet.setVisibility(View.INVISIBLE);
                        }else{
                            //if no connection then stop loading and display TV to user
                            loadingBar = findViewById(R.id.progressBar);
                            loadingBar.setVisibility(View.GONE);
                            noInternet = findViewById(R.id.no_connection_tv);
                            noInternet.setVisibility(View.VISIBLE);
                            //TODO checking if .removeAllViews() compensates for .clear() not working
                            movieGrid.removeAllViews();
                            //movieAdapter.clear();
                        }
                    }
                    //display favourite movies
                    if(options[which]==favourites){
                        List<Movie> movies = MoviesDbHelper.ShowFavouriteMovies(MainActivity.this);
                        //TODO checking if .removeAllViews() compensates for .clear() not working
                        movieGrid.removeAllViews();
                        //movieAdapter.clear();
                        if(movieAdapter != null){
                            movieAdapter.addAll(movies);
                            movieAdapter.notifyDataSetChanged();
                        }
                    }
                    //else if they hit cancel
                    else if (options[which]==cancel){
                        dialog.dismiss();
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }else{
         return false;
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this,MOVIES_URL_LINK_FINAL);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        //TODO checking if .removeAllViews() compensates for .clear() not working
        movieGrid.removeAllViews();
        //movieAdapter.clear();
        View loadingBar = findViewById(R.id.progressBar);
        loadingBar.setVisibility(View.GONE);
        if(movieAdapter != null){
            movieAdapter.addAll(movies);
            movieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> movies) {
        //TODO checking if .removeAllViews() compensates for .clear() not working
        movieGrid.removeAllViews();
        //movieAdapter.clear();
    }
}
