package example.com.eldareini.eldarmovieapp.activities;

import androidx.fragment.app.Fragment;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.OnChangeToSearchListener;
import example.com.eldareini.eldarmovieapp.OnDetailsDownloadListener;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.TvSeriesListener;
import example.com.eldareini.eldarmovieapp.fragments.EpisodeFragment;
import example.com.eldareini.eldarmovieapp.fragments.MovieFragment;
import example.com.eldareini.eldarmovieapp.fragments.PersonFragment;
import example.com.eldareini.eldarmovieapp.fragments.SearchFragment;
import example.com.eldareini.eldarmovieapp.fragments.SeasonsFragment;
import example.com.eldareini.eldarmovieapp.fragments.TvSeriesFragment;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.objects.TvEpisode;
import example.com.eldareini.eldarmovieapp.objects.TvSeason;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;
import example.com.eldareini.eldarmovieapp.tasks.DetailsMediaDownloadTask;
import example.com.eldareini.eldarmovieapp.tasks.MoreMediaTask;

import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_EPISODE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_SEARCH;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_SEASON;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_TITLE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_UPDATE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_URL;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_EPISODE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_MOVIE;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_PERSON;
import static example.com.eldareini.eldarmovieapp.Constant.MEDIA_TYPE_TV;

public class MediaActivity extends AppCompatActivity implements OnDetailsDownloadListener, OnChangeToSearchListener, TvSeriesListener {
    private ProgressBar progressBar;
    private boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBarMedia);
        progressBar.setVisibility(View.VISIBLE);

        MediaItem mediaItem = getIntent().getParcelableExtra(EXTRA_MEDIA);
        isSaved = getIntent().getBooleanExtra(EXTRA_UPDATE, false);

        if (mediaItem != null){
            if (isSaved)
                goToMedia(mediaItem);
            else
                new DetailsMediaDownloadTask(this).execute(mediaItem);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void getMediaDetails(MediaItem mediaItem) {

        if (mediaItem != null) {

            goToMedia(mediaItem);


        }

    }

    private void goToMedia(MediaItem item){

        Fragment fragment = null;

        switch (item.getMediaType()) {
            case MEDIA_TYPE_MOVIE:

                fragment = new MovieFragment();

                break;

            case MEDIA_TYPE_TV:

                fragment = new TvSeriesFragment();

                break;

            case MEDIA_TYPE_PERSON:

                fragment = new PersonFragment();

                break;

            case MEDIA_TYPE_EPISODE:
                fragment = new EpisodeFragment();
        }

        if (fragment != null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_MEDIA, item);
            bundle.putBoolean(EXTRA_UPDATE, isSaved);

            fragment.setArguments(bundle);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mediaContainer, fragment, "myMedia")
                    .commit();

            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void searchChangeTo(String url, ArrayList<MediaItem> items, String title) {


        goToSearch(url, items, title);

    }

    private void goToSearch(String url, ArrayList<MediaItem> items, String title) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_SEARCH ,items);
        bundle.putString(EXTRA_URL, url);
        bundle.putString(EXTRA_TITLE, title);

        Fragment fragment = new SearchFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mediaContainer, fragment)
                .addToBackStack("myMedia")
                .commit();
    }

    @Override
    public void similarChangeTo(MediaItem item) {

        progressBar.setVisibility(View.VISIBLE);

        String url = "";
        String title = "Similar to " + item.getName();


        if (item.getMediaType() == MEDIA_TYPE_MOVIE) {

            url = "https://api.themoviedb.org/3/movie/"+item.getId()+"/similar?api_key=07c664e1eda6cd9d46f1da1dbaefc959";


        } else if(item.getMediaType() == MEDIA_TYPE_TV || item.getMediaType() == MEDIA_TYPE_EPISODE){

            url = "https://api.themoviedb.org/3/tv/"+item.getId()+"/similar?api_key=07c664e1eda6cd9d46f1da1dbaefc959";
        }

        final String finalUrl = url;
        new MoreMediaTask(this, new MoreMediaTask.OnMediaDownloadedListener() {
            @Override
            public void getMedia(ArrayList<MediaItem> items) {
                goToSearch(finalUrl, items, title);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).execute(new String[]{url, 1 + ""});

    }


    @Override
    public void goToSeasons(TvSeries series, boolean isSaved) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MEDIA, series);
        bundle.putBoolean(EXTRA_UPDATE, isSaved);
        SeasonsFragment fragment = new SeasonsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("myMedia")
                .replace(R.id.mediaContainer, fragment, "seasonsFragment")
                .commit();

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void goToEpisode(final TvSeries series, final TvSeason season, TvEpisode episode, final boolean isSaved) {

        progressBar.setVisibility(View.VISIBLE);
        if (isSaved && episode.getImdb() != null && episode.getActors() != null)
            setEpisode(series, season, episode, isSaved);
        else {
            new DetailsMediaDownloadTask(new OnDetailsDownloadListener() {
                @Override
                public void getMediaDetails(MediaItem mediaItem) {

                    setEpisode(series, season, (TvEpisode) mediaItem, isSaved);

                }
            }).execute(new MediaItem[]{episode, season, series});
        }


    }

    private void setEpisode(TvSeries series, TvSeason season, TvEpisode episode, boolean isSaved){

        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MEDIA, series);
        bundle.putParcelable(EXTRA_SEASON, season);
        bundle.putParcelable(EXTRA_EPISODE, episode );
        bundle.putBoolean(EXTRA_UPDATE, isSaved);
        EpisodeFragment fragment = new EpisodeFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("seasonsFragment")
                .replace(R.id.mediaContainer, fragment, "myMedia")
                .commit();

        progressBar.setVisibility(View.INVISIBLE);

    }

}
