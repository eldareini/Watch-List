package example.com.eldareini.eldarmovieapp.objects;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.util.Date;

import static example.com.eldareini.eldarmovieapp.Constant.SIMPLE_YEAR_FORMAT;


public class MediaItem implements Parcelable, Comparable<MediaItem> {
    private int id;
    private int mediaType;
    private String  name, posterUrl, imdb;
    private int[] genresIds;
    private Date date;

    public MediaItem( @NonNull int id, @NonNull int mediaType, String name, String posterUrl) {
        this.id = id;
        this.mediaType = mediaType;
        this.name = name;
        this.posterUrl = posterUrl;
    }

    public MediaItem(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, Date date) {
        this.id = id;
        this.mediaType = mediaType;
        this.name = name;
        this.posterUrl = posterUrl;
        this.date = date;
    }

    public MediaItem(int id, int mediaType, String name, String posterUrl, Date date, String imdb) {
        this.id = id;
        this.mediaType = mediaType;
        this.date = date;
        this.name = name;
        this.posterUrl = posterUrl;
        this.imdb = imdb;
    }

    public MediaItem(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds) {
        this.id = id;
        this.mediaType = mediaType;
        this.date = date;
        this.name = name;
        this.posterUrl = posterUrl;
        this.genresIds = genresIds;
    }

    public MediaItem(int id, int mediaType, Date date, String name, String posterUrl, int[] genresIds, String imdb) {
        this.id = id;
        this.mediaType = mediaType;
        this.date = date;
        this.name = name;
        this.posterUrl = posterUrl;
        this.genresIds = genresIds;
        this.imdb = imdb;
    }

    public String getImdb() {
        return imdb;
    }

    public int[] getGenresIds() {
        return genresIds;
    }

    public int getId() {
        return id;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getName() {
        return name;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public Date getDate() {
        return date;
    }

    public String getYear(){

        if (date != null)
            return SIMPLE_YEAR_FORMAT.format(date);
        else
            return "";
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MediaItem(Parcel in){
        id = in.readInt();
        mediaType = in.readInt();
        name = in.readString();
        posterUrl = in.readString();
        date = (Date) in.readSerializable();
        genresIds = in.createIntArray();
        imdb = in.readString();

    }

    public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
        @Override
        public MediaItem createFromParcel(Parcel source) {
            return new MediaItem(source);
        }

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(mediaType);
        dest.writeString(name);
        dest.writeString(posterUrl);
        dest.writeSerializable(date);
        dest.writeIntArray(genresIds);
        dest.writeString(imdb);

    }

    @Override
    public int compareTo(MediaItem o) {
        if (o.getDate() == null)
            return 1;
        if (this.getDate() == null)
            return -1;

        if (this.getDate().compareTo(o.getDate()) > 0)
            return -1;
        else if (this.getDate().compareTo(o.getDate()) < 0)
            return 1;
        else
            return 0;
    }
}
