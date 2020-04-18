package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.Constant;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.activities.MediaActivity;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;
import example.com.eldareini.eldarmovieapp.objects.Movie;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_UPDATE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_CONTINUE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_NOW;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_POPULAR;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_UPCOMING;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_PERSON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV_ON_AIR;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV_POPULAR;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainHolder> {
    private Context context;
    private boolean isSaved, isSearched;
    private ArrayList<MediaItem> items = new ArrayList<>();
    private OnShowMoreClickedListener listener;

    public MainListAdapter(Context context, boolean isSaved, boolean isSearched, OnShowMoreClickedListener listener) {
        this.context = context;
        this.isSaved = isSaved;
        this.isSearched = isSearched;
        this.listener = listener;
    }

    public void add(MediaItem item){

        MediaItem mediaItem;
        if (item.getMediaType() == MEDIA_TYPE_MOVIE_NOW || item.getMediaType() == MEDIA_TYPE_MOVIE_POPULAR
                || item.getMediaType() == MEDIA_TYPE_MOVIE_UPCOMING){
            mediaItem = new Movie(item.getId(), MEDIA_TYPE_MOVIE, item.getDate(), item.getName(), item.getPosterUrl(), item.getGenresIds());
        } else if (item.getMediaType() == MEDIA_TYPE_TV_ON_AIR || item.getMediaType() == MEDIA_TYPE_TV_POPULAR){
            mediaItem = new TvSeries(item.getId(), MEDIA_TYPE_TV, item.getDate(), item.getName(), item.getPosterUrl(), item.getGenresIds());
        } else {
            mediaItem = item;
        }

        items.add(mediaItem);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<MediaItem> mediaItems){
        if(isSaved){
            for (int i = mediaItems.size() -1; i >= 0 ; i--) {
                items.add(mediaItems.get(i));
            }
        } else {
        items.addAll(mediaItems);
        }
        notifyDataSetChanged();
    }

    public void addAllMovies(ArrayList<Movie> movies){
        if(isSaved){
            for (int i = movies.size() -1; i >= 0 ; i--) {
                items.add(movies.get(i));
            }
        } else {
            items.addAll(movies);
        }
        notifyDataSetChanged();
    }

    public void addAllTv(ArrayList<TvSeries> tvSeries){
        if(isSaved){
            for (int i = tvSeries.size() -1; i >= 0 ; i--) {
                items.add(tvSeries.get(i));
            }
        } else {
            items.addAll(tvSeries);
        }
        notifyDataSetChanged();
    }

    public ArrayList<MediaItem> getAll(){
        return items;
    }

    public void remove(int position){
        items.remove(position);
        notifyDataSetChanged();
    }

    public void remove(MediaItem item){
        items.remove(item);
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.choose_media_layout, viewGroup, false);
        return new MainHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder mainHolder, int i) {

        mainHolder.bind(items.get(i));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        private ImageView image;
        private TextView textTitle, textGenre, textMediaType;
        private MediaItem currentItem;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public MainHolder(@NonNull View v) {
            super(v);

            image = v.findViewById(R.id.imagePoster);
            textTitle = v.findViewById(R.id.textTitle);
            textGenre = v.findViewById(R.id.textGenre);
            textMediaType = v.findViewById(R.id.textMediaType);

            v.setOnClickListener(this);
            v.setOnTouchListener(this);

        }

        public void bind(MediaItem item){

            currentItem = item;

            if (isSearched){
                switch (item.getMediaType()){
                    case MEDIA_TYPE_MOVIE:
                        textMediaType.setText("Movie");
                        break;

                    case MEDIA_TYPE_TV:
                        textMediaType.setText("TV Series");
                        break;

                    case MEDIA_TYPE_PERSON:
                    case MEDIA_TYPE_CONTINUE:
                    default:
                        textMediaType.setText("");
                        break;

                }
            } else
                textMediaType.setText("");

            Constant constant = new Constant();
            if (item.getMediaType() != MEDIA_TYPE_CONTINUE)
            textTitle.setText(item.getName());
            else
                textTitle.setText("");
            if (item.getMediaType() != MEDIA_TYPE_PERSON )
                if(item.getMediaType() == MEDIA_TYPE_CONTINUE){
                    textGenre.setText(item.getName());

                } else
                textGenre.setText(constant.getTheGenre(item.getGenresIds()));
            else
                textGenre.setText("");

            if(item.getMediaType() != MEDIA_TYPE_CONTINUE){

                Glide.with(context).load(API_GET_IMAGE + item.getPosterUrl())
                    .placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.no_movie).into(image);
            } else  {
                Glide.with(context).load(R.drawable.ic_send).into(image);
            }

        }

        @Override
        public void onClick(View v) {

            if(currentItem.getMediaType() != MEDIA_TYPE_CONTINUE){

                Intent intent = new Intent(context, MediaActivity.class);
                intent.putExtra(EXTRA_MEDIA, currentItem);

                if (isSaved)
                    intent.putExtra(EXTRA_UPDATE, true);

                context.startActivity(intent);
            } else  {

                listener.onPressed(currentItem.getId());

            }

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(0.5f);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    v.setAlpha(1f);
                    break;
                }
            }
            return false;
        }
    }

    public interface OnShowMoreClickedListener{
        void onPressed(int id);
    }
}
