package example.com.eldareini.eldarmovieapp.objects;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.Date;

import static example.com.eldareini.eldarmovieapp.Constant.SIMPLE_YEAR_FORMAT;

public class TvSeries extends Movie {

    private Date lastAirDate;
    private TvSeason[] seasons;


    public TvSeries(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, Date date) {
        super(id, mediaType, name, posterUrl, date);
    }

    public TvSeries(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds) {
        super(id, mediaType, date, name, posterUrl, genresIds);
    }

    public TvSeries(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds,
                    int runtime, String plot, String actors, String director, boolean isWatched,
                    float rate, String[] youtube, Date lastAirDate, TvSeason[] seasons, String imdb) {
        super(id, mediaType, date, name, posterUrl, genresIds, runtime, plot, actors, director, isWatched, rate, youtube, imdb);
        this.lastAirDate = lastAirDate;
        this.seasons = seasons;
    }

    public TvSeries(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds,
                    int runtime, String plot, String actors, String director, float rate,
                    String[] youtube, Date lastAirDate, TvSeason[] seasons, String imdb) {
        super(id, mediaType, date, name, posterUrl, genresIds, runtime, plot, actors, director, rate, youtube, imdb);
        this.lastAirDate = lastAirDate;
        this.seasons = seasons;
    }

    public TvSeries(int id, int mediaType, Date date, String title, String posterUrl,
                    int[] genre, int runtime, String plot, String actors, String director,
                    boolean isWatched, float rate, String[] youtube, Date lastAirDate, String imdb) {
        super(id, mediaType, date, title, posterUrl, genre, runtime, plot, actors, director, rate, youtube, imdb);
        this.lastAirDate = lastAirDate;
    }


    public TvSeries(MediaItem mediaItem, int runtime, String plot, String actors, String director,
                     float rate, String[] youtube, Date lastAirDate, TvSeason[] seasons, String imdb) {
        super(mediaItem, runtime, plot, actors, director, rate, youtube, imdb);
        this.lastAirDate = lastAirDate;
        this.seasons = seasons;
    }

    public TvSeries(MediaItem mediaItem, int runtime, String plot, String actors, String director,
                    float rate, String[] youtube, Date lastAirDate, String imdb) {
        super(mediaItem, runtime, plot, actors, director, rate, youtube, imdb);
        this.lastAirDate = lastAirDate;
    }

    public TvSeries(Parcel in) {
        super(in);
        this.lastAirDate = (Date) in.readSerializable();
        this.seasons = in.createTypedArray(TvSeason.CREATOR);
    }


    public Date getLastAirDate() {
        return lastAirDate;
    }

    public String getLastAirYear(){
        if (lastAirDate != null)
            return SIMPLE_YEAR_FORMAT.format(lastAirDate);
        else
            return "";
    }

    public TvSeason[] getSeasons() {
        return seasons;
    }

    public void setSeasons(TvSeason[] seasons) {
        this.seasons = seasons;
    }

    public static final Creator<TvSeries> CREATOR = new Creator<TvSeries>() {
        @Override
        public TvSeries createFromParcel(Parcel source) {
            return new TvSeries(source);
        }

        @Override
        public TvSeries[] newArray(int size) {
            return new TvSeries[size];
        }
    };

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeSerializable(this.lastAirDate);
        dest.writeTypedArray(this.seasons, flags);
    }
}
