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
import androidx.lifecycle.ViewModelProvider;

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
import example.com.eldareini.eldarmovieapp.TvSeriersManager;
import example.com.eldareini.eldarmovieapp.TvSeriesListener;
import example.com.eldareini.eldarmovieapp.activities.CastAndCrewActivity;
import example.com.eldareini.eldarmovieapp.activities.YoutubeDialogActivity;
import example.com.eldareini.eldarmovieapp.activities.ImagesDialogActivity;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;
import example.com.eldareini.eldarmovieapp.tasks.CreditsDownloadTask;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_CAST_CREW;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_EPISODE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_SEASON;
import static example.com.eldareini.eldarmovieapp.Constant.SIMPLE_FORMAT;

/**
 * A simple {@link Fragment} subclass.
 */
public class EpisodeFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private ImageView imagePoster;
    private RotationRatingBar ratingMovie;
    private MediaDBHelper helper;
    private TvSeriersManager manager;
    private boolean isUpdate;
    private TvEpisode currentTvEpisode;
    private TvSeason currentSeason;
    private TvSeries currentSeries;
    private TextView textDirector, textActors, textTitle, textYear, textPlot, textGenre, textRuntime, textSeason, textEpisode, textEpisodeName;
    private ImageView btnWatchVideo;

    private OnChangeToSearchListener searchListener;
    private TvSeriesListener tvSeriesListener;


    public EpisodeFragment() {
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_episode, container, false);

        helper = new MediaDBHelper(getContext());
        manager = new TvSeriersManager(getContext());


        textSeason = v.findViewById(R.id.textSeason);

        textEpisode = v.findViewById(R.id.textEpisode);

        textTitle = v.findViewById(R.id.textMovieTitle);
        textYear =  v.findViewById(R.id.textMovieYear);
        textGenre = v.findViewById(R.id.textGenre);
        textPlot = v.findViewById(R.id.textMoviePlot);
        textRuntime = v.findViewById(R.id.textMovieRuntime);
        textEpisodeName = v.findViewById(R.id.textEpisodeName);

        ratingMovie =  v.findViewById(R.id.ratingMovie);

        imagePoster = v.findViewById(R.id.imagePoster);

        v.findViewById(R.id.btnSave).setOnClickListener(this);
        btnWatchVideo = v.findViewById(R.id.btnWatchTrailer);
        btnWatchVideo.setOnClickListener(this);
        textActors = v.findViewById(R.id.textActors);
        textDirector = v.findViewById(R.id.textDirector);
        imagePoster.setOnClickListener(this);
        v.findViewById(R.id.btnMovieShare).setOnClickListener(this);
        v.findViewById(R.id.btnSimilar).setOnClickListener(this);
        v.findViewById(R.id.btnEpisodeGuide).setOnClickListener(this);
        v.findViewById(R.id.btnImdb).setOnClickListener(this);

        btnWatchVideo.setVisibility(View.INVISIBLE);

        int movieId = getArguments().getInt("movieID", -1);

        if (movieId != -1) {
            //currentTvEpisode = helper.getMovie(movieId);

        } else {
            currentTvEpisode = (TvEpisode) getArguments().getParcelable(EXTRA_EPISODE);
            currentSeason = getArguments().getParcelable(EXTRA_SEASON);
            currentSeries = getArguments().getParcelable(EXTRA_MEDIA);
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentSeries.getName());

        setData(currentTvEpisode, currentSeries.getName());

        textActors.setOnClickListener(this);

        btnWatchVideo.setOnTouchListener(this);
        imagePoster.setOnTouchListener(this);
        v.findViewById(R.id.btnMovieShare).setOnTouchListener(this);
        v.findViewById(R.id.btnSimilar).setOnTouchListener(this);
        v.findViewById(R.id.btnEpisodeGuide).setOnTouchListener(this);
        v.findViewById(R.id.btnImdb).setOnTouchListener(this);
        textActors.setOnClickListener(this);

        isUpdate = getArguments().getBoolean("isUpdate", false);



        return v;
    }

    private void setData(TvEpisode tvEpisode, String seriesName) {
        if (tvEpisode != null) {

            currentTvEpisode = tvEpisode;


            textEpisodeName.setText(currentTvEpisode.getName());

            textSeason.append(currentSeason.getSeasonNum() + "");
            textEpisode.append(currentTvEpisode.getEpisodeNum() + "");

            textTitle.setText(seriesName);
            textPlot.setText(tvEpisode.getPlot());
            textYear.setText(SIMPLE_FORMAT.format(currentTvEpisode.getDate()) );

            ratingMovie.setRating(tvEpisode.getRate());
            if (!tvEpisode.getPosterUrl().toString().isEmpty()) {
                Glide.with(this).load(API_GET_IMAGE + tvEpisode.getPosterUrl())
                        .placeholder(R.mipmap.no_movie).error(R.mipmap.no_movie).into(imagePoster);

            }

            final Spannable spannable = new SpannableString("click for full cast and crew list!");
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textActors.setText(currentTvEpisode.getActors() + "...");
            textActors.append(spannable);

            textDirector.setText(currentTvEpisode.getDirector());


            Constant constant = new Constant();


            btnWatchVideo.setVisibility(View.VISIBLE);
            textGenre.setText(constant.getTheGenre(currentTvEpisode.getGenresIds()));
            textRuntime.setText(currentTvEpisode.getRuntime() + "min");


        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()){

            case R.id.btnSave:

                currentTvEpisode.setRate(ratingMovie.getRating());

                if (!isUpdate && helper.getEpisode(currentTvEpisode.getId()) != null){

                    isUpdate = true;

                }





                if (isUpdate) {

                    helper.updateEpisode(currentTvEpisode);
                    Toast.makeText(getContext(), "Episode Updated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    int seasonIndex = currentSeason.getSeasonNum();

                    if (seasonIndex != 0 && currentSeries.getSeasons()[0].getSeasonNum() != 0){
                        seasonIndex -= 1;
                    }

                    int episodeIndex = currentTvEpisode.getEpisodeNum();

                    if (episodeIndex != 0 && currentSeason.getEpisodes()[0].getEpisodeNum() != 0){
                        episodeIndex -= 1;
                    }


                    currentSeries.getSeasons()[seasonIndex].getEpisodes()[episodeIndex].setRate(currentTvEpisode.getRate());
                    manager.insertTvSeries(currentSeries);
                    Toast.makeText(getContext(), "Series Saved", Toast.LENGTH_SHORT).show();
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
                }).execute(currentTvEpisode);


                break;

            case R.id.btnWatchTrailer:

                if (currentTvEpisode.getYoutube() == null || currentTvEpisode.getYoutube().length == 0){
                    Toast.makeText(getContext(), "Can't download this Video", Toast.LENGTH_SHORT).show();
                } else {

                    intent = new Intent(getContext(), YoutubeDialogActivity.class);
                    intent.putExtra("Youtube", currentTvEpisode.getYoutube());
                }

                break;

            case R.id.imagePoster:

                intent = new Intent(getContext(), ImagesDialogActivity.class);
                intent.putExtra(EXTRA_MEDIA, currentTvEpisode);


                break;

            case R.id.btnMovieShare:

                setShare();

                break;

            case R.id.btnSimilar:

                searchListener.similarChangeTo(currentSeries);

                break;

            case R.id.btnEpisodeGuide:

                tvSeriesListener.goToSeasons(currentSeries, isUpdate);

                break;

            case R.id.btnImdb:

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/" + currentTvEpisode.getImdb()));

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
        if (!isUpdate && helper.getTvSeries(currentSeries.getId()) == null){
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
                        .setMessage("Are you sure you want to delete the TV Series " + currentSeries.getName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                helper.deleteTvSeries(currentSeries);
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
