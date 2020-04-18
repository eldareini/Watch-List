package example.com.eldareini.eldarmovieapp;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.objects.MediaItem;

public interface OnChangeToSearchListener {
    void searchChangeTo(String url, ArrayList<MediaItem> items, String name);
    void similarChangeTo(MediaItem item);
}
