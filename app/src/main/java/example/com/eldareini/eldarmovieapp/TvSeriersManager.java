package example.com.eldareini.eldarmovieapp;

import android.content.Context;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

public class TvSeriersManager {
    private Context context;
    private MediaDBHelper helper;

    public TvSeriersManager(Context context) {
        this.context = context;
        helper = new MediaDBHelper(context);
    }

    public ArrayList<TvSeries> getAllTvSeries(){
        ArrayList<TvSeries> series = helper.getAllTvSeries();


        for (int i = 0; i < series.size(); i++){
            int seriesID = series.get(i).getId();
            TvSeries newSeries = series.get(i);
            ArrayList<TvSeason> seasons = helper.getSeriesSeasons(seriesID);
            TvSeason[] newSeasons = new TvSeason[seasons.size()];
            for (int k = 0; k < seasons.size(); k++){
                ArrayList<TvEpisode> episodes = helper.getSeasonEpisodes(seriesID, seasons.get(k).getId());
                newSeasons[k] = seasons.get(k);
                newSeasons[k].setEpisodes(episodes.toArray(new TvEpisode[episodes.size()]));
            }

            newSeries.setSeasons(newSeasons);

            series.set(i, newSeries);
        }



        return series;



    }

    public void updateTvSeries(TvSeries newTvSeries){
        helper.updateTvSeries(newTvSeries);

        TvSeason[] seasons = newTvSeries.getSeasons();


        for (int i = 0; i < seasons.length; i++){
            helper.updateSeason(seasons[i]);

            TvEpisode[] episodes = seasons[i].getEpisodes();
            for (int k = 0; k < episodes.length; k++){
                helper.updateEpisode(episodes[k]);
            }

        }


    }

    public void insertTvSeries(TvSeries series){

        helper.insertTvSeries(series);

        TvSeason[] seasons = series.getSeasons();


        for (int i = 0; i < seasons.length; i++){


            TvEpisode[] episodes = seasons[i].getEpisodes();
            for (int k = 0; k < episodes.length; k++){
                helper.insertEpisode(episodes[k]);
            }

            helper.insertSeason(seasons[i]);

        }

    }


}
