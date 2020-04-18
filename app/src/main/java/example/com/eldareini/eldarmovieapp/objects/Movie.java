package example.com.eldareini.eldarmovieapp.objects;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverter;

import java.util.Date;

import static example.com.eldareini.eldarmovieapp.Constant.TABLE_MOVIES;

/**
 * Created by Eldar on 8/18/2017.
 */

//Creating a movie object to work with, and make him Parcelable for Intent.putExtra
public class Movie extends MediaItem  {

    private int runtime;
    private String plot, actors, director;
    private boolean isWatched;
    private float rate;
    private String[] youtube;


    public Movie(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, Date date) {
        super(id, mediaType, name, posterUrl, date);
    }

    public Movie(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds) {
        super(id, mediaType, date, name, posterUrl, genresIds);
    }

    public Movie(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds,
                 int runtime, String plot, String actors, String director, boolean isWatched,
                 float rate, String[] youtube, String imdb) {
        super(id, mediaType, date, name, posterUrl, genresIds, imdb);
        this.runtime = runtime;
        this.plot = plot;
        this.actors = actors;
        this.director = director;
        this.isWatched = isWatched;
        this.rate = rate;
        this.youtube = youtube;
    }

    public Movie(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds,
                 int runtime, String plot, String actors, String director, float rate, String[] youtube, String imdb) {
        super(id, mediaType, date, name, posterUrl, genresIds, imdb);
        this.runtime = runtime;
        this.plot = plot;
        this.actors = actors;
        this.director = director;
        this.rate = rate;
        this.youtube = youtube;
    }

    public Movie(MediaItem mediaItem, int runtime, String plot, String actors, String director, float rate, String[] youtube, String imdb) {
        super(mediaItem.getId(), mediaItem.getMediaType(), mediaItem.getDate(), mediaItem.getName(), mediaItem.getPosterUrl(), mediaItem.getGenresIds(), imdb);
        this.runtime = runtime;
        this.plot = plot;
        this.actors = actors;
        this.director = director;
        this.rate = rate;
        this.youtube = youtube;
    }

    protected Movie(Parcel in) {
        super(in);
        runtime = in.readInt();
        plot = in.readString();
        actors = in.readString();
        director = in.readString();
        isWatched = in.readByte() != 0;
        rate = in.readFloat();
        youtube = in.createStringArray();


    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) throws NegativeArraySizeException {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setYoutube(String[] youtube) {
        this.youtube = youtube;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }


    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public String getDirector() {
        return director;
    }


    public String[] getYoutube() {
        return youtube;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }




    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(runtime);
        dest.writeString(plot);
        dest.writeString(actors);
        dest.writeString(director);
        dest.writeByte((byte)(isWatched? 1 : 0));
        dest.writeFloat(rate);
        dest.writeStringArray(youtube);


    }

    @Override
    public String toString() {
        return getName();
    }
}
