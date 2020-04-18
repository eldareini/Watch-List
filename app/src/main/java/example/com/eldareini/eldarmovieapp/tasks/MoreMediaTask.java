package example.com.eldareini.eldarmovieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
import example.com.eldareini.eldarmovieapp.objects.MediaItem;

import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_UNKNOWN;

public class MoreMediaTask extends AsyncTask<String, Void, ArrayList<MediaItem>> {
    Context context;
    OnMediaDownloadedListener listener;

    public MoreMediaTask(Context context, OnMediaDownloadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ArrayList<MediaItem> doInBackground(String... strings) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        ArrayList<MediaItem> items = new ArrayList<>();

        try {
            URL url = new URL(strings[0] + "&page=" + strings[1]);
            connection = (HttpsURLConnection) url.openConnection();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();

            while (line != null){
                builder.append(line);
                line = reader.readLine();
            }

            JSONObject object = new JSONObject(builder.toString());

            if (object.getInt("total_pages") <= Integer.parseInt(strings[1])){
                return null;
            }

            Constant constant = new Constant();

            if (strings[0].contains("query")){

                items.addAll(constant.getJSONMedia(object.getJSONArray("results")));
                return items;

            }

            JSONArray array = object.getJSONArray("results");

            int mediaType = MEDIA_TYPE_UNKNOWN;
            if (strings[0].contains("/tv/"))
                mediaType = MEDIA_TYPE_TV;
            else if (strings[0].contains("/movie/"))
                mediaType = MEDIA_TYPE_MOVIE;



            for (int j = 0; j < array.length(); j++){

                JSONObject arrayObject = array.getJSONObject(j);
                int id = arrayObject.getInt("id");
                String poster_path = arrayObject.getString("poster_path");
                JSONArray genreJsonArray = arrayObject.getJSONArray("genre_ids");
                int[] genreArray = new int[genreJsonArray.length()];

                for (int n = 0; n < genreJsonArray.length(); n++){
                    genreArray[n] = genreJsonArray.getInt(n);
                }

                String title = "";
                Date year = null;

                if (mediaType == MEDIA_TYPE_MOVIE){

                    title = arrayObject.getString("title");
                    year = constant.getDateFromString(arrayObject.getString("release_date"));

                } else if (mediaType == MEDIA_TYPE_TV){

                    title = arrayObject.getString("name");
                    year = constant.getDateFromString(arrayObject.getString("first_air_date"));

                }


                items.add(new MediaItem(id, mediaType, year, title, poster_path, genreArray));

            }

            return items;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MediaItem> items) {
        super.onPostExecute(items);
        listener.getMedia(items);
    }

    public interface OnMediaDownloadedListener{
        void getMedia(ArrayList<MediaItem> items);
    }
}
