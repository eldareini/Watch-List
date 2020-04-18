package example.com.eldareini.eldarmovieapp.objects;

import android.os.Parcel;
import androidx.annotation.NonNull;

import java.util.Date;

public class CreditItem extends MediaItem{

    private int part, episodeCount;
    private String job;




    public CreditItem(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, int part, String job) {
        super(id, mediaType, name, posterUrl);
        this.part = part;
        this.job = job;
    }

    public CreditItem(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, Date date, String job, int part, int[] genres) {
        super(id, mediaType, date, name, posterUrl, genres);
        this.job = job;
        this.part = part;

    }

    public CreditItem(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, Date date, int episodeCount, String job, int part, int[] genres) {
        super(id, mediaType, date, name, posterUrl, genres);
        this.episodeCount = episodeCount;
        this.job = job;
        this.part = part;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }


    public int getPart() {
        return part;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void appendJob(String text){
        this.job += text;
    }

    public CreditItem(Parcel in) {
        super(in);
        this.part = in.readInt();
        this.job = in.readString();
        this.episodeCount = in.readInt();
    }

    public static final Creator<CreditItem> CREATOR = new Creator<CreditItem>() {
        @Override
        public CreditItem createFromParcel(Parcel source) {
            return new CreditItem(source);
        }

        @Override
        public CreditItem[] newArray(int size) {
            return new CreditItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(part);
        dest.writeString(job);
        dest.writeInt(episodeCount);
    }

}
