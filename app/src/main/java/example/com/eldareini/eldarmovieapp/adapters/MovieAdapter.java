package example.com.eldareini.eldarmovieapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.activities.MediaActivity;
import example.com.eldareini.eldarmovieapp.objects.Movie;
import example.com.eldareini.eldarmovieapp.MediaDBHelper;
import example.com.eldareini.eldarmovieapp.R;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;

/**
 * Created by Eldar on 8/18/2017.
 */

//Creating an Adapter that can work with RecyclerView
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    public static final String ACTION_PROGRESS = "example.com.eldareini.eldarmovieapp.PROGRESS_ACTION";
    public static final String EXTRA_IS_FINISHED = "is_finished";

    Context context;
    private ArrayList<Movie> movies = new ArrayList<>();

    public MovieAdapter(Context context) {
        this.context = context;
    }

    //Mettods to add or remove one movie or all movies
    public void add(Movie movie){
        movies.add(0 ,movie);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Movie> newMovies){
        ArrayList<Movie> endToTopMovieList = new ArrayList<>();

        for (int i = newMovies.size()-1; i >= 0; i--){
            endToTopMovieList.add(newMovies.get(i));
        }
        movies.addAll(endToTopMovieList);
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies(){
        return movies;
    }

    public void remove(Movie movie){
        movies.remove(movie);
        notifyDataSetChanged();
    }

    public void remove(long index){
        movies.remove(index);
        notifyDataSetChanged();
    }

    public Movie getMovie(int position){
        return movies.get(position);
    }

    public void clear(){
        movies.clear();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    //Make the Adapter to work with the layout
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false);

        return new MovieHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    //how many movies ther is
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //Creating am Holder for the Adapter
    public class MovieHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener, AlertDialog.OnClickListener {

        private TextView textName, textYear;
        private CheckBox checkWatched;
        private Movie currentMovie;
        private MediaDBHelper helper;
        private MenuItem itemEdit,itemShear, itemDelete;
        private ImageView imageMovie;


        public MovieHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.textName);
            textYear = (TextView) itemView.findViewById(R.id.textYear);
            checkWatched = (CheckBox) itemView.findViewById(R.id.checkWatch);
            helper = new MediaDBHelper(context);
            itemView.setOnCreateContextMenuListener(this);
            imageMovie = itemView.findViewById(R.id.imageMovie);

            checkWatched.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);


        }

        public void bind(Movie movie){
            currentMovie = movie;
            textName.setText(movie.getName());
            textYear.setText(movie.getYear());
            checkWatched.setChecked(movie.isWatched());

            Glide.with(context).load(API_GET_IMAGE + movie.getPosterUrl())
                    .placeholder(R.drawable.ic_autorenew_black_24dp).error(R.mipmap.no_movie).into(imageMovie);


            whenChecked(movie.isWatched());
        }

        //what heppen when someone check the check box
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            currentMovie.setWatched(isChecked);
            helper.updateMovie(currentMovie);

            whenChecked(isChecked);

        }

        @Override
        public void onClick(View v) {
            sendData();
        }

        private void sendData() {

            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_PROGRESS));

            Intent intent = new Intent(context, MediaActivity.class);
            intent.putExtra("movieID", currentMovie.getId());
            intent.putExtra("isUpdate", true);
            context.startActivity(intent);


        }

        //creating a context menu for the Items of the adapter list
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            itemShear = menu.add(R.menu.click_item_menu,R.id.itemMovieShere,0,"Share");
            itemDelete = menu.add(R.menu.click_item_menu,R.id.itemDelete,0,"Delete");

            itemEdit.setOnMenuItemClickListener(this);
            itemShear.setOnMenuItemClickListener(this);
            itemDelete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){

                case R.id.itemMovieShere:
                    Intent intent2 = new Intent(Intent.ACTION_SEND);
                    intent2.setType("text/plain");
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

                    intent2.putExtra(Intent.EXTRA_SUBJECT, "You need to watch that Movie!");
                    intent2.putExtra(Intent.EXTRA_TEXT,"You need to watch that Movie!\n" + currentMovie.getName() + " from " +
                            currentMovie.getDate() + "!");
                    context.startActivity(Intent.createChooser(intent2, "How do you want to share?"));

                    return true;

                case R.id.itemDelete:
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Are you Sure?")
                            .setMessage("Are you sure you want to delete " + currentMovie.getName())
                            .setPositiveButton("Yes", this)
                            .setNegativeButton("No", this)
                            .create();
                    dialog.show();

                    return true;

            }
            return false;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    helper.deleteMovie(currentMovie.getId());
                    movies.remove(currentMovie);
                    notifyDataSetChanged();
                    break;
            }
        }

        private void whenChecked(boolean isChecked){

            if (isChecked){
                if (currentMovie.getRate() > 0 && currentMovie.getRate() < 2.5f){
                    itemView.setBackgroundColor(Color.RED);
                } else if (currentMovie.getRate() >= 2.5f && currentMovie.getRate() <= 3.5f){
                    itemView.setBackgroundColor(Color.YELLOW);
                } else if (currentMovie.getRate() > 3.5f && currentMovie.getRate() <= 5 ){
                    itemView.setBackgroundColor(Color.GREEN);
                }
            } else {
                itemView.setBackgroundColor(Color.rgb(169, 134, 251));
            }

        }
    }
}
