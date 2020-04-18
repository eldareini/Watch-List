package example.com.eldareini.eldarmovieapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import example.com.eldareini.eldarmovieapp.Constant;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.Person;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

import static example.com.eldareini.eldarmovieapp.Constant.CREDIT_CAST_MEMBER;
import static example.com.eldareini.eldarmovieapp.Constant.CREDIT_CREW_MEMBER;
import static example.com.eldareini.eldarmovieapp.Constant.GENDER_FEMALE;
import static example.com.eldareini.eldarmovieapp.Constant.GENDER_MALE;
import static example.com.eldareini.eldarmovieapp.Constant.GENRE_DOCUMENTARY;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_EPISODE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_PERSON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class CreditsDownloadTask extends AsyncTask<MediaItem, Void, ArrayList<CreditItem>> {
    private OnCastAndCrewDownloadListener listener;

    public CreditsDownloadTask(OnCastAndCrewDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<CreditItem> doInBackground(MediaItem... params) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        ArrayList<CreditItem> creditItems = new ArrayList<>();

        try {
            URL url = null;
            switch (params[0].getMediaType()){
                case MEDIA_TYPE_MOVIE:

                    url = new URL("https://api.themoviedb.org/3/movie/" + params[0].getId() + "/credits?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_TV:

                    url = new URL("https://api.themoviedb.org/3/tv/" + params[0].getId() + "/credits?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_PERSON:

                    url = new URL("https://api.themoviedb.org/3/person/"+ params[0].getId() +"/combined_credits?api_key=07c664e1eda6cd9d46f1da1dbaefc959");

                    break;

                case MEDIA_TYPE_EPISODE:

                    TvEpisode episode = (TvEpisode) params[0];
                    TvSeason season = (TvSeason) params[1];
                    TvSeries series = (TvSeries) params[2];

                    if (episode != null && season != null && series != null) {

                        url = new URL("https://api.themoviedb.org/3/tv/" + series.getId() + "/season/" +
                                season.getSeasonNum() + "/episode/" + episode.getEpisodeNum() + "/credits?api_key=07c664e1eda6cd9d46f1da1dbaefc959");
                    }

                    break;
            }

            connection = (HttpsURLConnection) url.openConnection();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK ) {

                Log.e("Download Cast and Crew", "can't download cast and crew. responseCode = " + connection.getResponseCode() );

                return null;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();

            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }

            JSONObject obj = new JSONObject(builder.toString());

                JSONArray rootCastArray = obj.getJSONArray("cast");
                JSONArray rootCrewArray = obj.getJSONArray("crew");

            if (params[0].getMediaType() == MEDIA_TYPE_MOVIE || params[0].getMediaType() == MEDIA_TYPE_TV
                    || params[0].getMediaType() == MEDIA_TYPE_EPISODE ) {

                for (int i = 0; i < rootCastArray.length(); i++) {
                    JSONObject castObject = rootCastArray.getJSONObject(i);
                    int id = castObject.getInt("id");
                    String job = castObject.getString("character");
                    String name = castObject.getString("name");
                    String profile_path = "";
                    if (!castObject.isNull("profile_path"))
                        profile_path = castObject.getString("profile_path");

                    creditItems.add(new CreditItem(id, MEDIA_TYPE_PERSON, name, profile_path, CREDIT_CAST_MEMBER, job));
                }

                if (params[0].getMediaType() == MEDIA_TYPE_EPISODE){
                    JSONArray gustArray = obj.getJSONArray("guest_stars");

                    for (int i = 0; i < gustArray.length(); i++){

                        JSONObject gustObject = gustArray.getJSONObject(i);
                        int id = gustObject.getInt("id");
                        String job = gustObject.getString("character");
                        String name = gustObject.getString("name");
                        String profile_path = "";
                        if (!gustObject.isNull("profile_path"))
                            profile_path = gustObject.getString("profile_path");

                        creditItems.add(new CreditItem(id, MEDIA_TYPE_PERSON, name, profile_path, CREDIT_CAST_MEMBER, job));


                    }
                }

                for (int i = 0; i < rootCrewArray.length(); i++) {
                    JSONObject crewObject = rootCrewArray.getJSONObject(i);
                    int id = crewObject.getInt("id");
                    String name = crewObject.getString("name");
                    String job = crewObject.getString("job");
                    String profile_path = crewObject.getString("profile_path");

                    creditItems.add(new CreditItem(id, MEDIA_TYPE_PERSON, name, profile_path, CREDIT_CREW_MEMBER, job));
                }


                return creditItems;
            } else if (params[0].getMediaType() == MEDIA_TYPE_PERSON){

                Person person = (Person) params[0];

                String job = "";


                if (creditItems.size() == 0) {


                    for (int i = 0; i < rootCastArray.length(); i++) {
                        if (rootCastArray.getJSONObject(i).getJSONArray("genre_ids").getInt(0) != GENRE_DOCUMENTARY) {
                            switch (person.getGender()) {

                                case GENDER_MALE:

                                    job = "Actor";

                                    break;

                                case GENDER_FEMALE:

                                    job = "Actress";

                                    break;
                            }

                            break;

                        }
                    }

                    for (int i = 0; i < rootCrewArray.length(); i++) {

                        if (!job.contains(rootCrewArray.getJSONObject(i).getString("job"))) {

                            if (!job.isEmpty())
                                job += " | ";

                            job += rootCrewArray.getJSONObject(i).getString("job");
                        }
                    }

                    creditItems.add(new CreditItem(person.getId(), person.getMediaType(), person.getName(), person.getPosterUrl(), person.getDate(), job, CREDIT_CAST_MEMBER, new int[0]));
                }




                    creditItems.addAll(getPersonCredits(rootCastArray, CREDIT_CAST_MEMBER));
                    creditItems.addAll(getPersonCredits(rootCrewArray, CREDIT_CREW_MEMBER));

                    return creditItems;


            }



        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.e("DownloadCredit", e.getMessage());
        }
        return null;
    }

    private ArrayList<CreditItem> getPersonCredits(JSONArray jsonArray, int creditMember) {

        ArrayList<CreditItem> items = new ArrayList<>();

        try {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Constant constant = new Constant();

            JSONArray array = jsonObject.getJSONArray("genre_ids");
            int[] genres = new int[array.length()];
            for (int h = 0; h < array.length(); h++){
                genres[h] = array.getInt(h);
            }


            int mediaType = constant.getMediaType(jsonObject.getString("media_type"));

            int id = jsonObject.getInt("id");

            String posterURL = "";

            if (!jsonObject.isNull("poster_path")){

                posterURL = jsonObject.getString("poster_path");
            }

            String character = "";

            switch (creditMember){

                case CREDIT_CAST_MEMBER:

                    character = jsonObject.getString("character");

                    break;

                case CREDIT_CREW_MEMBER:

                    character = jsonObject.getString("job");

                    break;
            }


            String title = "";
            Date date = null;



            switch (mediaType) {
                case MEDIA_TYPE_MOVIE:

                    title = jsonObject.getString("title");
                    if (!jsonObject.isNull("release_date"))
                        date = constant.getDateFromString(jsonObject.getString("release_date"));

                    items.add(new CreditItem(id, mediaType, title, posterURL, date, character, creditMember, genres));

                    break;

                case MEDIA_TYPE_TV:

                    title = jsonObject.getString("name");
                    date = constant.getDateFromString(jsonObject.getString("first_air_date"));

                    int episodes = jsonObject.getInt("episode_count");


                    items.add(new CreditItem(id, mediaType, title, posterURL, date, episodes, character, creditMember, genres));


                    break;
            }

        }

        return items;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<CreditItem> creditItems) {
        super.onPostExecute(creditItems);

        if (creditItems != null){
            listener.getData(creditItems);
        }


    }



    public interface OnCastAndCrewDownloadListener{
        void getData(ArrayList<CreditItem> creditItems);
    }
}
