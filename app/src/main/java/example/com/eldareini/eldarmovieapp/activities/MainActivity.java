package example.com.eldareini.eldarmovieapp.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.com.eldareini.eldarmovieapp.ApiCall;
import example.com.eldareini.eldarmovieapp.Constant;
import example.com.eldareini.eldarmovieapp.MediaDBHelper;
import example.com.eldareini.eldarmovieapp.OnChangeToSearchListener;
import example.com.eldareini.eldarmovieapp.adapters.AutoSuggestAdapter;
import example.com.eldareini.eldarmovieapp.adapters.MovieAdapter;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.fragments.MediaListFragment;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.fragments.SearchFragment;
import example.com.eldareini.eldarmovieapp.tasks.DownloadMediaTask;

import static example.com.eldareini.eldarmovieapp.Constant.ACTION_RELOAD;
import static example.com.eldareini.eldarmovieapp.Constant.AUTO_COMPLETE_DELAY;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_CAST_OR_CREW;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_ID;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_SEARCH;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_SIMILAR;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_TITLE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_URL;
import static example.com.eldareini.eldarmovieapp.Constant.GO_CAST_OR_CREW;
import static example.com.eldareini.eldarmovieapp.Constant.GO_SIMILAR;
import static example.com.eldareini.eldarmovieapp.Constant.TAG_MOVIE_LIST_FRAGMENT;
import static example.com.eldareini.eldarmovieapp.Constant.TRIGGER_AUTO_COMPLETE;
import static example.com.eldareini.eldarmovieapp.adapters.MovieAdapter.ACTION_PROGRESS;

//the main activity that you can see and edit you movie list

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AlertDialog.OnClickListener,
        DownloadMediaTask.OnDownloadMovieListener, OnChangeToSearchListener, View.OnTouchListener {


    private AlertDialog dialogDeleteAll, dialogExit, dialogDeleteMovies, dialogDeleteTv;
    private MediaDBHelper helper;
    private ProgressBar mProgressBar;
    private ClickedReceiver receiver;
    private AppCompatAutoCompleteTextView editSearch;
    private ImageButton btnSearch;



    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;


    @Override
    protected void onStart() {
        super.onStart();

        editSearch.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MediaItem> items = getIntent().getParcelableArrayListExtra(EXTRA_MEDIA);


        editSearch = findViewById(R.id.editSearch);

        autoSuggestAdapter = new AutoSuggestAdapter(this);
        editSearch.setThreshold(2);
        editSearch.setAdapter(autoSuggestAdapter);
        editSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                editSearch.setText(autoSuggestAdapter.getItem(position).toString());

                Intent intent = new Intent(MainActivity.this, MediaActivity.class);
                intent.putExtra(EXTRA_MEDIA, autoSuggestAdapter.getItem(position));
                startActivity(intent);

            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE){
                    if (!TextUtils.isEmpty(editSearch.getText())){
                        makeApiCall(editSearch.getText().toString());
                    }
                }

                return false;
            }
        });

        this.editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO){

                    goSearch();

                    return true;
                }
                return false;
            }
        });


        helper = new MediaDBHelper(this);




        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        btnSearch.setOnTouchListener(this);

        mProgressBar = findViewById(R.id.mProgressBar);

        receiver = new ClickedReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(ACTION_PROGRESS));

        if (savedInstanceState == null){

            Fragment fragment = new MediaListFragment();

            if (items != null){

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(EXTRA_MEDIA, items);
                fragment.setArguments(bundle);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainContainer, fragment, TAG_MOVIE_LIST_FRAGMENT)
                    .commit();
        }

        if (getIntent().getBooleanExtra(EXTRA_SIMILAR, false)){


            new DownloadMediaTask(this, this).execute(new String[]{getIntent().getStringExtra(EXTRA_ID), GO_SIMILAR});

        }

        if (getIntent().getBooleanExtra(EXTRA_CAST_OR_CREW, false)){
            new DownloadMediaTask(this, this).execute(new String[]{getIntent().getStringExtra(EXTRA_ID), GO_CAST_OR_CREW});
        }
    }

    private void makeApiCall(String text) {

        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<MediaItem> movieList = new ArrayList<>();
                try {

                    Constant constant = new Constant();

                    JSONObject obj = new JSONObject(response);
                    movieList.addAll(constant.getJSONMedia(obj.getJSONArray("results")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                autoSuggestAdapter.setData(movieList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }



    @Override
    public void onClick(View v) {

        if(!autoSuggestAdapter.isEmpty()){
            goSearch();
        }

    }

    //Create an onClick for dialogIterface, for all the dialogs
    @Override
    public void onClick(DialogInterface dialog, int which) {

        Intent intent;
        boolean isReload = false;

         if (dialog == dialogDeleteAll){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    helper.deleteAllMovies();
                    helper.deleteAllSeries();
                    isReload = true;

                    Toast.makeText(this, "All Data Deleted", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (dialog == dialogExit){
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
            }
        } else if (dialog == dialogDeleteMovies){

             switch (which){
                 case DialogInterface.BUTTON_POSITIVE:
                     helper.deleteAllMovies();
                     isReload = true;
                     Toast.makeText(this, "All Data Movies", Toast.LENGTH_SHORT).show();
                     break;
             }
         } else if(dialog == dialogDeleteTv){
             switch (which){
                 case DialogInterface.BUTTON_POSITIVE:
                     helper.deleteAllSeries();
                     isReload = true;
                     Toast.makeText(this, "All TV Series Deleted", Toast.LENGTH_SHORT).show();
                     break;
             }
         }

         if(isReload){
             LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_RELOAD));
         }
    }

    //Create option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //create the option menu do on item click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            //share the list of movies
            case R.id.itemListShare:
                String allMovieList = "";


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

                intent.putExtra(Intent.EXTRA_SUBJECT, "Look at my movie List!");
                intent.putExtra(Intent.EXTRA_TEXT,"This is my Movie List:\n" + allMovieList);
                startActivity(Intent.createChooser(intent, "How do you want to share?"));

                break;

            //delete all movies
            case R.id.itemDeleteAll:
                dialogDeleteAll = new AlertDialog.Builder(this)
                        .setTitle("Are You Sure?")
                        .setMessage("Are you sure you want to delete all movies and tv series?")
                        .setPositiveButton("Yes",this)
                        .setNegativeButton("No", this)
                        .create();
                dialogDeleteAll.show();
                break;

            //exit the app
            case R.id.itemExit:
                dialogExit = new AlertDialog.Builder(this)
                        .setTitle("Are You Sure?")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", this)
                        .setNegativeButton("No", this)
                        .create();
                dialogExit.show();

                break;

            case R.id.itemDeleteAllMovies:

                dialogDeleteMovies = new AlertDialog.Builder(this)
                        .setTitle("Are You Sure?")
                        .setMessage("Are you sure you want to delete all you're movies?")
                        .setPositiveButton("Yes", this)
                        .setNegativeButton("No", this)
                        .create();

                dialogDeleteMovies.show();

                break;

            case R.id.itemDeleteAllTv:

                dialogDeleteTv = new AlertDialog.Builder(this)
                        .setTitle("Are You Sure?")
                        .setMessage("Are you sure you want to delete all you're movies?")
                        .setPositiveButton("Yes", this)
                        .setNegativeButton("No", this)
                        .create();

                dialogDeleteTv.show();

                break;

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getData(ArrayList<MediaItem> mediaItems) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_SEARCH, mediaItems);
        bundle.putString(EXTRA_URL, "https://api.themoviedb.org/3/search/multi?api_key=07c664e1eda6cd9d46f1da1dbaefc959&query=" + editSearch.getText());


        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, searchFragment)
                .addToBackStack(TAG_MOVIE_LIST_FRAGMENT)
                .commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void searchChangeTo(String url, ArrayList<MediaItem> items, String name) {

        items.remove(items.size() -1);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_SEARCH, items);
        bundle.putString(EXTRA_URL, url);
        bundle.putString(EXTRA_TITLE, name);
        Fragment fragment = new SearchFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .addToBackStack(TAG_MOVIE_LIST_FRAGMENT)
                .commit();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void similarChangeTo(MediaItem item) {

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


    public class ClickedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(MovieAdapter.EXTRA_IS_FINISHED, false))
                mProgressBar.setVisibility(View.INVISIBLE);
            else
                mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void goSearch(){

        new DownloadMediaTask(this, this).execute(editSearch.getText().toString());

        editSearch.dismissDropDown();

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

    }

}
