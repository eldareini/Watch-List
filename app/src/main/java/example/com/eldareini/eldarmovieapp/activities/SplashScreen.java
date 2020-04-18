package example.com.eldareini.eldarmovieapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import example.com.eldareini.eldarmovieapp.Constant;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;

import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_NOW;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_POPULAR;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_UPCOMING;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV_ON_AIR;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV_POPULAR;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_UNKNOWN;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBarSplashScreen);
        new DownloadPopularMediaTask().execute(new String[]{"https://api.themoviedb.org/3/movie/now_playing?api_key=07c664e1eda6cd9d46f1da1dbaefc959",
                "https://api.themoviedb.org/3/movie/popular?api_key=07c664e1eda6cd9d46f1da1dbaefc959",
                "https://api.themoviedb.org/3/movie/upcoming?api_key=07c664e1eda6cd9d46f1da1dbaefc959",
                "https://api.themoviedb.org/3/tv/on_the_air?api_key=07c664e1eda6cd9d46f1da1dbaefc959",
                "https://api.themoviedb.org/3/tv/popular?api_key=07c664e1eda6cd9d46f1da1dbaefc959"});

    }

    private class DownloadPopularMediaTask extends AsyncTask<String, Integer, ArrayList<MediaItem>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setMax(120);
            progressBar.setProgress(0);

        }

        @Override
        protected ArrayList<MediaItem> doInBackground(String... strings) {
            int count = 0;
            ArrayList<MediaItem> items = new ArrayList<>();

            for (int i = 0; i < strings.length; i++) {

                HttpsURLConnection connection = null;
                BufferedReader reader = null;
                StringBuilder builder = new StringBuilder();

                try {

                    URL url = new URL(strings[i]);
                    connection = (HttpsURLConnection) url.openConnection();

                    if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = reader.readLine();

                    while (line != null){
                        builder.append(line);
                        line = reader.readLine();
                    }

                    JSONObject object = new JSONObject(builder.toString());
                    JSONArray array = object.getJSONArray("results");

                    int mediaType = MEDIA_TYPE_UNKNOWN;
                    if (strings[i].contains("/tv/on_the_air"))
                        mediaType = MEDIA_TYPE_TV_ON_AIR;
                    else if (strings[i].contains("/tv/popular"))
                        mediaType = MEDIA_TYPE_TV_POPULAR;
                    else if (strings[i].contains("/movie/popular"))
                        mediaType = MEDIA_TYPE_MOVIE_POPULAR;
                    else if (strings[i].contains("/movie/upcoming"))
                        mediaType = MEDIA_TYPE_MOVIE_UPCOMING;
                    else if (strings[i].contains("/movie/now_playing"))
                        mediaType = MEDIA_TYPE_MOVIE_NOW;


                    for (int j = 0; j < array.length(); j++){

                        JSONObject arrayObject = array.getJSONObject(j);
                        int id = arrayObject.getInt("id");
                        String poster_path = arrayObject.getString("poster_path");
                        Constant constant = new Constant();
                        JSONArray genreJsonArray = arrayObject.getJSONArray("genre_ids");
                        int[] genreArray = new int[genreJsonArray.length()];

                        for (int n = 0; n < genreJsonArray.length(); n++){
                            genreArray[n] = genreJsonArray.getInt(n);
                        }

                        String title = "";
                        Date date = null;

                        if (mediaType == MEDIA_TYPE_MOVIE_POPULAR || mediaType == MEDIA_TYPE_MOVIE_NOW
                                || mediaType == MEDIA_TYPE_MOVIE_UPCOMING){

                            title = arrayObject.getString("title");
                            date = constant.getDateFromString(arrayObject.getString("release_date"));

                        } else if (mediaType == MEDIA_TYPE_TV_ON_AIR || mediaType == MEDIA_TYPE_TV_POPULAR){

                            title = arrayObject.getString("name");
                            date = constant.getDateFromString(arrayObject.getString("first_air_date"));

                        }


                        items.add(new MediaItem(id, mediaType, date, title, poster_path, genreArray));
                        if (count < 99){
                            count++;
                            publishProgress(count);
                        }

                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }



            return items;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(ArrayList<MediaItem> mediaItems) {
            super.onPostExecute(mediaItems);

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra(EXTRA_MEDIA, mediaItems);
            startActivity(intent);
            SplashScreen.this.finish();
        }
    }
}
