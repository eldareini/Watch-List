package example.com.eldareini.eldarmovieapp.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import example.com.eldareini.eldarmovieapp.Constant;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;

public class DownloadMediaTask extends AsyncTask<String, Void, ArrayList<MediaItem>> {
    private OnDownloadMovieListener listener;
    private Context context;

    public DownloadMediaTask(Context context, OnDownloadMovieListener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Toast.makeText(getContext(), "Searching...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ArrayList<MediaItem> doInBackground(String... params) {
        HttpsURLConnection httpsConnection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        ArrayList<MediaItem> mediaItems = new ArrayList<>();


        try {
            URL url = null;

            url = new URL("https://api.themoviedb.org/3/search/multi?api_key=07c664e1eda6cd9d46f1da1dbaefc959&query=" + params[0]);

            httpsConnection = (HttpsURLConnection) url.openConnection();
            if (httpsConnection.getResponseCode() != HttpsURLConnection.HTTP_OK ) {

                AlertDialog dialogWeb = new AlertDialog.Builder(context)
                        .setTitle("Connection Problem")
                        .setMessage("Sorry, there was a connection problem :( \nGet WiFi and try again")
                        .create();
                dialogWeb.show();

                return null;
            }

            reader = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));

            String line = reader.readLine();

            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            Constant constant = new Constant();
            JSONObject obj = new JSONObject(builder.toString());

            mediaItems.addAll(constant.getJSONMedia(obj.getJSONArray("results")));



        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaItems;
    }

    //connecting all the movies to the adapter, if there is movies
    @Override
    protected void onPostExecute(ArrayList<MediaItem> mediaItems) {
        super.onPostExecute(mediaItems);
        if (mediaItems == null || mediaItems.size() == 0){
            AlertDialog dialogNoMovie = new AlertDialog.Builder(context)
                    .setTitle("Didn't found your movie")
                    .setMessage("Sorry, \nWe couldn't found your movie :( \nTry Another One")
                    .create();
            dialogNoMovie.show();
        } else {
            listener.getData(mediaItems);
        }
    }

    public interface OnDownloadMovieListener{
        void getData(ArrayList<MediaItem> movies);
    }
}

