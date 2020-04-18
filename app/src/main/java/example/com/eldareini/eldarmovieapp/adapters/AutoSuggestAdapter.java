package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_PERSON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class AutoSuggestAdapter extends ArrayAdapter<MediaItem> implements Filterable {

    private List<MediaItem> mListData;

    public AutoSuggestAdapter(@NonNull Context context) {
        super(context, R.layout.search_list_layout);

        mListData = new ArrayList<>();
    }

    public void setData(List<MediaItem> list){
        mListData.clear();
        mListData.addAll(list);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_layout,parent, false);
        }
        ImageView image = convertView.findViewById(R.id.searchListImage);
        TextView title = convertView.findViewById(R.id.searchListTitle);
        TextView year = convertView.findViewById(R.id.searchListYear);
        TextView media = convertView.findViewById(R.id.searchListMedia);

        switch (getItem(position).getMediaType()){
            case MEDIA_TYPE_MOVIE:

                year.setText(getItem(position).getYear());
                media.setText("Movie");

                break;

            case MEDIA_TYPE_TV:

                year.setText(getItem(position).getYear());
                media.setText("TV Series");

                break;

            case MEDIA_TYPE_PERSON:

                year.setVisibility(View.GONE);
                media.setVisibility(View.GONE);

                break;
        }

        title.setText(getItem(position).getName());

        if (getItem(position).getMediaType() == MEDIA_TYPE_MOVIE || getItem(position).getMediaType() == MEDIA_TYPE_TV){

            Glide.with(getContext()).load(API_GET_IMAGE + getItem(position).getPosterUrl())
                    .placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.no_movie).into(image);
        } else if (getItem(position).getMediaType() == MEDIA_TYPE_PERSON){

            Glide.with(getContext()).load(API_GET_IMAGE + getItem(position).getPosterUrl())
                    .placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.profile_picture).into(image);

        }




        return convertView;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Nullable
    @Override
    public MediaItem getItem(int position) {
        return mListData.get(position);
    }

    public MediaItem getObject(int position){
        return mListData.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter movieFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<MediaItem> suggestions = new ArrayList<>();

                if (constraint == null || constraint.length() == 0){

                    suggestions.addAll(mListData);

                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (MediaItem mediaItem : mListData){
                        if (mediaItem.getName().toLowerCase().contains(filterPattern)){
                            suggestions.add(mediaItem);
                        }
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List) results.values);
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((MediaItem) resultValue).getName();
            }
        };

        return movieFilter;
    }
}
