package example.com.eldareini.eldarmovieapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import example.com.eldareini.eldarmovieapp.objects.GenreItem;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.Movie;
import example.com.eldareini.eldarmovieapp.objects.Person;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

public class Constant {

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_CAST_OR_CREW = "Extra_cast_or_crew";
    public static final String EXTRA_SIMILAR = "Similar";
    public static final String ACTION_SEARCH = "example.com.eldareini.eldarmovieapp.SEARCH_ACTION";
    public static final String EXTRA_SEARCH = "search";
    public static final String TAG_MOVIE_LIST_FRAGMENT = "movie_list";
    public static final String TAG_SEARCH_FRAGMENT = "movie_search";
    public static final int TRIGGER_AUTO_COMPLETE = 100;
    public static final long AUTO_COMPLETE_DELAY = 300;
    public static final String GO_SIMILAR = "similar";
    public static final String GO_SEARCH = "search";
    public static final String EXTRA_TITLE = "title";
    public static final String GO_CAST_OR_CREW = "castOrCrew";
    public static final String API_GET_IMAGE = "https://image.tmdb.org/t/p/w500/";

    public static final String ACTION_RELOAD = "example.com.eldareini.eldarmovieapp.ACTION_RELOAD";

    public static final String EXTRA_MEDIA = "media";
    public static final String EXTRA_CAST_CREW = "castCrew";
    public static final String EXTRA_UPDATE = "isUpdate";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_SEASON = "season";
    public static final String EXTRA_EPISODE = "episode";

    public static final String CON_ID = "id";
    public static final String CON_MEDIA_TYPE = "media_type";
    public static final String CON_NAME = "name";
    public static final String CON_POSTER_URL = "poster_url";
    public static final String CON_IMDB = "imdb";
    public static final String CON_GENRES = "genres";
    public static final String CON_DATE = "date";
    public static final String CON_RUNTIME = "runtime";
    public static final String CON_PLOT = "plot";
    public static final String CON_ACTORS = "actors";
    public static final String CON_DIRECTOR = "director";
    public static final String CON_RATE = "rate";
    public static final String CON_YOUTUBE = "youtube";
    public static final String CON_IS_WATCHED = "watched";
    public static final String CON_LAST_AIR_DATE = "last_air_date";
    public static final String CON_SEASONS = "seasons";
    public static final String CON_SEASON_NUMBER = "season_number";
    public static final String CON_EPISODES = "episodes";
    public static final String CON_EPISODE_NUMBER = "episode_number";
    public static final String CON_SERIES_ID = "series_id";
    public static final String CON_SEASON_ID = "season_id";

    public static final String TABLE_MOVIES = "table_movies";
    public static final String TABLE_TV_SERIES = "table_tv_series";
    public static final String TABLE_SEASONS = "table_seasons";
    public static final String TABLE_EPISODES = "table_episodes";



    public static final int MEDIA_TYPE_MOVIE = 0;
    public static final int MEDIA_TYPE_TV = 1;
    public static final int MEDIA_TYPE_PERSON = 2;
    public static final int MEDIA_TYPE_SEASON = 3;
    public static final int MEDIA_TYPE_EPISODE = 4;
    public static final int MEDIA_TYPE_UNKNOWN = -1;
    public static final int MEDIA_TYPE_CONTINUE = 55;

    public static final int MEDIA_TYPE_MOVIE_POPULAR = 11;
    public static final int MEDIA_TYPE_MOVIE_UPCOMING = 12;
    public static final int MEDIA_TYPE_MOVIE_NOW = 13;
    public static final int MEDIA_TYPE_TV_POPULAR = 21;
    public static final int MEDIA_TYPE_TV_ON_AIR = 22;



    public static final int CREDIT_CAST_MEMBER = 0;
    public static final int CREDIT_CREW_MEMBER = 1;

    public static final int GENRE_DOCUMENTARY = 99;

    public static final int GENDER_MALE = 2;
    public static final int GENDER_FEMALE = 1;

    public static final SimpleDateFormat SIMPLE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
    public static final SimpleDateFormat SIMPLE_YEAR_FORMAT = new SimpleDateFormat("yyyy");

    public static final GenreItem[] GENRES = new GenreItem[]{new GenreItem(10759, "Action & Adventure"),
    new GenreItem(16, "Animation"), new GenreItem(35, "Comedy"), new GenreItem(80, "Crime"),
            new GenreItem(99, "Documentary"), new GenreItem(18, "Drama"), new GenreItem(10751, "Family"),
            new GenreItem(10762, "Kids"), new GenreItem(9648, "Mystery"), new GenreItem(10763, "News"),
            new GenreItem(10764, "Reality"), new GenreItem(10765, "Sci-Fi & Fantasy"), new GenreItem(10766, "Soap"),
            new GenreItem(10767, "Talk"), new GenreItem(10768, "War & Politics"), new GenreItem(37, "Western"),
            new GenreItem(28, "Action"), new GenreItem(12, "Adventure"), new GenreItem(14, "Fantasy"),
            new GenreItem(36, "History"), new GenreItem(27, "Horror"), new GenreItem(10749, "Romance"),
            new GenreItem(878, "Science Fiction"), new GenreItem(10770, "TV Movie"), new GenreItem(53, "Thriller"),
            new GenreItem(10752, "War")};


    public String getTheGenre(int[] genresIDs){

        String genre = "";
        for (int i = 0; i < genresIDs.length; i++){

            for (int j = 0; j < GENRES.length; j++){
                if (genresIDs[i] == GENRES[j].getId()){

                    if (!genre.isEmpty())
                        genre += " | ";

                    genre += GENRES[j].getGenre();

                }
            }
        }

        return genre;

    }



    public ArrayList<MediaItem> getJSONMedia(JSONArray rootArray) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (int i = 0; i < rootArray.length(); i++) {
            JSONObject commentObj = null;
            try {
                commentObj = rootArray.getJSONObject(i);

                int id = commentObj.getInt("id");


                int mediaType = getMediaType(commentObj.getString("media_type"));

                String title = "";
                Date date = null;
                String posterUrl = "";
                String stringYear = "";
                int[] genresIds;

                switch (mediaType){
                    case MEDIA_TYPE_MOVIE:

                        title = commentObj.getString("title");
                        stringYear = commentObj.getString("release_date");
                        date = getDateFromString(stringYear);

                        posterUrl = commentObj.getString("poster_path");


                        genresIds = putGenresIds(commentObj.getJSONArray("genre_ids"));


                        mediaItems.add(new Movie(id, mediaType, date, title, posterUrl, genresIds));

                        break;

                    case MEDIA_TYPE_TV:

                        title = commentObj.getString("name");

                        stringYear = commentObj.getString("first_air_date");

                        date = getDateFromString(stringYear);

                        posterUrl = commentObj.getString("poster_path");

                        genresIds = putGenresIds(commentObj.getJSONArray("genre_ids"));

                        mediaItems.add(new TvSeries(id, mediaType, date, title, posterUrl, genresIds ));

                        break;

                    case MEDIA_TYPE_PERSON:

                        title = commentObj.getString("name");
                        posterUrl = commentObj.getString("profile_path");

                        mediaItems.add(new Person(id, mediaType, title, posterUrl));

                        break;
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return mediaItems;
    }

    private int[] putGenresIds(JSONArray jsonArray){

        int[] genresIds = new int[jsonArray.length()];
        try {
        for (int j = 0; j < jsonArray.length(); j++){

                genresIds[j] = jsonArray.getInt(j);

        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return genresIds;

    }

    public int getMediaType(String media_type) {
        if (media_type.equals("movie")){
            return MEDIA_TYPE_MOVIE;
        } else if (media_type.equals("tv")){
            return MEDIA_TYPE_TV;
        } else if (media_type.equals("person")){
            return MEDIA_TYPE_PERSON;
        }

        return MEDIA_TYPE_UNKNOWN;
    }

    public Date getDateFromString(String stringDate) {

        if (stringDate != null) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return date;
        }

        return null;
    }

    public String getStringFromDate(Date date){

        if (date != null) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        }

        return null;

    }

    public float setRate(float rate) {

        if (rate > 9.2f && rate <= 10f) {
            rate = 5f;
        } else if (rate > 8.2f && rate <= 9.2f) {
            rate = 4.5f;
        } else if (rate > 7.2f && rate <= 8.2f) {
            rate = 4f;
        } else if (rate > 6.2f && rate <= 7.2f) {
            rate = 3.5f;
        } else if (rate > 5.2f && rate <= 6.2f) {
            rate = 3f;
        } else if (rate > 4.2f && rate <= 5.2f) {
            rate = 2.5f;
        } else if (rate > 3.2f && rate <= 4.2f) {
            rate = 2f;
        } else if (rate > 2.2f && rate <= 3.2f) {
            rate = 1.5f;
        } else if (rate > 1.2f && rate <= 2.2f) {
            rate = 1f;
        } else if (rate > 0.2f && rate <= 1.2f) {
            rate = 0.5f;
        } else {
            rate = 0;
        }

        return rate;
    }



}
