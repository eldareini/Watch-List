package example.com.eldareini.eldarmovieapp.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.MediaDBHelper;
import example.com.eldareini.eldarmovieapp.OnChangeToSearchListener;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.TvSeriersManager;
import example.com.eldareini.eldarmovieapp.adapters.MainListAdapter;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;

import static example.com.eldareini.eldarmovieapp.Constant.ACTION_RELOAD;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_CONTINUE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_NOW;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_POPULAR;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE_UPCOMING;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV_ON_AIR;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV_POPULAR;


public class MediaListFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, MainListAdapter.OnShowMoreClickedListener {
    private MainListAdapter savedMoviesAdapter,savedTvAdapter, popularMoviesAdapter, popularTvAdapter, playingMoviesAdapter, playingTvAdapter, upcomingMoviesAdapter;
    private TextView textSavedMovies, textSavedTv, textPopularMovies, textPopularTv, textPlayingMovies, textPlayingTv, textUpcomingMovies;
    private RecyclerView listSavedMovies, listSavedTv, listPopularMovies, listPopularTv, listPlayingMovies, listPlayingTv, listUpcomingMovies;
    private MediaDBHelper helper;
    private OnChangeToSearchListener listener;
    private TvSeriersManager manager;
    private ReloadReceiver receiver;

    public MediaListFragment() {
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
    public void onStart() {
        super.onStart();

        savedMoviesAdapter.clear();
        savedMoviesAdapter.addAllMovies(helper.getAllMovies());
        setVisible(savedMoviesAdapter.getItemCount(), textSavedMovies, listSavedMovies);



        savedTvAdapter.clear();
        savedTvAdapter.addAllTv(manager.getAllTvSeries());


        setVisible(savedTvAdapter.getItemCount(), textSavedTv, listSavedTv);

        savedMoviesAdapter.add(new MediaItem(MEDIA_TYPE_MOVIE, MEDIA_TYPE_CONTINUE, "Show All", null));
        savedTvAdapter.add(new MediaItem(MEDIA_TYPE_TV, MEDIA_TYPE_CONTINUE, "Show All", null));

    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        super.onDestroy();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);

        receiver = new ReloadReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(ACTION_RELOAD));

        helper = new MediaDBHelper(getContext());
        manager = new TvSeriersManager(getContext());

        savedMoviesAdapter = new MainListAdapter(getContext(), true, false, this);
        savedTvAdapter = new MainListAdapter(getContext(), true, false, this);
        popularMoviesAdapter = new MainListAdapter(getContext(), false, false, this);
        popularTvAdapter = new MainListAdapter(getContext(), false, false, this);
        playingMoviesAdapter = new MainListAdapter(getContext(), false, false, this);
        playingTvAdapter = new MainListAdapter(getContext(), false, false, this);
        upcomingMoviesAdapter  = new MainListAdapter(getContext(), false, false, this);

        textSavedMovies = view.findViewById(R.id.textSavedMovies);
        textSavedTv = view.findViewById(R.id.textSavedTv);
        textPopularMovies = view.findViewById(R.id.textPopularMovies);
        textPopularTv = view.findViewById(R.id.textPopularTv);
        textPlayingMovies = view.findViewById(R.id.textPlayingMovies);
        textPlayingTv = view.findViewById(R.id.textPlayingTv);
        textUpcomingMovies  = view.findViewById(R.id.textUpcomingMovies);

        textPopularMovies.setOnClickListener(this);
        textPopularTv.setOnClickListener(this);
        textPlayingMovies.setOnClickListener(this);
        textPlayingTv.setOnClickListener(this);
        textUpcomingMovies.setOnClickListener(this);
        textSavedMovies.setOnClickListener(this);
        textSavedTv.setOnClickListener(this);

        textPopularMovies.setOnTouchListener(this);
        textPopularTv.setOnTouchListener(this);
        textPlayingMovies.setOnTouchListener(this);
        textPlayingTv.setOnTouchListener(this);
        textUpcomingMovies.setOnTouchListener(this);
        textSavedMovies.setOnTouchListener(this);
        textSavedTv.setOnTouchListener(this);

        listSavedMovies = view.findViewById(R.id.listSavedMovies);
        listSavedTv = view.findViewById(R.id.listSavedTv);
        listPopularMovies = view.findViewById(R.id.listPopularMovies);
        listPopularTv = view.findViewById(R.id.listPopularTv);
        listPlayingMovies = view.findViewById(R.id.listPlayingMovies);
        listPlayingTv = view.findViewById(R.id.listPlayingTv);
        listUpcomingMovies  = view.findViewById(R.id.listUpcomingMovies);

        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager manager3 = new LinearLayoutManager(getContext());
        manager3.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager manager4 = new LinearLayoutManager(getContext());
        manager4.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager manager5 = new LinearLayoutManager(getContext());
        manager5.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager manager6 = new LinearLayoutManager(getContext());
        manager6.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager manager7 = new LinearLayoutManager(getContext());
        manager7.setOrientation(LinearLayoutManager.HORIZONTAL);

        listSavedMovies.setHasFixedSize(true);
        listSavedTv.setHasFixedSize(true);
        listPopularMovies.setHasFixedSize(true);
        listPopularTv.setHasFixedSize(true);
        listPlayingMovies.setHasFixedSize(true);
        listPlayingTv.setHasFixedSize(true);
        listUpcomingMovies.setHasFixedSize(true);

        listSavedMovies.setLayoutManager(manager1);
        listSavedTv.setLayoutManager(manager2);
        listPopularMovies.setLayoutManager(manager3);
        listPopularTv.setLayoutManager(manager4);
        listPlayingMovies.setLayoutManager(manager5);
        listPlayingTv.setLayoutManager(manager6);
        listUpcomingMovies.setLayoutManager(manager7);

        listSavedMovies.setAdapter(savedMoviesAdapter);
        listSavedTv.setAdapter(savedTvAdapter);
        listPopularMovies.setAdapter(popularMoviesAdapter);
        listPopularTv.setAdapter(popularTvAdapter);
        listPlayingMovies.setAdapter(playingMoviesAdapter);
        listPlayingTv.setAdapter(playingTvAdapter);
        listUpcomingMovies.setAdapter(upcomingMoviesAdapter);

        assert getArguments() != null;
        ArrayList<MediaItem> items = getArguments().getParcelableArrayList(EXTRA_MEDIA);

        if (items != null){
            for (int i = 0; i < items.size(); i++){
                switch (items.get(i).getMediaType()){
                    case MEDIA_TYPE_MOVIE_POPULAR:

                        popularMoviesAdapter.add(items.get(i));

                        break;

                    case MEDIA_TYPE_MOVIE_NOW:

                        playingMoviesAdapter.add(items.get(i));

                        break;

                    case MEDIA_TYPE_MOVIE_UPCOMING:

                        upcomingMoviesAdapter.add(items.get(i));

                        break;

                    case MEDIA_TYPE_TV_POPULAR:

                        popularTvAdapter.add(items.get(i));

                        break;

                    case MEDIA_TYPE_TV_ON_AIR:

                        playingTvAdapter.add(items.get(i));

                        break;
                }
            }

        }


        setVisible(popularMoviesAdapter.getItemCount(), textPopularMovies, listPopularMovies);
        setVisible(popularTvAdapter.getItemCount(), textPopularTv, listPopularTv);
        setVisible(playingMoviesAdapter.getItemCount(), textPlayingMovies, listPlayingMovies);
        setVisible(playingTvAdapter.getItemCount(), textPlayingTv, listPlayingTv);
        setVisible(upcomingMoviesAdapter.getItemCount(), textUpcomingMovies, listUpcomingMovies);


        popularMoviesAdapter.add(new MediaItem(MEDIA_TYPE_MOVIE_POPULAR, MEDIA_TYPE_CONTINUE, "Show more", null ));
        playingMoviesAdapter.add(new MediaItem(MEDIA_TYPE_MOVIE_NOW, MEDIA_TYPE_CONTINUE, "Show more", null ));
        upcomingMoviesAdapter.add(new MediaItem(MEDIA_TYPE_MOVIE_UPCOMING, MEDIA_TYPE_CONTINUE, "Show more", null ));
        popularTvAdapter.add(new MediaItem(MEDIA_TYPE_TV_POPULAR, MEDIA_TYPE_CONTINUE, "Show more", null ));
        playingTvAdapter.add(new MediaItem(MEDIA_TYPE_TV_ON_AIR, MEDIA_TYPE_CONTINUE, "Show more", null ));


        return view;
    }

    private void setVisible(int itemsCount ,TextView textView, RecyclerView recyclerView){
        if (itemsCount == 0){
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void goToShowMore(int id){
        switch (id){
            case MEDIA_TYPE_MOVIE_POPULAR:

                listener.searchChangeTo("https://api.themoviedb.org/3/movie/popular?api_key=07c664e1eda6cd9d46f1da1dbaefc959", popularMoviesAdapter.getAll(), "Popular Movies");

                break;

            case MEDIA_TYPE_TV_POPULAR:

                listener.searchChangeTo("https://api.themoviedb.org/3/tv/popular?api_key=07c664e1eda6cd9d46f1da1dbaefc959", popularTvAdapter.getAll(), "Popular TV Series");

                break;

            case MEDIA_TYPE_MOVIE_NOW:

                listener.searchChangeTo("https://api.themoviedb.org/3/movie/now_playing?api_key=07c664e1eda6cd9d46f1da1dbaefc959", playingMoviesAdapter.getAll(), "Now Playing Movies");

                break;

            case MEDIA_TYPE_TV_ON_AIR:

                listener.searchChangeTo("https://api.themoviedb.org/3/tv/on_the_air?api_key=07c664e1eda6cd9d46f1da1dbaefc959", playingTvAdapter.getAll(), "Now Playing TV Series");

                break;

            case MEDIA_TYPE_MOVIE_UPCOMING:

                listener.searchChangeTo("https://api.themoviedb.org/3/movie/upcoming?api_key=07c664e1eda6cd9d46f1da1dbaefc959", upcomingMoviesAdapter.getAll(), "Upcoming Movies");

                break;

            case MEDIA_TYPE_MOVIE:
                listener.searchChangeTo("", savedMoviesAdapter.getAll(), "Your Movies");
                break;

            case MEDIA_TYPE_TV:
                listener.searchChangeTo("", savedTvAdapter.getAll(), "Your TV Series");
        }

    }


    @Override
    public void onClick(View v) {

        int id = -1;

        switch (v.getId()){
            case R.id.textPopularMovies:

                id = MEDIA_TYPE_MOVIE_POPULAR;

                break;

            case R.id.textPopularTv:

                id = MEDIA_TYPE_TV_POPULAR;

                break;

            case R.id.textPlayingMovies:

                id = MEDIA_TYPE_MOVIE_NOW;

                break;

            case R.id.textPlayingTv:

                id = MEDIA_TYPE_TV_ON_AIR;

                break;

            case R.id.textUpcomingMovies:

                id = MEDIA_TYPE_MOVIE_UPCOMING;

                break;

            case R.id.textSavedMovies:
                id = MEDIA_TYPE_MOVIE;
                break;

            case R.id.textSavedTv:
                id = MEDIA_TYPE_TV;
                break;
        }

        goToShowMore(id);

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
    public void onPressed(int id) {
        goToShowMore(id);

    }

    public class ReloadReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            onStart();
        }
    }
}
