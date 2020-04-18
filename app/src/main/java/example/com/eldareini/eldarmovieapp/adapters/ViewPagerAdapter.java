package example.com.eldareini.eldarmovieapp.adapters;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.objects.ImageItem;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;

public class ViewPagerAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<ImageItem> images;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Activity activity) {
        this.activity = activity;
        this.images = new ArrayList<>();
    }

    public void add(ImageItem imageItem){
        images.add(imageItem);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ImageItem> imageItems){
        images.addAll(imageItems);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pager_layout, container, false);

        ImageView image = view.findViewById(R.id.imageBigPoster);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);



        Glide.with(activity.getApplicationContext()).load(API_GET_IMAGE + images.get(position).getImage())
                .placeholder(R.drawable.ic_autorenew_white_24dp).error(R.mipmap.no_movie).into(image);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View) object);
    }
}
