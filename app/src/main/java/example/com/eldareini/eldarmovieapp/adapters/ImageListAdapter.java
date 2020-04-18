package example.com.eldareini.eldarmovieapp.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.objects.ImageItem;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    private ArrayList<ImageItem> images;
    private Context context;
    private OnPhotoPressedListener listener;


    public ImageListAdapter(Context context, OnPhotoPressedListener listener){
        this.listener = listener;
        this.context = context;
        images = new ArrayList<>();
    }

    public void addAll(ArrayList<ImageItem> s){
        images.addAll(s);
        notifyDataSetChanged();
    }

    public void add(ImageItem imageItem){
        images.add(imageItem);
        notifyDataSetChanged();
    }

    public void update(){
        notifyDataSetChanged();
    }

    public ArrayList<ImageItem> getAll(){
        return images;
    }


    public ImageItem get(int position){

        return images.get(position);

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.images_list_view, viewGroup, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        holder.bind(images.get(position));

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView listImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            listImage = itemView.findViewById(R.id.listImage);

            listImage.setOnClickListener(this);
            listImage.setOnTouchListener(new View.OnTouchListener() {
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
            });

        }

        public void bind(ImageItem imageItem){

            Glide.with(context).load(API_GET_IMAGE + imageItem.getImage()).
                    placeholder(R.drawable.ic_autorenew_white_24dp).into(listImage);

            if (imageItem.isSelected()){
                listImage.setBackground(context.getResources().getDrawable(R.drawable.image_selected_background));

            } else {
                listImage.setBackgroundColor(Color.BLACK);
            }
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.listImage:

                    listener.sendData(getAdapterPosition());

                    break;

            }

        }


    }

    public interface OnPhotoPressedListener{
        void sendData(int position);
    }

}
