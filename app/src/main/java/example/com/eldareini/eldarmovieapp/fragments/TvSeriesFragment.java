package example.com.eldareini.eldarmovieapp.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.willy.ratingbar.RotationRatingBar;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.Constant;
import example.com.eldareini.eldarmovieapp.MediaDBHelper;
import example.com.eldareini.eldarmovieapp.OnChangeToSearchListener;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.TvSeriesListener;
import example.com.eldareini.eldarmovieapp.TvSeriersManager;
import example.com.eldareini.eldarmovieapp.activities.CastAndCrewActivity;
import example.com.eldareini.eldarmovieapp.activities.YoutubeDialogActivity;
import example.com.eldareini.eldarmovieapp.activities.ImagesDialogActivity;
import example.com.eldareini.eldarmovieapp.adapters.ReviewAdapter;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;
import example.com.eldareini.eldarmovieapp.objects.ReviewItem;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;
import example.com.eldareini.eldarmovieapp.tasks.CreditsDownloadTask;
import example.com.eldareini.eldarmovieapp.tasks.ReviewDownloadTask;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_CAST_CREW;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvSeriesFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private ImageView imagePoster;
    private RotationRatingBar ratingMovie;
    private MediaDBHelper helper;
    private boolean isUpdate;
    private TvSeries currentTvSeries;
    private TextView textDirector, textActors, textTitle, textYear, textPlot, textGenre, textRuntime, textSeasons, textCreator;
    private ImageView btnWatchVideo;
    private RecyclerView reviewList;
    private OnChangeToSearchListener searchListener;
    private TvSeriesListener tvSeriesListener;
    private TvSeriersManager manager;



    public TvSeriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        searchListener = (OnChangeToSearchListener) context;
        tvSeriesListener = (TvSeriesListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        searchListener = null;
        tvSeriesListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tv_series, container, false);

        helper = new MediaDBHelper(getContext());
        manager = new TvSeriersManager(getContext());

        reviewList = v.findViewById(R.id.reviewList);

        textSeasons = v.findViewById(R.id.textSeason);

        textCreator = v.findViewById(R.id.textCreator);

        textTitle = v.findViewById(R.id.textMovieTitle);
        textYear =  v.findViewById(R.id.textMovieYear);
        textGenre = v.findViewById(R.id.textGenre);
        textPlot = v.findViewById(R.id.textMoviePlot);
        textRuntime = v.findViewById(R.id.textMovieRuntime);

        ratingMovie =  v.findViewById(R.id.ratingMovie);

        imagePoster = v.findViewById(R.id.imagePoster);

        v.findViewById(R.id.btnSave).setOnClickListener(this);
        btnWatchVideo = v.findViewById(R.id.btnWatchTrailer);
        btnWatchVideo.setOnClickListener(this);
        btnWatchVideo.setOnTouchListener(this);
        textActors = v.findViewById(R.id.textActors);
        textDirector = v.findViewById(R.id.textDirector);
        imagePoster.setOnClickListener(this);
        imagePoster.setOnTouchListener(this);
        v.findViewById(R.id.btnMovieShare).setOnClickListener(this);
        v.findViewById(R.id.btnSimilar).setOnClickListener(this);
        v.findViewById(R.id.btnEpisodeGuide).setOnClickListener(this);
        v.findViewById(R.id.btnImdb).setOnClickListener(this);

        v.findViewById(R.id.btnMovieShare).setOnTouchListener(this);
        v.findViewById(R.id.btnSimilar).setOnTouchListener(this);
        v.findViewById(R.id.btnEpisodeGuide).setOnTouchListener(this);
        v.findViewById(R.id.btnImdb).setOnTouchListener(this);

        btnWatchVideo.setVisibility(View.INVISIBLE);

        currentTvSeries = (TvSeries) (getArguments().getParcelable(EXTRA_MEDIA));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentTvSeries.getName());


        setData(currentTvSeries);

        textActors.setOnClickListener(this);
        textActors.setOnTouchListener(this);

        isUpdate = getArguments().getBoolean("isUpdate", false);



        return v;
    }

    private void setData(TvSeries tvSeries) {
        if (tvSeries != null) {

            currentTvSeries = tvSeries;

            int seasons = currentTvSeries.getSeasons().length;

            for (int i = 0; i < currentTvSeries.getSeasons().length; i++){
                if (currentTvSeries.getSeasons()[i].getSeasonNum() == 0){
                    seasons--;
                    break;
                }
            }

            textSeasons.setText(seasons + "");

            textTitle.setText(tvSeries.getName());
            textPlot.setText(tvSeries.getPlot());
            textYear.setText(tvSeries.getYear() + "-" );
            if (tvSeries.getLastAirDate() != null){
                textYear.append(tvSeries.getLastAirYear());
            }
            ratingMovie.setRating(tvSeries.getRate());

            if (!tvSeries.getPosterUrl().toString().isEmpty()) {
                Glide.with(this).load(API_GET_IMAGE + tvSeries.getPosterUrl())
                        .placeholder(R.mipmap.no_movie).error(R.mipmap.no_movie).into(imagePoster);

            }

            final Spannable spannable = new SpannableString("click for full cast and crew list!");
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textActors.setText(currentTvSeries.getActors() + "...");
            textActors.append(spannable);

            if (currentTvSeries.getDirector().contains(",")){

                textCreator.setText("Creators:");

            }

            textDirector.setText(currentTvSeries.getDirector());




            final ReviewAdapter adapter = new ReviewAdapter(getContext());
            new ReviewDownloadTask(getContext(), new ReviewDownloadTask.OnReviewDownloadListener() {
                @Override
                public void getReviews(ArrayList<ReviewItem> reviewItems) {
                    adapter.addAll(reviewItems);
                }
            }).execute(currentTvSeries);


            reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
            reviewList.setAdapter(adapter);


            Constant constant = new Constant();


            btnWatchVideo.setVisibility(View.VISIBLE);
            textGenre.setText(constant.getTheGenre(currentTvSeries.getGenresIds()));
            textRuntime.setText(currentTvSeries.getRuntime() + "min");


        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()){

            case R.id.btnSave:

                currentTvSeries.setRate(ratingMovie.getRating());

                if (!isUpdate && helper.getTvSeries(currentTvSeries.getId()) != null){

                    isUpdate = true;

                }


                if (isUpdate) {

                    manager.updateTvSeries(currentTvSeries);
                    Toast.makeText(getContext(), "TV Series Updated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    manager.insertTvSeries(currentTvSeries);
                    Toast.makeText(getContext(), "TV Series Saved", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

                break;

            case R.id.textActors:

                new CreditsDownloadTask(new CreditsDownloadTask.OnCastAndCrewDownloadListener() {
                    @Override
                    public void getData(ArrayList<CreditItem> creditItems) {

                        Intent intent = new Intent(getContext(), CastAndCrewActivity.class);
                        intent.putExtra(EXTRA_CAST_CREW, creditItems);
                        startActivity(intent);

                    }
                }).execute(currentTvSeries);


                break;

            case R.id.btnWatchTrailer:

                if (currentTvSeries.getYoutube() == null || currentTvSeries.getYoutube().length == 0){
                    Toast.makeText(getContext(), "Can't download this Video", Toast.LENGTH_SHORT).show();
                } else {

                    intent = new Intent(getContext(), YoutubeDialogActivity.class);
                    intent.putExtra("Youtube", currentTvSeries.getYoutube());
                }

                break;

            case R.id.imagePoster:

                intent = new Intent(getContext(), ImagesDialogActivity.class);
                intent.putExtra(EXTRA_MEDIA, currentTvSeries);


                break;

            case R.id.btnMovieShare:

                setShare();

                break;

            case R.id.btnSimilar:

                searchListener.similarChangeTo(currentTvSeries);



                break;

            case R.id.btnEpisodeGuide:

                tvSeriesListener.goToSeasons(currentTvSeries, isUpdate);

                break;

            case R.id.btnImdb:

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/" + currentTvSeries.getImdb()));

                break;


        }

        if (intent != null){

            startActivity(intent);

        }
    }


    private void setShare(){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        intent.putExtra(Intent.EXTRA_SUBJECT, "You need to watch that TV Series!");
        intent.putExtra(Intent.EXTRA_TEXT,"You need to watch " + textTitle.getText().toString() + " from " +
                textYear.getText().toString() + "!");
        startActivity(Intent.createChooser(intent, "How do you want to share?"));

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.click_item_menu, menu);
        if (!isUpdate && helper.getTvSeries(currentTvSeries.getId()) == null){
            menu.getItem(1).setVisible(false);

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemMovieShere:
                setShare();
                break;

            case R.id.itemDelete:
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Are You Sure?")
                        .setMessage("Are you sure you want to delete " + currentTvSeries.getName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                helper.deleteTvSeries(currentTvSeries);
                                getActivity().finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
