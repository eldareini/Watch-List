package example.com.eldareini.eldarmovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.Movie;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

import static example.com.eldareini.eldarmovieapp.Constant.CON_ACTORS;
import static example.com.eldareini.eldarmovieapp.Constant.CON_DATE;
import static example.com.eldareini.eldarmovieapp.Constant.CON_DIRECTOR;
import static example.com.eldareini.eldarmovieapp.Constant.CON_EPISODES;
import static example.com.eldareini.eldarmovieapp.Constant.CON_EPISODE_NUMBER;
import static example.com.eldareini.eldarmovieapp.Constant.CON_GENRES;
import static example.com.eldareini.eldarmovieapp.Constant.CON_ID;
import static example.com.eldareini.eldarmovieapp.Constant.CON_IMDB;
import static example.com.eldareini.eldarmovieapp.Constant.CON_LAST_AIR_DATE;
import static example.com.eldareini.eldarmovieapp.Constant.CON_IS_WATCHED;
import static example.com.eldareini.eldarmovieapp.Constant.CON_MEDIA_TYPE;
import static example.com.eldareini.eldarmovieapp.Constant.CON_NAME;
import static example.com.eldareini.eldarmovieapp.Constant.CON_PLOT;
import static example.com.eldareini.eldarmovieapp.Constant.CON_POSTER_URL;
import static example.com.eldareini.eldarmovieapp.Constant.CON_RATE;
import static example.com.eldareini.eldarmovieapp.Constant.CON_RUNTIME;
import static example.com.eldareini.eldarmovieapp.Constant.CON_SEASONS;
import static example.com.eldareini.eldarmovieapp.Constant.CON_SEASON_ID;
import static example.com.eldareini.eldarmovieapp.Constant.CON_SEASON_NUMBER;
import static example.com.eldareini.eldarmovieapp.Constant.CON_SERIES_ID;
import static example.com.eldareini.eldarmovieapp.Constant.CON_YOUTUBE;
import static example.com.eldareini.eldarmovieapp.Constant.TABLE_EPISODES;
import static example.com.eldareini.eldarmovieapp.Constant.TABLE_MOVIES;
import static example.com.eldareini.eldarmovieapp.Constant.TABLE_SEASONS;
import static example.com.eldareini.eldarmovieapp.Constant.TABLE_TV_SERIES;

/**
 * Created by Eldar on 8/18/2017.
 */

//Creating a database for saving all the movies details
public class MediaDBHelper extends SQLiteOpenHelper {

    private Constant constant = new Constant();

    public MediaDBHelper(Context context) {
        super(context, "myMovieDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format( " CREATE TABLE %s ( %s INTEGER , %s INTEGER , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s INTEGER , %s TEXT ," +
                " %s TEXT , %s TEXT , %s INTEGER, %s REAL, %s TEXT )",
                TABLE_MOVIES, CON_ID, CON_MEDIA_TYPE, CON_NAME, CON_POSTER_URL, CON_IMDB, CON_GENRES, CON_DATE, CON_RUNTIME, CON_PLOT, CON_ACTORS, CON_DIRECTOR, CON_IS_WATCHED, CON_RATE, CON_YOUTUBE));

        db.execSQL(String.format( " CREATE TABLE %s ( %s INTEGER , %s INTEGER , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s INTEGER , %s TEXT ," +
                        " %s TEXT , %s TEXT , %s INTEGER, %s REAL, %s TEXT , %s TEXT )",
                TABLE_TV_SERIES, CON_ID, CON_MEDIA_TYPE, CON_NAME, CON_POSTER_URL, CON_IMDB, CON_GENRES, CON_DATE, CON_RUNTIME, CON_PLOT, CON_ACTORS, CON_DIRECTOR, CON_IS_WATCHED, CON_RATE, CON_YOUTUBE, CON_LAST_AIR_DATE));

        db.execSQL(" CREATE TABLE " + TABLE_SEASONS + " ( " + CON_ID + " INTEGER , " + CON_POSTER_URL + " TEXT , " + CON_DATE + " TEXT , " + CON_SEASON_NUMBER + " INTEGER , " + CON_SERIES_ID + " INTEGER )");

        db.execSQL(String.format( " CREATE TABLE %s ( %s INTEGER , %s INTEGER , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s INTEGER , %s TEXT ," +
                        " %s TEXT , %s TEXT , %s INTEGER, %s REAL , %s TEXT , %s INTEGER , %s INTEGER , %s INTEGER )",
                TABLE_EPISODES, CON_ID, CON_MEDIA_TYPE, CON_NAME, CON_POSTER_URL, CON_IMDB, CON_GENRES, CON_DATE, CON_RUNTIME, CON_PLOT, CON_ACTORS, CON_DIRECTOR, CON_IS_WATCHED, CON_RATE, CON_YOUTUBE, CON_EPISODE_NUMBER, CON_SERIES_ID, CON_SEASON_ID));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //***************************************

    //a metod to insert a movie to the database
    public void insertMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CON_ID, movie.getId());
        values.put(CON_MEDIA_TYPE, movie.getMediaType());
        values.put(CON_NAME, movie.getName());
        values.put(CON_PLOT, movie.getPlot());
        values.put(CON_DATE, constant.getStringFromDate(movie.getDate()));
        values.put(CON_POSTER_URL, movie.getPosterUrl());
        values.put(CON_IS_WATCHED, movie.isWatched());
        values.put(CON_RATE, movie.getRate());
        values.put(CON_ACTORS, movie.getActors());
        values.put(CON_DIRECTOR, movie.getDirector());
        values.put(CON_YOUTUBE, convertStringArrayToString(movie.getYoutube()));
        values.put(CON_RUNTIME, movie.getRuntime());
        values.put(CON_GENRES, convertIntArrayToString(movie.getGenresIds()) );

        db.insert(TABLE_MOVIES, null, values);
        db.close();
    }

    //get All the movies details from dataBase
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIES, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            movies.add(getMovie(cursor.getInt(cursor.getColumnIndex(CON_ID))));

        }

        db.close();

        return movies;
    }


    public Movie getMovie(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIES, null, CON_ID + "=" + id, null, null, null, null);
        if(cursor.moveToFirst()) {

            int mediaType = cursor.getInt(cursor.getColumnIndex(CON_MEDIA_TYPE));
            String title = cursor.getString(cursor.getColumnIndex(CON_NAME));
            String plot = cursor.getString(cursor.getColumnIndex(CON_PLOT));
            String posterUrl = cursor.getString(cursor.getColumnIndex(CON_POSTER_URL));
            String imdb = cursor.getString(cursor.getColumnIndex(CON_IMDB));
            Date date = constant.getDateFromString(cursor.getString(cursor.getColumnIndex(CON_DATE)));
            boolean isWatched;
            if (cursor.getInt(cursor.getColumnIndex(CON_IS_WATCHED)) == 1) {
                isWatched = true;
            } else {
                isWatched = false;
            }
            float rate = cursor.getFloat(cursor.getColumnIndex(CON_RATE));
            String actors = cursor.getString(cursor.getColumnIndex(CON_ACTORS));
            String director = cursor.getString(cursor.getColumnIndex(CON_DIRECTOR));
            int[] genre = convertStringToIntArray(cursor.getString(cursor.getColumnIndex(CON_GENRES)));
            String[] youtube = convertStringToStringArray(cursor.getString(cursor.getColumnIndex(CON_YOUTUBE)));
            int runtime = cursor.getInt(cursor.getColumnIndex(CON_RUNTIME));

            db.close();

            return new Movie(id, mediaType, date, title, posterUrl, genre, runtime, plot, actors, director, isWatched, rate, youtube, imdb);
        }

        db.close();

        return null;
    }
    //deleting all Movies from dataBase
    public void deleteAllMovies() {
        SQLiteDatabase  db = getWritableDatabase();
        db.delete(TABLE_MOVIES, null, null);
        db.close();
    }

    //deleting a Movie from dataBase
    public void deleteMovie(int id) {
        SQLiteDatabase  db = getWritableDatabase();
        db.delete(TABLE_MOVIES, CON_ID + "=" + id, null);
        db.close();
    }

    //update a movie details
    public void updateMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CON_ID, movie.getId());
        values.put(CON_MEDIA_TYPE, movie.getMediaType());
        values.put(CON_NAME, movie.getName());
        values.put(CON_PLOT, movie.getPlot());
        values.put(CON_DATE, constant.getStringFromDate(movie.getDate()));
        values.put(CON_POSTER_URL, movie.getPosterUrl());
        values.put(CON_IS_WATCHED, movie.isWatched());
        values.put(CON_RATE, movie.getRate());
        values.put(CON_ACTORS, movie.getActors());
        values.put(CON_DIRECTOR, movie.getDirector());
        values.put(CON_YOUTUBE, convertStringArrayToString(movie.getYoutube()));
        values.put(CON_RUNTIME, movie.getRuntime());
        values.put(CON_GENRES, convertIntArrayToString(movie.getGenresIds()) );

        db.update(TABLE_MOVIES, values, CON_ID + "=" + movie.getId(), null);
        db.close();
    }


    public void insertTvSeries(TvSeries series) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CON_ID, series.getId());
        values.put(CON_MEDIA_TYPE, series.getMediaType());
        values.put(CON_NAME, series.getName());
        values.put(CON_PLOT, series.getPlot());
        values.put(CON_DATE, constant.getStringFromDate(series.getDate()));
        values.put(CON_POSTER_URL, series.getPosterUrl());
        values.put(CON_IS_WATCHED, series.isWatched());
        values.put(CON_RATE, series.getRate());
        values.put(CON_ACTORS, series.getActors());
        values.put(CON_DIRECTOR, series.getDirector());
        values.put(CON_YOUTUBE, convertStringArrayToString(series.getYoutube()));
        values.put(CON_RUNTIME, series.getRuntime());
        values.put(CON_GENRES, convertIntArrayToString(series.getGenresIds()) );
        values.put(CON_LAST_AIR_DATE, constant.getStringFromDate(series.getLastAirDate()));


        db.insert(TABLE_TV_SERIES, null, values);
        db.close();

    }

    public ArrayList<TvSeries> getAllTvSeries(){
        SQLiteDatabase  db = getReadableDatabase();
        ArrayList<TvSeries> series = new ArrayList<>();

        Cursor cursor = db.query(TABLE_TV_SERIES, null, null, null, null, null, null);
        while (cursor.moveToNext()){

            series.add(getTvSeries(cursor.getInt(cursor.getColumnIndex(CON_ID))));

        }

        db.close();
        return series;
    }

    public TvSeries getTvSeries(int id){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_TV_SERIES, null, CON_ID + "=" + id, null, null, null, null);
        if (cursor.moveToFirst()) {
            int mediaType = cursor.getInt(cursor.getColumnIndex(CON_MEDIA_TYPE));
            String title = cursor.getString(cursor.getColumnIndex(CON_NAME));
            String plot = cursor.getString(cursor.getColumnIndex(CON_PLOT));
            String posterUrl = cursor.getString(cursor.getColumnIndex(CON_POSTER_URL));
            String imdb = cursor.getString(cursor.getColumnIndex(CON_IMDB));
            Date date = constant.getDateFromString(cursor.getString(cursor.getColumnIndex(CON_DATE)));
            boolean isWatched;
            if (cursor.getInt(cursor.getColumnIndex(CON_IS_WATCHED)) == 1) {
                isWatched = true;
            } else {
                isWatched = false;
            }
            float rate = cursor.getFloat(cursor.getColumnIndex(CON_RATE));
            String actors = cursor.getString(cursor.getColumnIndex(CON_ACTORS));
            String director = cursor.getString(cursor.getColumnIndex(CON_DIRECTOR));
            int[] genre = convertStringToIntArray(cursor.getString(cursor.getColumnIndex(CON_GENRES)));
            String[] youtube = convertStringToStringArray(cursor.getString(cursor.getColumnIndex(CON_YOUTUBE)));
            int runtime = cursor.getInt(cursor.getColumnIndex(CON_RUNTIME));
            Date lastAirDate = constant.getDateFromString(cursor.getString(cursor.getColumnIndex(CON_LAST_AIR_DATE)));

            db.close();

            return new TvSeries(id, mediaType, date, title, posterUrl, genre, runtime, plot, actors, director, isWatched, rate, youtube, lastAirDate, imdb);
        }

        db.close();

        return null;
    }

    public void updateTvSeries(TvSeries series){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CON_ID, series.getId());
        values.put(CON_MEDIA_TYPE, series.getMediaType());
        values.put(CON_NAME, series.getName());
        values.put(CON_PLOT, series.getPlot());
        values.put(CON_DATE, constant.getStringFromDate(series.getDate()));
        values.put(CON_POSTER_URL, series.getPosterUrl());
        values.put(CON_IS_WATCHED, series.isWatched());
        values.put(CON_RATE, series.getRate());
        values.put(CON_ACTORS, series.getActors());
        values.put(CON_DIRECTOR, series.getDirector());
        values.put(CON_YOUTUBE, convertStringArrayToString(series.getYoutube()));
        values.put(CON_RUNTIME, series.getRuntime());
        values.put(CON_GENRES, convertIntArrayToString(series.getGenresIds()) );
        values.put(CON_LAST_AIR_DATE, constant.getStringFromDate(series.getLastAirDate()));



        db.update(TABLE_TV_SERIES, values, CON_ID + "-" + series.getId(), null);
        db.close();



    }

    public void deleteTvSeries(TvSeries series){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TV_SERIES, CON_ID + "=" + series.getId(), null);
        db.delete(TABLE_SEASONS, CON_SERIES_ID + "=" + series.getId(), null);
        db.delete(TABLE_EPISODES, CON_SERIES_ID + "=" + series.getId(), null);
        db.close();

    }

    public void deleteAllSeries(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TV_SERIES, null, null);
        db.delete(TABLE_SEASONS, null, null);
        db.delete(TABLE_EPISODES, null, null);
        db.close();

    }


    public void insertEpisode(TvEpisode episode) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_EPISODES, null, null, null, null, null, null);

        ContentValues values = new ContentValues();

        values.put(CON_ID, episode.getId());
        values.put(CON_MEDIA_TYPE, episode.getMediaType());
        values.put(CON_NAME, episode.getName());
        values.put(CON_PLOT, episode.getPlot());
        values.put(CON_DATE, constant.getStringFromDate(episode.getDate()));
        values.put(CON_POSTER_URL, episode.getPosterUrl());
        values.put(CON_IS_WATCHED, episode.isWatched());
        values.put(CON_RATE, episode.getRate());
        values.put(CON_ACTORS, episode.getActors());
        values.put(CON_DIRECTOR, episode.getDirector());
        values.put(CON_YOUTUBE, convertStringArrayToString(episode.getYoutube()));
        values.put(CON_RUNTIME, episode.getRuntime());
        values.put(CON_GENRES, convertIntArrayToString(episode.getGenresIds()) );
        values.put(CON_EPISODE_NUMBER, episode.getEpisodeNum());
        values.put(CON_SERIES_ID, episode.getSeriesID());
        values.put(CON_SEASON_ID, episode.getSeasonID());

        db.insert(TABLE_EPISODES, null, values);
        db.close();
    }

    public TvEpisode getEpisode(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_EPISODES, null, CON_ID + "=" + id, null, null, null, null);
        if (cursor.moveToFirst()) {

            String title = cursor.getString(cursor.getColumnIndex(CON_NAME));
            String plot = cursor.getString(cursor.getColumnIndex(CON_PLOT));
            String posterUrl = cursor.getString(cursor.getColumnIndex(CON_POSTER_URL));
            String imdb = cursor.getString(cursor.getColumnIndex(CON_IMDB));
            Date date = constant.getDateFromString(cursor.getString(cursor.getColumnIndex(CON_DATE)));
            boolean isWatched;
            if (cursor.getInt(cursor.getColumnIndex(CON_IS_WATCHED)) == 1) {
                isWatched = true;
            } else {
                isWatched = false;
            }
            float rate = cursor.getFloat(cursor.getColumnIndex(CON_RATE));
            String actors = cursor.getString(cursor.getColumnIndex(CON_ACTORS));
            String director = cursor.getString(cursor.getColumnIndex(CON_DIRECTOR));
            int[] genre = convertStringToIntArray(cursor.getString(cursor.getColumnIndex(CON_GENRES)));
            String[] youtube = convertStringToStringArray(cursor.getString(cursor.getColumnIndex(CON_YOUTUBE)));
            int runtime = cursor.getInt(cursor.getColumnIndex(CON_RUNTIME));
            int episodeNum = cursor.getInt(cursor.getColumnIndex(CON_EPISODE_NUMBER));
            int seriesID = cursor.getInt(cursor.getColumnIndex(CON_SERIES_ID));
            int seasonID = cursor.getInt(cursor.getColumnIndex(CON_SEASON_ID));

            db.close();

            return new TvEpisode(id, date, title, posterUrl, genre, runtime, plot, actors, director, isWatched, rate, youtube, imdb, episodeNum, seriesID, seasonID);
        }

        db.close();

        return null;
    }

    public void updateEpisode(TvEpisode episode){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CON_ID, episode.getId());
        values.put(CON_MEDIA_TYPE, episode.getMediaType());
        values.put(CON_NAME, episode.getName());
        values.put(CON_PLOT, episode.getPlot());
        values.put(CON_DATE, constant.getStringFromDate(episode.getDate()));
        values.put(CON_POSTER_URL, episode.getPosterUrl());
        values.put(CON_IS_WATCHED, episode.isWatched());
        values.put(CON_RATE, episode.getRate());
        values.put(CON_ACTORS, episode.getActors());
        values.put(CON_DIRECTOR, episode.getDirector());
        values.put(CON_YOUTUBE, convertStringArrayToString(episode.getYoutube()));
        values.put(CON_RUNTIME, episode.getRuntime());
        values.put(CON_GENRES, convertIntArrayToString(episode.getGenresIds()) );
        values.put(CON_EPISODE_NUMBER, episode.getEpisodeNum());
        values.put(CON_SERIES_ID, episode.getSeriesID());
        values.put(CON_SEASON_ID, episode.getSeasonID());

        db.update(TABLE_EPISODES, values, CON_ID + "-" + episode.getId(), null);
        db.close();

    }

    public void deleteAllEpisodes(TvEpisode[] episodes){
        SQLiteDatabase db = getWritableDatabase();

        for (int i = 0; i < episodes.length; i++){
            db.delete(TABLE_EPISODES, CON_ID + "=" + episodes[i].getId(), null);
        }
        db.close();
    }

    public ArrayList<TvEpisode> getSeasonEpisodes(int seriesID, int seasonID){
        ArrayList<TvEpisode> episodes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_EPISODES, null,
                CON_SERIES_ID + "=" + seriesID + " AND " + CON_SEASON_ID + "=" + seasonID,
                null, null, null, null);

        while (cursor.moveToNext()){

            int id = cursor.getInt(cursor.getColumnIndex(CON_ID));
            String title = cursor.getString(cursor.getColumnIndex(CON_NAME));
            String plot = cursor.getString(cursor.getColumnIndex(CON_PLOT));
            String posterUrl = cursor.getString(cursor.getColumnIndex(CON_POSTER_URL));
            String imdb = cursor.getString(cursor.getColumnIndex(CON_IMDB));
            Date date = constant.getDateFromString(cursor.getString(cursor.getColumnIndex(CON_DATE)));
            boolean isWatched;
            if (cursor.getInt(cursor.getColumnIndex(CON_IS_WATCHED)) == 1) {
                isWatched = true;
            } else {
                isWatched = false;
            }
            float rate = cursor.getFloat(cursor.getColumnIndex(CON_RATE));
            String actors = cursor.getString(cursor.getColumnIndex(CON_ACTORS));
            String director = cursor.getString(cursor.getColumnIndex(CON_DIRECTOR));
            int[] genre = convertStringToIntArray(cursor.getString(cursor.getColumnIndex(CON_GENRES)));
            String[] youtube = convertStringToStringArray(cursor.getString(cursor.getColumnIndex(CON_YOUTUBE)));
            int runtime = cursor.getInt(cursor.getColumnIndex(CON_RUNTIME));
            int episodeNum = cursor.getInt(cursor.getColumnIndex(CON_EPISODE_NUMBER));

            episodes.add(new TvEpisode(id, date, title, posterUrl, genre, runtime, plot, actors, director, isWatched, rate, youtube, imdb, episodeNum, seriesID, seasonID));

        }

        db.close();

        return episodes;

    }

    public void insertSeason(TvSeason season) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CON_ID, season.getId());
        values.put(CON_DATE, constant.getStringFromDate(season.getDate()));
        values.put(CON_POSTER_URL, season.getPosterUrl());
        values.put(CON_SEASON_NUMBER, season.getSeasonNum());
        values.put(CON_SERIES_ID, season.getSeriesID());

        db.insert(TABLE_SEASONS, null, values);
        db.close();

    }

    public TvSeason getSeason(int id){

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_SEASONS, null, CON_ID + "=" + id, null, null, null, null);
        if(cursor.moveToFirst()) {
            Date date = constant.getDateFromString(cursor.getString(cursor.getColumnIndex(CON_DATE)));
            String posterUrl = cursor.getString(cursor.getColumnIndex(CON_POSTER_URL));
            int seasonNumber = cursor.getInt(cursor.getColumnIndex(CON_SEASON_NUMBER));
            int seriesID = cursor.getInt(cursor.getColumnIndex(CON_SERIES_ID));

            return new TvSeason(id, posterUrl, date, seasonNumber, seriesID);
        }

        db.close();

        return null;


    }

    public void deleteSeriesSeasons(TvSeason[] seasons){
        SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i < seasons.length; i++){
            db.delete(TABLE_SEASONS, CON_ID + "-" + seasons[i].getId(), null);
            deleteAllEpisodes(seasons[i].getEpisodes());
        }
        db.close();
    }

    public void updateSeason(TvSeason season){

        SQLiteDatabase  db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CON_ID, season.getId());
        values.put(CON_DATE, constant.getStringFromDate(season.getDate()));
        values.put(CON_POSTER_URL, season.getPosterUrl());
        values.put(CON_SEASON_NUMBER, season.getSeasonNum());
        values.put(CON_SERIES_ID, season.getSeriesID());

        db.update(TABLE_SEASONS, values, CON_ID + "=" + season.getId(), null);
        db.close();



    }

    public ArrayList<TvSeason> getSeriesSeasons(int seriesId){
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<TvSeason> seasons = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SEASONS, null,
                CON_SERIES_ID + "=" + seriesId, null, null, null,
                null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(CON_ID));
            Date date = constant.getDateFromString(cursor.getString(cursor.getColumnIndex(CON_DATE)));
            String posterUrl = cursor.getString(cursor.getColumnIndex(CON_POSTER_URL));
            int seasonNumber = cursor.getInt(cursor.getColumnIndex(CON_SEASON_NUMBER));
            int seriesID = cursor.getInt(cursor.getColumnIndex(CON_SERIES_ID));

            seasons.add(new TvSeason(id, posterUrl, date, seasonNumber, seriesID ));

        }

        db.close();

        return seasons;
    }





    public static String convertStringArrayToString(String[] stringList) {
        if (stringList != null) {

            String string = "";

            for (int i = 0; i < stringList.length; i++){
                if (i > 0){
                    string += ",";
                }

                string += stringList[i];
            }

            return string;
        }

        return null;
    }

    public static String[] convertStringToStringArray(String str) {
        if (str != null ) {

            return str.split(",");

        } else
            return null;
    }

    public static String convertIntArrayToString(int[] ints){
        String string = "";
        for (int i = 0; i < ints.length; i++){
            if (i > 0)
                string += ",";

            string += ints[i] + "";
        }

        return string;
    }

    public static int[] convertStringToIntArray(String string){
        String[] strings = string.split(",");
        int[] ints = new int[strings.length];
        for (int i = 0; i < strings.length; i++){
            ints[i] = Integer.parseInt(strings[i]);
        }

        return ints;
    }


}