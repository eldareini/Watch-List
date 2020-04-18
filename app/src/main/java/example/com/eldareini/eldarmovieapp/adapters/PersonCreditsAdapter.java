package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.activities.MediaActivity;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class PersonCreditsAdapter extends RecyclerView.Adapter<PersonCreditsAdapter.CreditHolder> {

    Context context;
    ArrayList<CreditItem> creditItems = new ArrayList<>();

    public PersonCreditsAdapter(Context context) {
        this.context = context;
    }


    public void addAll(ArrayList<CreditItem> creditItems){


        this.creditItems.addAll(creditItems);

        ArrayList<CreditItem> deletePosition = new ArrayList<>();
        for (int i = 0; i < creditItems.size(); i++){

            for (int j = (i + 1); j < creditItems.size(); j++ ){
                if (creditItems.get(i).getId() == creditItems.get(j).getId()){

                    boolean isStored = false;

                    for (int k = 0; k < deletePosition.size(); k++){
                        if (creditItems.get(j) == deletePosition.get(k)){

                            isStored = true;
                            break;
                        }
                    }

                    if (!isStored){
                        deletePosition.add(creditItems.get(j));
                        this.creditItems.get(i).appendJob(" | " + creditItems.get(j).getJob());
                    }

                }
            }
        }

        for (int k = 0; k < deletePosition.size(); k++){
            this.creditItems.remove((deletePosition.get(k)));
        }

        Collections.sort(this.creditItems, new Comparator<CreditItem>() {
            @Override
            public int compare(CreditItem o1, CreditItem o2) {
                return o1.compareTo(o2);
            }
        });

        notifyDataSetChanged();

    }


    public ArrayList<MediaItem> getAll(){
        ArrayList<MediaItem> items = new ArrayList<>();
        for (int i = 0; i < creditItems.size(); i++){
            items.add(creditItems.get(i));
        }
        return items;
    }

    @NonNull
    @Override
    public CreditHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.person_credits_layout, viewGroup, false);
        return new CreditHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditHolder holder, int i) {
        holder.bind(creditItems.get(i));

    }

    @Override
    public int getItemCount() {
        return creditItems.size();
    }

    public class CreditHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imagePoster;
        private TextView textTitle, textYear, textMedia, textEpisodeNum, textCharacter;
        private MediaItem item;

        public CreditHolder(@NonNull View itemView) {
            super(itemView);

            imagePoster = itemView.findViewById(R.id.imageMediaPerson);
            textTitle = itemView.findViewById(R.id.textMediaTitle);
            textMedia = itemView.findViewById(R.id.textMedia);
            textYear = itemView.findViewById(R.id.textMediaYear);
            textEpisodeNum = itemView.findViewById(R.id.textEpisodesNum);
            textCharacter = itemView.findViewById(R.id.textCharacter);

            itemView.setOnClickListener(this);
        }

        public void bind(CreditItem creditItem){

            item = creditItem;

            Glide.with(context).load(API_GET_IMAGE + creditItem.getPosterUrl())
                    .placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.no_movie).into(imagePoster);

            textTitle.setText(creditItem.getName());
            textYear.setText(creditItem.getYear());
            textCharacter.setText(creditItem.getJob());
            switch (creditItem.getMediaType()){
                case MEDIA_TYPE_MOVIE:
                    textEpisodeNum.setVisibility(View.GONE);
                    textMedia.setText("Movie");

                    break;

                case MEDIA_TYPE_TV:
                    if(creditItem.getEpisodeCount() > 0) {
                        textEpisodeNum.setVisibility(View.VISIBLE);
                        textEpisodeNum.setText(creditItem.getEpisodeCount() + " Episodes");
                    }
                    textMedia.setText("TV Show");

                    break;

            }


        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, MediaActivity.class);
            intent.putExtra(EXTRA_MEDIA, item);
            context.startActivity(intent);

        }
    }
}
