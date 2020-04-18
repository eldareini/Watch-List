package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.SIMPLE_FORMAT;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {

    private Context context;
    private ArrayList<TvEpisode> episodes = new ArrayList<>();

    public EpisodeAdapter(Context context) {
        this.context = context;
    }

    public void addAll(ArrayList<TvEpisode> episodes){
        this.episodes.addAll(episodes);
        notifyDataSetChanged();
    }

    public void add(TvEpisode episode){
        this.episodes.add(episode);
        notifyDataSetChanged();
    }

    public TvEpisode get(int position){
        return episodes.get(position);
    }

    @NonNull
    @Override
    public EpisodeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.episodes_season_item_layout, viewGroup, false);

        return new EpisodeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeHolder holder, int i) {

        holder.bind(episodes.get(i));

    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder {
        private ImageView imageEpisode;
        private TextView textEpisodeNumber, textEpisodeTitle, textEpisodeDate, textEpisodeOverview;

        public EpisodeHolder(@NonNull View itemView) {
            super(itemView);

            imageEpisode = itemView.findViewById(R.id.imageEpisodeSeason);
            textEpisodeNumber = itemView.findViewById(R.id.textEpisodeNumber);
            textEpisodeTitle = itemView.findViewById(R.id.textEpisodeTitle);
            textEpisodeDate = itemView.findViewById(R.id.textEpisodeDate);
            textEpisodeOverview = itemView.findViewById(R.id.textEpisodeOverview);
        }


        public void bind(TvEpisode tvEpisode){



            textEpisodeDate.setText(SIMPLE_FORMAT.format(tvEpisode.getDate()));
            textEpisodeNumber.setText("Episode" + tvEpisode.getEpisodeNum());
            textEpisodeOverview.setText(tvEpisode.getPlot());
            textEpisodeTitle.setText(tvEpisode.getName());

            Glide.with(context).load(API_GET_IMAGE + tvEpisode.getPosterUrl())
                    .placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.no_movie).into(imageEpisode);

        }
    }
}
