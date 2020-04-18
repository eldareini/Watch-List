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
import java.util.Date;


import javax.net.ssl.HttpsURLConnection;

import example.com.eldareini.eldarmovieapp.Constant;
import example.com.eldareini.eldarmovieapp.OnDetailsDownloadListener;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;;
import example.com.eldareini.eldarmovieapp.objects.Movie;
import example.com.eldareini.eldarmovieapp.objects.Person;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_EPISODE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_PERSON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_SEASON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class DetailsMediaDownloadTask extends AsyncTask<MediaItem, Void, MediaItem> {
    private OnDetailsDownloadListener listener;

    public DetailsMediaDownloadTask(OnDetailsDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected MediaItem doInBackground(MediaItem... params) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        TvSeason tvSeason = null;
        TvEpisode tvEpisode = null;
        TvSeries tvSeries = null;


        try {
            URL url = null;
            URL youtubeURL = null;
            URL creditsURL = null;
            URL imdbUrl = null;
            switch (params[0].getMediaType()){
                case MEDIA_TYPE_MOVIE:

                    url = new URL("https://api.themoviedb.org/3/movie/"+ params[0].getId() +"?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    youtubeURL = new URL("https://api.themoviedb.org/3/movie/" + params[0].getId() + "/videos?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    creditsURL = new URL("https://api.themoviedb.org/3/movie/" + params[0].getId() + "/credits?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    imdbUrl = new URL("https://api.themoviedb.org/3/movie/"+params[0].getId()+"/external_ids?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_TV:

                    url = new URL("https://api.themoviedb.org/3/tv/"+ params[0].getId() +"?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    youtubeURL = new URL("https://api.themoviedb.org/3/tv/" + params[0].getId() + "/videos?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    creditsURL = new URL("https://api.themoviedb.org/3/tv/" + params[0].getId() + "/credits?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    imdbUrl = new URL("https://api.themoviedb.org/3/tv/"+params[0].getId()+"/external_ids?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_PERSON:

                    url = new URL("https://api.themoviedb.org/3/person/"+ params[0].getId() +"?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_SEASON:

                    tvSeason = (TvSeason) params[0];

                    url = new URL("https://api.themoviedb.org/3/tv/"+ tvSeason.getId() +"/season/" +
                            tvSeason.getSeasonNum() + "?api_key=07c664e1eda6cd9d46f1da1dbaefc959");


                    break;


                case MEDIA_TYPE_EPISODE:

                    tvEpisode = (TvEpisode) params[0];
                    tvSeason = (TvSeason) params[1];
                    tvSeries = (TvSeries) params[2];

                    if (tvEpisode != null && tvSeason != null && tvSeries != null) {

                        url = new URL("https://api.themoviedb.org/3/tv/" + tvSeries.getId() +
                                "/season/" + tvSeason.getSeasonNum() + "/episode/" + tvEpisode.getEpisodeNum() +
                                "?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                        youtubeURL = new URL("https://api.themoviedb.org/3/tv/" + tvSeries.getId() +
                                "/season/" + tvSeason.getSeasonNum() + "/episode/" + tvEpisode.getEpisodeNum()
                                + "/videos?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                        creditsURL = new URL("https://api.themoviedb.org/3/tv/" + tvSeries.getId() +
                                "/season/" + tvSeason.getSeasonNum() + "/episode/" + tvEpisode.getEpisodeNum() +
                                "/credits?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                        imdbUrl = new URL("https://api.themoviedb.org/3/tv/" + tvSeries.getId() + "/season/" +
                                tvSeason.getSeasonNum() + "/episode/" + tvEpisode.getEpisodeNum() + "/external_ids?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    }

                    break;
            }
            connection = (HttpsURLConnection) url.openConnection();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
                Log.e("DetailsTask", "Can't download data");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();

            while (line != null){
                builder.append(line);
                line = reader.readLine();
            }

            JSONObject object = new JSONObject(builder.toString());

            Constant constant = new Constant();
            int runtime = 0;
            String plot = "";
            float rate = 0;
            Date lastDate = null;
            String actors = "";
            String director = "";
            int id = 0;
            String poster_path = "";
            String imdb = "";

            TvSeason[] seasons;
            String[] youtube = null;

            if (params[0].getMediaType() == MEDIA_TYPE_MOVIE || params[0].getMediaType() == MEDIA_TYPE_TV
                    || params[0].getMediaType() == MEDIA_TYPE_EPISODE ) {

                plot = object.getString("overview");
                rate = (float) object.getDouble("vote_average");
                rate = constant.setRate(rate);


                connection = (HttpsURLConnection) youtubeURL.openConnection();

                if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
                    Log.e("YoutubeDownload", "can't download youtube");
                } else {

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    line = reader.readLine();
                    builder = new StringBuilder();

                    while (line != null){
                        builder.append(line);
                        line = reader.readLine();
                    }

                    JSONObject youtubeJsonObject = new JSONObject(builder.toString());
                    JSONArray youtubeArray = youtubeJsonObject.getJSONArray("results");
                    youtube = new String[youtubeArray.length()];
                    for (int i = 0; i < youtubeArray.length(); i++) {
                        JSONObject youtubeObject = youtubeArray.getJSONObject(0);
                        youtube[i] = youtubeObject.getString("key");
                    }

                }

                connection = (HttpsURLConnection) creditsURL.openConnection();

                if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
                    Log.e("CastCrewDownload", "can't download cast and crew");
                } else{

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    line = reader.readLine();
                    builder = new StringBuilder();

                    while (line != null) {
                        builder.append(line);
                        line = reader.readLine();
                    }

                    JSONObject objectCredits = new JSONObject(builder.toString());
                    JSONArray rootCastArray = objectCredits.getJSONArray("cast");
                    JSONArray rootCrewArray = objectCredits.getJSONArray("crew");

                    int cast = 4;

                    if (rootCastArray.length() < 4){
                        cast = rootCastArray.length();
                    }

                    for (int i = 0; i < cast; i++){

                        if (i > 0 && i < cast){
                            actors += ", ";
                        }

                        actors += rootCastArray.getJSONObject(i).getString("name");
                    }

                    if (params[0].getMediaType() == MEDIA_TYPE_MOVIE || params[0].getMediaType() == MEDIA_TYPE_EPISODE) {
                        int counter = 0;

                        for (int i = 0; i < rootCrewArray.length(); i++) {

                            JSONObject crewObject = rootCrewArray.getJSONObject(i);

                            if (crewObject.getString("job").equalsIgnoreCase("Director")) {

                                if (counter > 0 && counter < rootCrewArray.length() - 1) {
                                    director += ", ";
                                }

                                director += crewObject.getString("name");
                                counter++;
                            }
                        }

                    }

                }

            }

            if (imdbUrl != null) {

                connection = (HttpsURLConnection) imdbUrl.openConnection();
                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    line = reader.readLine();
                    builder = new StringBuilder();

                    while (line != null){
                        builder.append(line);
                        line = reader.readLine();
                    }

                    JSONObject imdbJsonObject = new JSONObject(builder.toString());

                    if (!imdbJsonObject.isNull("imdb_id")) {
                        imdb = imdbJsonObject.getString("imdb_id");
                    }


                }
            }

            switch (params[0].getMediaType()){
                case MEDIA_TYPE_MOVIE:

                    if (!object.isNull("runtime"))
                        runtime = object.getInt("runtime");

                    return new Movie(params[0],runtime, plot, actors, director, rate, youtube, imdb);



                case MEDIA_TYPE_TV:

                    if (!object.getJSONArray("episode_run_time").isNull(0))
                        runtime = object.getJSONArray("episode_run_time").getInt(0);


                    if (object.isNull("next_episode_to_air")){
                        lastDate = constant.getDateFromString(object.getJSONObject("last_episode_to_air").getString("air_date"));
                    }

                    JSONArray creatorsArray = object.getJSONArray("created_by");
                    for (int i = 0; i < creatorsArray.length(); i++ ){

                        if (i > 0 && i < creatorsArray.length()){
                            director += ", ";
                        }

                        director += creatorsArray.getJSONObject(i).getString("name");
                    }


                    JSONArray seasonArray = object.getJSONArray("seasons");
                    seasons = new TvSeason[seasonArray.length()];
                    for (int i = 0; i < seasonArray.length(); i++){

                        int seasonNumber = seasonArray.getJSONObject(i).getInt("season_number");
                        URL seasonsUrl = new URL("https://api.themoviedb.org/3/tv/"+ params[0].getId()
                                +"/season/"+ seasonNumber +"?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                        connection = (HttpsURLConnection) seasonsUrl.openConnection();

                        if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK){
                            break;
                        }

                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        line = reader.readLine();
                        builder = new StringBuilder();

                        while (line != null){
                            builder.append(line);
                            line = reader.readLine();
                        }

                        JSONObject seasonObject = new JSONObject(builder.toString());

                        int seasonId = seasonObject.getInt("id");
                        String stringYear = seasonObject.getString("air_date");
                        Date seasonDate = constant.getDateFromString(stringYear);
                        String seasonPoster_path = seasonObject.getString("poster_path");



                        JSONArray jsonArray = seasonObject.getJSONArray("episodes");
                        TvEpisode[] episodes = new TvEpisode[jsonArray.length()];

                        for (int e = 0; e < jsonArray.length(); e++){
                            JSONObject episodeObject  = jsonArray.getJSONObject(e);
                            int episodeNumber = episodeObject.getInt("episode_number");
                            int episodeId = episodeObject.getInt("id");
                            String stringDate = episodeObject.getString("air_date");
                            Date date = constant.getDateFromString(stringDate);

                            String title = episodeObject.getString("name");
                            String overview = episodeObject.getString("overview");
                            String image = episodeObject.getString("still_path");
                            float episodeRate = (float) episodeObject.getDouble("vote_average");

                            episodes[e] = new TvEpisode(episodeId, date,title,image, params[0].getGenresIds(), runtime, overview, null, null, episodeRate,null,null,episodeNumber, params[0].getId(), seasonId ) ;
                        }

                        seasons[i] = new TvSeason(seasonId,seasonPoster_path,seasonDate,seasonNumber, episodes, params[0].getId());


                    }


                    return new TvSeries(params[0], runtime, plot, actors, director, rate, youtube, lastDate,seasons, imdb);

                case MEDIA_TYPE_PERSON:

                    Date birthday = constant.getDateFromString(object.getString("birthday"));
                    Date deathDay = null;
                    if (!object.isNull("deathday")){
                        deathDay = constant.getDateFromString(object.getString("deathday"));
                    }

                    int gender = object.getInt("gender");

                    plot = object.getString("biography");

                    String place_of_birth = object.getString("place_of_birth");
                    place_of_birth = place_of_birth.replace('-', ',');
                    String[] strings = place_of_birth.split(",");
                    place_of_birth = strings[strings.length-1];

                    imdb = object.getString("imdb_id");

                    return new Person(params[0], birthday, deathDay, plot, place_of_birth, gender, imdb);


                case MEDIA_TYPE_EPISODE:

                    id = object.getInt("id");
                    String name = object.getString("name");
                    poster_path = object.getString("still_path");
                    Date date = constant.getDateFromString(object.getString("air_date"));

                    tvSeries = (TvSeries) params[2];


                    return new TvEpisode(id,date,name,poster_path,tvSeries.getGenresIds(),tvSeries.getRuntime(), plot, actors, director, rate, youtube, imdb, tvEpisode.getEpisodeNum(), tvEpisode.getSeriesID(), tvEpisode.getSeasonID());


            }




        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(MediaItem mediaItem) {
        super.onPostExecute(mediaItem);

        listener.getMediaDetails(mediaItem);
    }




}
