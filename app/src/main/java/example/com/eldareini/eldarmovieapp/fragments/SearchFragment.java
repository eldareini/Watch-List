package example.com.eldareini.eldarmovieapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.activities.MediaActivity;
import example.com.eldareini.eldarmovieapp.adapters.MainListAdapter;
import example.com.eldareini.eldarmovieapp.adapters.SearchAdapter;
import example.com.eldareini.eldarmovieapp.objects.MediaItem;
import example.com.eldareini.eldarmovieapp.tasks.MoreMediaTask;

import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_SEARCH;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_TITLE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchAdapter.OnSearchListListener {
    private MainListAdapter adapter;
    private int nextPage = 2;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        String title = getArguments().getString(EXTRA_TITLE);

        if(title != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        }


        adapter = new MainListAdapter(getContext(), false, true, new MainListAdapter.OnShowMoreClickedListener() {
            @Override
            public void onPressed(int id) {

            }
        });
        RecyclerView searchList = view.findViewById(R.id.listSearch);
        searchList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        searchList.setAdapter(adapter);

        searchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                String url = getArguments().getString(EXTRA_URL);


                if (!recyclerView.canScrollVertically(1)){

                    new MoreMediaTask(getContext(), new MoreMediaTask.OnMediaDownloadedListener() {
                        @Override
                        public void getMedia(ArrayList<MediaItem> items) {

                            if (items != null) {
                                adapter.addAll(items);
                                nextPage++;
                            } else
                                Toast.makeText(getContext(), "That's All", Toast.LENGTH_SHORT).show();

                        }
                    }).execute(new String[]{url, nextPage +""});

                }
            }
        });

        ArrayList<MediaItem> mediaItems = getArguments().getParcelableArrayList(EXTRA_SEARCH);


        if (mediaItems != null) {

            adapter.addAll(mediaItems);
        }

        return view;
    }

    @Override
    public void getMedia(MediaItem mediaItem) {


                Intent intent = new Intent(getContext(), MediaActivity.class);
                intent.putExtra(EXTRA_MEDIA, mediaItem);
                startActivity(intent);

    }




}
