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
import example.com.eldareini.eldarmovieapp.activities.CastAndCrewActivity;
import example.com.eldareini.eldarmovieapp.activities.YoutubeDialogActivity;
import example.com.eldareini.eldarmovieapp.activities.ImagesDialogActivity;
import example.com.eldareini.eldarmovieapp.adapters.ReviewAdapter;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;
import example.com.eldareini.eldarmovieapp.objects.Movie;
import example.com.eldareini.eldarmovieapp.objects.ReviewItem;
import example.com.eldareini.eldarmovieapp.tasks.CreditsDownloadTask;
import example.com.eldareini.eldarmovieapp.tasks.ReviewDownloadTask;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_CAST_CREW;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_UPDATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private ImageView imagePoster;
    private RotationRatingBar ratingMovie;
    private MediaDBHelper helper;
    private boolean isUpdate;
    private Movie currentMovie;
    private TextView textDirector, textActors, textTitle, textYear, textPlot, textGenre, textRuntime;
    private ImageView btnWatchVideo;
    private RecyclerView reviewList;
    private OnChangeToSearchListener listener;



    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnChangeToSearchListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
        View v = inflater.inflate(R.layout.fragment_movie, container, false);

        helper = new MediaDBHelper(getContext());

        reviewList = v.findViewById(R.id.reviewList);

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
        textActors = v.findViewById(R.id.textActors);
        textDirector = v.findViewById(R.id.textDirector);
        imagePoster.setOnClickListener(this);
        v.findViewById(R.id.btnMovieShare).setOnClickListener(this);
        v.findViewById(R.id.btnSimilar).setOnClickListener(this);
        v.findViewById(R.id.btnImdb).setOnClickListener(this);

        currentMovie = (Movie)(getArguments().getParcelable(EXTRA_MEDIA));

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentMovie.getName());

        setData(currentMovie);

        textActors.setOnClickListener(this);

        v.findViewById(R.id.btnMovieShare).setOnTouchListener(this);
        v.findViewById(R.id.btnSimilar).setOnTouchListener(this);
        v.findViewById(R.id.btnImdb).setOnTouchListener(this);
        textActors.setOnTouchListener(this);
        btnWatchVideo.setOnTouchListener(this);
        imagePoster.setOnTouchListener(this);

        isUpdate = getArguments().getBoolean(EXTRA_UPDATE, false);

        return v;
    }

    private void setData(Movie movie) {
        if (movie != null) {
            currentMovie = movie;

            textTitle.setText(movie.getName());
            textPlot.setText(movie.getPlot());
            textYear.setText(movie.getYear());
            ratingMovie.setRating(movie.getRate());

            if (!movie.getPosterUrl().toString().isEmpty()) {
                Glide.with(this).load(API_GET_IMAGE + movie.getPosterUrl())
                        .placeholder(R.mipmap.no_movie).error(R.mipmap.no_movie).into(imagePoster);

            }

            final Spannable spannable = new SpannableString("click for full cast and crew list!");
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textActors.setText(currentMovie.getActors() + "...");
                textActors.append(spannable);
                textDirector.setText(currentMovie.getDirector());




            final ReviewAdapter adapter = new ReviewAdapter(getContext());
            new ReviewDownloadTask(getContext(), new ReviewDownloadTask.OnReviewDownloadListener() {
                @Override
                public void getReviews(ArrayList<ReviewItem> reviewItems) {
                    adapter.addAll(reviewItems);
                }
            }).execute(currentMovie);


            reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
            reviewList.setAdapter(adapter);

            Constant constant = new Constant();


                btnWatchVideo.setVisibility(View.VISIBLE);
                textGenre.setText(constant.getTheGenre(currentMovie.getGenresIds()));
                textRuntime.setText(currentMovie.getRuntime() + "min");


        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()){

            case R.id.btnSave:

                currentMovie.setRate(ratingMovie.getRating());

                if (!isUpdate && helper.getMovie(currentMovie.getId()) != null){

                    isUpdate = true;

                }

                if (isUpdate) {

                    helper.updateMovie(currentMovie);
                    Toast.makeText(getContext(), "Movie Updated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    helper.insertMovie(currentMovie);
                    Toast.makeText(getContext(), "Movie Saved", Toast.LENGTH_SHORT).show();
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
                    }).execute(currentMovie);


                break;

            case R.id.btnWatchTrailer:

                if (currentMovie.getYoutube() == null || currentMovie.getYoutube().length == 0){
                    Toast.makeText(getContext(), "Can't download this Video", Toast.LENGTH_SHORT).show();
                } else {

                    intent = new Intent(getContext(), YoutubeDialogActivity.class);
                    intent.putExtra("Youtube", currentMovie.getYoutube());
                }

                break;

            case R.id.imagePoster:

                intent = new Intent(getContext(), ImagesDialogActivity.class);
                intent.putExtra(EXTRA_MEDIA, currentMovie);


                break;

            case R.id.btnMovieShare:

                setShare();

                break;

            case R.id.btnSimilar:

                listener.similarChangeTo(currentMovie);

                break;

            case R.id.btnImdb:

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/" + currentMovie.getImdb()));

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

        intent.putExtra(Intent.EXTRA_SUBJECT, "You need to watch that Movie!");
        intent.putExtra(Intent.EXTRA_TEXT,"You need to watch that Movie!\n" + textTitle.getText().toString() + " from " +
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
        if (!isUpdate && helper.getMovie(currentMovie.getId()) == null){
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
                        .setMessage("Are you sure you want to delete " + currentMovie.getName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                helper.deleteMovie(currentMovie.getId());
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
