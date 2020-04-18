package example.com.eldareini.eldarmovieapp.activities;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.adapters.ImageListAdapter;
import example.com.eldareini.eldarmovieapp.adapters.ViewPagerAdapter;
import example.com.eldareini.eldarmovieapp.objects.ImageItem;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.tasks.ImagesDownloadTask;

import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;

public class ImagesDialogActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, ViewPager.OnPageChangeListener {

    private RecyclerView recyclerView;
    private ImageListAdapter adapter;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView leftNav, rightNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_images_dialog);

        MediaItem mediaItem = getIntent().getParcelableExtra(EXTRA_MEDIA);

        if (mediaItem != null) {

            viewPager = findViewById(R.id.viewPagerImages);
            viewPagerAdapter = new ViewPagerAdapter(this);

            recyclerView = findViewById(R.id.imagesList);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerView.setLayoutManager(manager);

            adapter = new ImageListAdapter(this, new ImageListAdapter.OnPhotoPressedListener() {
                @Override
                public void sendData(int position) {
                    viewPager.setCurrentItem(position);

                }
            });

            adapter.add(new ImageItem(mediaItem.getPosterUrl(), true));

            new ImagesDownloadTask(new ImagesDownloadTask.OnImagesDownloadListener() {
                @Override
                public void getImages(ArrayList<ImageItem> images) {
                    adapter.addAll(images);
                    viewPagerAdapter.addAll(adapter.getAll());
                    if (viewPagerAdapter.getCount() <= 1){
                        leftNav.setVisibility(View.INVISIBLE);
                        rightNav.setVisibility(View.INVISIBLE);
                    }
                }
            }).execute(mediaItem);

            recyclerView.setAdapter(adapter);

            findViewById(R.id.btnImagesExit).setOnClickListener(this);

            viewPager.setAdapter(viewPagerAdapter);

            viewPager.addOnPageChangeListener(this);

            rightNav = findViewById(R.id.right_nav);
            rightNav.setOnClickListener(this);
            leftNav = findViewById(R.id.left_nav);
            leftNav.setOnClickListener(this);

            findViewById(R.id.btnImagesExit).setOnTouchListener(this);
            rightNav.setOnTouchListener(this);
            leftNav.setOnTouchListener(this);
        } else {
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnImagesExit:
                finish();
                break;

            case R.id.right_nav:
                viewPager.arrowScroll(View.FOCUS_RIGHT);
                break;

            case R.id.left_nav:
                viewPager.arrowScroll(View.FOCUS_LEFT);
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }


    @Override
    public void onPageSelected(int position) {

        setBackground(position);
        recyclerView.getLayoutManager().scrollToPosition(position);

        if (position == 0){
            leftNav.setVisibility(View.INVISIBLE);
        } else if (leftNav.getVisibility() == View.INVISIBLE){
            leftNav.setVisibility(View.VISIBLE);
        }

        if (position == adapter.getItemCount() - 1){
            rightNav.setVisibility(View.INVISIBLE);
        } else if (rightNav.getVisibility() == View.INVISIBLE){
            rightNav.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setBackground(int position) {

        adapter.get(position).setSelected(true);

        for (int i = 0; i < adapter.getItemCount(); i++){
            if (position != i && adapter.get(i).isSelected()){

                adapter.get(i).setSelected(false);

            }
        }

        adapter.notifyDataSetChanged();
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
