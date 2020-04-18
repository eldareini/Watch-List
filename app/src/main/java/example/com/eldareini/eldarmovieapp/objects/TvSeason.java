package example.com.eldareini.eldarmovieapp.objects;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_SEASON;
import static example.com.eldareini.eldarmovieapp.Constant.TABLE_SEASONS;

public class TvSeason extends MediaItem {

    private int seasonNum;
    private TvEpisode[] episodes;
    private int seriesID;


    public TvSeason(@NonNull int id, String posterUrl, Date date, int seasonNum, TvEpisode[] episodes, int seriesID) {
        super(id, MEDIA_TYPE_SEASON, "Season " + seasonNum, posterUrl, date);
        this.seasonNum = seasonNum;
        this.episodes = episodes;
        this.seriesID = seriesID;
    }


    public TvSeason(@NonNull int id, String posterUrl, Date date, int seasonNum, int seriesID) {
        super(id, MEDIA_TYPE_SEASON, "Season " + seasonNum, posterUrl, date);
        this.seasonNum = seasonNum;
        this.seriesID = seriesID;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public TvEpisode[] getEpisodes() {
        return episodes;
    }

    public void setEpisodes(TvEpisode[] episodes) {
        this.episodes = episodes;
    }

    public int getSeriesID() {
        return seriesID;
    }

    public TvSeason(Parcel in) {
        super(in);
        this.seasonNum = in.readInt();
        this.episodes = in.createTypedArray(TvEpisode.CREATOR);
        this.seriesID = in.readInt();

    }

    public static final Creator<TvSeason> CREATOR = new Creator<TvSeason>() {
        @Override
        public TvSeason createFromParcel(Parcel source) {
            return new TvSeason(source);
        }

        @Override
        public TvSeason[] newArray(int size) {
            return new TvSeason[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeInt(this.seasonNum);
        dest.writeTypedArray(this.episodes, flags);
        dest.writeInt(this.seriesID);

    }
}
