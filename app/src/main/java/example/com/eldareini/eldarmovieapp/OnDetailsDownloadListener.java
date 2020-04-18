package example.com.eldareini.eldarmovieapp;

import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.Movie;
import example.com.eldareini.eldarmovieapp.objects.Person;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

public interface OnDetailsDownloadListener {
    void getMediaDetails(MediaItem mediaItem);

}
