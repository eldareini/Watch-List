package example.com.eldareini.eldarmovieapp.objects;

import android.os.Parcel;

import androidx.room.Entity;

import java.util.Date;

import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_EPISODE;
import static example.com.eldareini.eldarmovieapp.Constant.TABLE_EPISODES;

public class TvEpisode extends Movie {

    private int episodeNum;
    private int seriesID;
    private int seasonID;

    public TvEpisode(int id,
                     Date date,
                     String name,
                     String posterUrl,
                     int[] genresIds,
                     int runtime,
                     String plot,
                     String actors,
                     String director,
                     float rate,
                     String[] youtube,
                     String imdb,
                     int episodeNum,
                     int seriesID,
                     int seasonID) {
        super(id, MEDIA_TYPE_EPISODE, date, name, posterUrl, genresIds, runtime, plot, actors, director, rate, youtube, imdb);
        this.episodeNum = episodeNum;
        this.seriesID = seriesID;
        this.seasonID = seasonID;

    }

    public TvEpisode(int id,
                     Date date,
                     String name,
                     String posterUrl,
                     int[] genresIds,
                     int runtime,
                     String plot,
                     String actors,
                     String director,
                     boolean isWatched,
                     float rate, String[] youtube,
                     String imdb,
                     int episodeNum,
                     int seriesID,
                     int seasonID) {
        super(id, MEDIA_TYPE_EPISODE, date, name, posterUrl, genresIds, runtime, plot, actors, director, isWatched, rate, youtube, imdb);
        this.episodeNum = episodeNum;
        this.seriesID = seriesID;
        this.seasonID = seasonID;
    }

    public int getEpisodeNum() {
        return episodeNum;
    }

    public int getSeriesID() {
        return seriesID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public TvEpisode(Parcel in) {
        super(in);
        this.episodeNum = in.readInt();
        this.seriesID = in.readInt();
        this.seasonID = in.readInt();

    }



    public static final  Creator<TvEpisode> CREATOR = new Creator<TvEpisode>() {
        @Override
        public TvEpisode createFromParcel(Parcel source) {
            return new TvEpisode(source);
        }

        @Override
        public TvEpisode[] newArray(int size) {
            return new TvEpisode[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeInt(episodeNum);
        dest.writeInt(seriesID);
        dest.writeInt(seasonID);
    }
}
