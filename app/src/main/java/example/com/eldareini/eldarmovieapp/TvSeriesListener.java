package example.com.eldareini.eldarmovieapp;

import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

public interface TvSeriesListener {
    void goToSeasons(TvSeries series, boolean isSaved);
    void goToEpisode(TvSeries series, TvSeason season, TvEpisode episode, boolean isSaved);
}
