package example.com.eldareini.eldarmovieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.Movie;
import example.com.eldareini.eldarmovieapp.objects.ReviewItem;

import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class ReviewDownloadTask extends AsyncTask<MediaItem, Void, ArrayList<ReviewItem>> {
    private Context context;
    private OnReviewDownloadListener listener;

    public ReviewDownloadTask(Context context, OnReviewDownloadListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected ArrayList<ReviewItem> doInBackground(MediaItem... params) {

        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();

        try {
            URL url = null;
            switch (params[0].getMediaType()){
                case MEDIA_TYPE_MOVIE:

                    url = new URL("https://api.themoviedb.org/3/movie/"+ params[0].getId() +"/reviews?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_TV:

                    url = new URL("https://api.themoviedb.org/3/tv/"+ params[0].getId() +"/reviews?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;
            }

            connection = (HttpsURLConnection) url.openConnection();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK){

                Log.e("ReviewDownloadTask", "Can't Download Reviews");
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();

            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            JSONObject obj = new JSONObject(builder.toString());
            JSONArray rootArray = obj.getJSONArray("results");
            for (int i = 0; i < rootArray.length(); i++){

                JSONObject reviewObject = rootArray.getJSONObject(i);
                String author = reviewObject.getString("author");
                String content = reviewObject.getString("content");

                reviewItems.add(new ReviewItem(author, content));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return reviewItems;

    }

    @Override
    protected void onPostExecute(ArrayList<ReviewItem> reviewItems) {
        super.onPostExecute(reviewItems);

        listener.getReviews(reviewItems);
    }

    public interface OnReviewDownloadListener{
        void getReviews(ArrayList<ReviewItem> reviewItems);
    }
}
