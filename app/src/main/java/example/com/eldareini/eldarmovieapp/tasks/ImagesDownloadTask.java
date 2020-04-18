package example.com.eldareini.eldarmovieapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import example.com.eldareini.eldarmovieapp.objects.ImageItem;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_EPISODE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_PERSON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class ImagesDownloadTask extends AsyncTask<MediaItem, Void, ArrayList<ImageItem>> {
    private OnImagesDownloadListener listener;

    public ImagesDownloadTask(OnImagesDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<ImageItem> doInBackground(MediaItem... params) {

        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        ArrayList<ImageItem> images = new ArrayList<>();

        try {

            URL url = null;

            switch (params[0].getMediaType()){
                case MEDIA_TYPE_MOVIE:

                    url = new URL("https://api.themoviedb.org/3/movie/"+ params[0].getId() +"/images?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    break;

                case MEDIA_TYPE_TV:

                    url = new URL("https://api.themoviedb.org/3/tv/"+ params[0].getId() +"/images?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_PERSON:

                    url = new URL("https://api.themoviedb.org/3/person/"+ params[0].getId() +"/images?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_EPISODE:
                    TvEpisode episode = (TvEpisode) params[0];
                    TvSeason season = (TvSeason) params[1];
                    TvSeries series = (TvSeries) params[2];

                    url = new URL("https://api.themoviedb.org/3/tv/"+series.getId()+"/season/"+
                            season.getSeasonNum()+"/episode/"+episode.getEpisodeNum()+"/images?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;
            }

            connection = (HttpsURLConnection) url.openConnection();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
                Log.e("ImagesDownloadTask", "can't download images ");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();

            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }

            JSONObject object = new JSONObject(builder.toString());
            JSONArray imagesArray = null;
            if (params[0].getMediaType() == MEDIA_TYPE_MOVIE || params[0].getMediaType() == MEDIA_TYPE_TV) {
                imagesArray = object.getJSONArray("backdrops");


            } else if (params[0].getMediaType() == MEDIA_TYPE_PERSON){

                imagesArray = object.getJSONArray("profiles");


            } else if (params[0].getMediaType() == MEDIA_TYPE_EPISODE){

                imagesArray = object.getJSONArray("stills");
            }

            for (int i = 0; i < imagesArray.length(); i++) {
                JSONObject imageObject = imagesArray.getJSONObject(i);
                images.add(new ImageItem(imageObject.getString("file_path")));
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return images;
    }

    @Override
    protected void onPostExecute(ArrayList<ImageItem> imageItems) {
        super.onPostExecute(imageItems);

        listener.getImages(imageItems);
    }

    public interface OnImagesDownloadListener{
        void getImages(ArrayList<ImageItem> images);
    }
}
