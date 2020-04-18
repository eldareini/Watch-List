package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.objects.MediaItem;

import example.com.eldareini.eldarmovieapp.R;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_PERSON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {
    private Context context;
    private OnSearchListListener listListener;
    private ArrayList<MediaItem> mediaItems = new ArrayList<>();

    public SearchAdapter(Context context, OnSearchListListener listListener) {
        this.context = context;
        this.listListener = listListener;
    }

    public void addAll(ArrayList<MediaItem> mediaItems){
        this.mediaItems.addAll(mediaItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.search_list_layout, null);
        return new SearchHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder searchHolder, int i) {

        searchHolder.bind(mediaItems.get(i));

    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageSearch;
        private TextView textTitle, textYear, textMedia;
        private MediaItem currentMediaItem;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            imageSearch = itemView.findViewById(R.id.searchListImage);
            textTitle = itemView.findViewById(R.id.searchListTitle);
            textYear = itemView.findViewById(R.id.searchListYear);
            textMedia = itemView.findViewById(R.id.searchListMedia);

            itemView.setOnClickListener(this);
        }

        public void bind(MediaItem mediaItem){

            currentMediaItem = mediaItem;
            textTitle.setText(mediaItem.getName());

            switch (mediaItem.getMediaType()){
                case MEDIA_TYPE_MOVIE:

                    textYear.setText(mediaItem.getYear());
                    textMedia.setText("Movie");

                    break;

                case MEDIA_TYPE_TV:

                    textYear.setText(mediaItem.getYear());
                    textMedia.setText("TV Series");

                    break;

                case MEDIA_TYPE_PERSON:

                    textYear.setVisibility(View.GONE);
                    textMedia.setVisibility(View.GONE);

                    break;
            }


            Glide.with(context).load(API_GET_IMAGE + mediaItem.getPosterUrl()).
                    placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.no_movie).into(imageSearch);

        }

        @Override
        public void onClick(View v) {
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                v.setSelected(false);
            }
            else {
                selectedItems.put(getAdapterPosition(), true);
                v.setSelected(true);
                listListener.getMedia(currentMediaItem);
            }



        }
    }

    public interface OnSearchListListener{
        void getMedia(MediaItem mediaItem);
    }
}
