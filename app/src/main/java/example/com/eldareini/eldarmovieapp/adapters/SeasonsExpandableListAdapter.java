package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.SIMPLE_FORMAT;

public class SeasonsExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<TvSeason> seasons = new ArrayList<>();
    private HashMap<TvSeason, TvEpisode[]> hashMap = new HashMap<>();
    //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");



    public SeasonsExpandableListAdapter(Context context) {
        this.context = context;
    }

    public void add(TvSeason tvSeason){
        seasons.add(tvSeason);
        hashMap.put(tvSeason, tvSeason.getEpisodes());
        notifyDataSetChanged();
    }

    public void addAll(TvSeason[] seasons){
        for (int i = 0; i < seasons.length; i++){

            this.seasons.add(seasons[i]);
            hashMap.put(seasons[i], seasons[i].getEpisodes());
        }

        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return seasons.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return hashMap.get(seasons.get(groupPosition)).length;
    }

    @Override
    public TvSeason getGroup(int groupPosition) {
        return seasons.get(groupPosition);
    }

    @Override
    public TvEpisode getChild(int groupPosition, int childPosition) {
        return hashMap.get(seasons.get(groupPosition))[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        TvSeason tvSeason = (TvSeason) getGroup(groupPosition);

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_expandable_group, null);
        }

        TextView textSeasonNumber = view.findViewById(R.id.textSeasonNumber);
        TextView textSeasonYear = view.findViewById(R.id.textSeasonYear);
        TextView textEpisodes = view.findViewById(R.id.textEpisodes);
        ImageView imageSeason = view.findViewById(R.id.seasonImage);

        if (tvSeason.getSeasonNum() == 0){
            textSeasonNumber.setText("Specials");
        } else {

            textSeasonNumber.setText("Season " + tvSeason.getSeasonNum());

        }




        textSeasonYear.setText(SIMPLE_FORMAT.format(tvSeason.getDate()));

        textEpisodes.setText(tvSeason.getEpisodes().length + " Episodes");

        Glide.with(context).load(API_GET_IMAGE + tvSeason.getPosterUrl()).
                placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.no_movie).into(imageSeason);

        return view;

    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        TvEpisode episode = (TvEpisode) getChild(groupPosition, childPosition);

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.episodes_season_item_layout, null);
        }

        TextView textPlot = view.findViewById(R.id.textEpisodeOverview);
        TextView textName = view.findViewById(R.id.textEpisodeTitle);
        TextView textDate = view.findViewById(R.id.textEpisodeDate);
        TextView textEpisode = view.findViewById(R.id.textEpisodeNumber);
        ImageView imageView = view.findViewById(R.id.imageEpisodeSeason);

        Glide.with(context).load(API_GET_IMAGE + episode.getPosterUrl()).placeholder(R.drawable.ic_autorenew_black_24dp)
                .error(R.mipmap.no_movie).into(imageView);

        textPlot.setText(episode.getPlot());
        textName.setText(episode.getName());
        textDate.setText(SIMPLE_FORMAT.format(episode.getDate()));
        textEpisode.setText("Episode " + episode.getEpisodeNum());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
