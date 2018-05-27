package com.example.niamhtohill.movieappudacity;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by niamhtohill on 24/05/2018.
 */

public final class QueryUtils {
    private QueryUtils(){
    }
    private static URL buildUrl(String apiUrl){
        URL url = null;
        try{
            url = new URL(apiUrl);
        }catch(MalformedURLException e){
            Log.e("ERROR MESSAGE = ",e.getMessage());
        }
        return url;
    }
    public static List<Movie> retrieveJsonData(String apiUrl){
        URL url = buildUrl(apiUrl);
        String jsonData = null;
        try{
            jsonData = makeHttpRequest(url);
        }catch (IOException e){
            Log.e("ERROR = ", e.getMessage());
        }
        List<Movie> movies = createMovieObjects(jsonData);
        return movies;
    }
    private static String makeHttpRequest(URL url)throws IOException{
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        String movieJsonStr = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode==200){
                in = urlConnection.getInputStream();
                movieJsonStr = readFromStream(in);
            }else{
                Log.e("ERROR RESPONSE CODE = ", "Response Code: " +responseCode);
            }
        }catch (IOException e){
            Log.e("ERROR : ",e.getMessage());
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(in !=null){
                in.close();
            }
        }
        return movieJsonStr;
    }

    private static String readFromStream(InputStream in) throws IOException{
        StringBuilder jsonOutput = new StringBuilder();
        if(in !=null){
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                jsonOutput.append(line);
                line=reader.readLine();
            }
        }
        return jsonOutput.toString();
    }
    private static List<Movie> createMovieObjects(String jsonData){
        if(TextUtils.isEmpty(jsonData)){
            return null;
        }
        List<Movie> movies = new ArrayList<Movie>();
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject.has("results")){
                JSONArray movieArray = jsonObject.getJSONArray("results");
                for(int i =0; i< movieArray.length(); i++){
                    JSONObject movieObject = movieArray.getJSONObject(i);
                    String movieImageUrl = movieObject.getString("poster_path");
                    String movieTitle = movieObject.getString("original_title");
                    String movieSynopsis = movieObject.getString("overview");
                    String movieRelease = movieObject.getString("release_date");
                    Double movieAverage = movieObject.getDouble("vote_average");
                    Integer movieID = movieObject.getInt("id");
                    //determine runtime of movieObject
                    String movieRuntime = getRunTime(movieID.toString());
                    movies.add(new Movie(movieTitle,movieRelease,movieSynopsis,movieAverage,movieImageUrl,movieID,movieRuntime));
                }
            }
        }catch (JSONException e){
            Log.e("JSON ERROR: ", e.getMessage());
        }
        return movies;
    }

    public static String getRunTime(String movieID){
        String movieBaseLink = "https://api.themoviedb.org/3/movie/";
        String API_KEY = "?api_key=1119711545cd4fbc29520df875c8d677&language=en-US";

        String totalDurationURL = movieBaseLink + movieID + API_KEY;
        String movieRuntime = "0";
        URL url = buildUrl(totalDurationURL);
        String movieJsonStr = null;
        try{
            HttpURLConnection urlConnection = null;
            InputStream in = null;

            try{
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if(responseCode==200){
                    in = urlConnection.getInputStream();
                    movieJsonStr = readFromStream(in);

                    //determine runtime of movieObject
                    try {

                        JSONObject jsonObject1 = new JSONObject(movieJsonStr);
                        if(jsonObject1.has("runtime")){
                            movieRuntime = jsonObject1.getString("runtime");
                        }
                    }catch (JSONException e){
                        Log.e("JSON EXCEPTION", e.getMessage());
                    }

                }else{
                    Log.e("ERROR RESPONSE CODE = ", "Response Code: " +responseCode);
                }
            }catch (IOException e){
                Log.e("ERROR : ",e.getMessage());
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(in !=null){
                    in.close();
                }
            }
        }catch (IOException e){
            Log.e("ERROR = ", e.getMessage());
        }
        return movieRuntime;
    }

    public static List<VideoObject> getTrailerVideos(String movieID){
        String movieBaseLink = "https://api.themoviedb.org/3/movie/";
        String API_KEY = "/videos?api_key=1119711545cd4fbc29520df875c8d677&language=en-US";
        String totalApiURL = movieBaseLink + movieID + API_KEY;
        URL url = buildUrl(totalApiURL);
        String videoJsonStr = null;
        List<VideoObject> videos = new ArrayList<>();
        try{
            HttpURLConnection urlConnection = null;
            InputStream in = null;
            try{
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if(responseCode==200){
                    in = urlConnection.getInputStream();
                    videoJsonStr = readFromStream(in);
                }else{
                    Log.e("ERROR RESPONSE CODE = ", "Response Code: " +responseCode);
                }
                //getting trailers related to movieObject
                try {

                    JSONObject jsonObject2 = new JSONObject(videoJsonStr);
                    if (jsonObject2.has("results")) {
                        JSONArray jsonArray = jsonObject2.getJSONArray("results");
                        if (jsonArray.length() > 0) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject videoObject = jsonArray.getJSONObject(j);
                                String videoID = videoObject.getString("id");
                                String videoKey = videoObject.getString("key");
                                String videoTitle = videoObject.getString("name");
                                String videoSite = videoObject.getString("site");
                                String videoType = videoObject.getString("type");
                                videos.add(new VideoObject(videoID, videoTitle, videoKey, videoSite, videoType));
                                System.out.println("************videos found");
                            }
                        }
                    }
                }catch (JSONException e){
                    Log.e("VIDEO JSON ERROR", e.getMessage());
                }
            }catch (IOException e){
                Log.e("ERROR : ",e.getMessage());
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(in !=null){
                    in.close();
                }
            }
        }catch (IOException e){
            Log.e("ERROR = ", e.getMessage());
        }
        return videos;

    }
}
