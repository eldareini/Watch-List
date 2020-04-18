package example.com.eldareini.eldarmovieapp.objects;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

import static example.com.eldareini.eldarmovieapp.Constant.SIMPLE_YEAR_FORMAT;

public class Person extends MediaItem {

    private String  biography, place_of_birth;
    private int  gender;
    private Date deathDay;

    public Person(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, Date date, String imdb, Date deathDay, String biography, String place_of_birth, int gender) {
        super(id, mediaType, name, posterUrl, date, imdb);
        this.deathDay = deathDay;
        this.biography = biography;
        this.place_of_birth = place_of_birth;
        this.gender = gender;
    }

    public Person(MediaItem mediaItem, Date date, Date deathDay, String biography, String place_of_birth, int gender, String imdb) {
        super(mediaItem.getId(), mediaItem.getMediaType(), mediaItem.getName(), mediaItem.getPosterUrl(), date, imdb);
        this.deathDay = deathDay;
        this.biography = biography;
        this.place_of_birth = place_of_birth;
        this.gender = gender;
    }

    public Date getDeathDay() {
        return deathDay;
    }

    public String getBiography() {
        return biography;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getDeathDayYear(){
        if (deathDay != null)
            return SIMPLE_YEAR_FORMAT.format(deathDay);
        else
            return "";
    }


    public int getGender() {
        return gender;
    }

    public Person(@NonNull int id, @NonNull int mediaType, String name, String posterUrl) {
        super(id, mediaType, name, posterUrl);
    }

    public Person(@NonNull int id, @NonNull int mediaType, String name, String posterUrl, Date date) {
        super(id, mediaType, name, posterUrl, date);
    }

    public Person(MediaItem mediaItem) {
        super(mediaItem.getId(), mediaItem.getMediaType(), mediaItem.getName(), mediaItem.getPosterUrl());
    }


    public Person(Parcel in){
        super(in);
        deathDay = (Date) in.readSerializable();
        biography = in.readString();
        place_of_birth = in.readString();
        gender = in.readInt();

    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(deathDay);
        dest.writeString(biography);
        dest.writeString(place_of_birth);
        dest.writeInt(gender);
    }

}
