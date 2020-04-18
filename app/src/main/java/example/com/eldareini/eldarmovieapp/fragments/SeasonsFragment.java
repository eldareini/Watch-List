package example.com.eldareini.eldarmovieapp.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.TvSeriesListener;
import example.com.eldareini.eldarmovieapp.adapters.SeasonsExpandableListAdapter;
import example.com.eldareini.eldarmovieapp.objects.TvSeries;

import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_UPDATE;

public class SeasonsFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    private ExpandableListView seasonList;
    private SeasonsExpandableListAdapter adapter;
    private TvSeriesListener listener;
    private TvSeries tvSeries = null;
    private boolean isSaved;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TvSeriesListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_seasons_dialog, container, false);

        isSaved = getArguments().getBoolean(EXTRA_UPDATE);

        seasonList = v.findViewById(R.id.seasonsExpandableList);
        adapter = new SeasonsExpandableListAdapter(getContext());
        seasonList.setAdapter(adapter);

        seasonList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;
            @Override
            public void onGroupExpand(int groupPosition) {

                if (groupPosition != previousItem){
                    seasonList.collapseGroup(previousItem);
                    previousItem = groupPosition;
                }

            }
        });

        seasonList.setOnChildClickListener(this);


        tvSeries = getArguments().getParcelable(EXTRA_MEDIA);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(tvSeries.getName());

        if (tvSeries != null){
            adapter.addAll(tvSeries.getSeasons());

        }else {

            endDialog();
        }

        return v;
    }



    private void endDialog() {

        Toast.makeText(getContext(), "Sorry, could't find the info", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        listener.goToEpisode(tvSeries, adapter.getGroup(groupPosition), adapter.getChild(groupPosition,childPosition), isSaved);
        return true;
    }
}
