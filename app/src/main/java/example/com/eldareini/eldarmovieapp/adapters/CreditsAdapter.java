package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.activities.MediaActivity;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.PeopleHolder> {

    private Context context;
    private ArrayList<CreditItem> people = new ArrayList<>();

    public CreditsAdapter(Context context) {
        this.context = context;
    }

    public void add(CreditItem credit){
        people.add(credit);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<CreditItem> creditItems){
        this.people.addAll(creditItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeopleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.cast_crew_layout, viewGroup, false);
        return new PeopleHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleHolder castHolder, int i) {
        castHolder.bind(people.get(i));

    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class PeopleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textName, textPart;
        private CircleImageView imageCast;
        private MediaItem item;

        public PeopleHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textNameCastCrew);
            textPart = itemView.findViewById(R.id.textPartCastCrew);
            imageCast = itemView.findViewById(R.id.imageCastCrew);
            itemView.setOnClickListener(this);
        }

        public void bind(CreditItem credit){

            item = credit;

            textName.setText(credit.getName());
            textPart.setText(credit.getJob());
            Glide.with(context).load(API_GET_IMAGE + credit.getPosterUrl()).centerCrop()
                    .placeholder(R.mipmap.user).error(R.mipmap.user).into(imageCast);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MediaActivity.class);
            intent.putExtra(EXTRA_MEDIA, item );
            context.startActivity(intent);

        }
    }
}
