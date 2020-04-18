package example.com.eldareini.eldarmovieapp.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.adapters.CreditsAdapter;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrewListFragment extends Fragment {

    CreditsAdapter adapter;


    public CrewListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crew_list, container, false);

        ArrayList<CreditItem> creditItems = getArguments().getParcelableArrayList("Crew");

        adapter = new CreditsAdapter(getContext());
        if (creditItems != null) {
            adapter.addAll(creditItems);
        }

            RecyclerView crewList = v.findViewById(R.id.crewList);
            crewList.setLayoutManager(new LinearLayoutManager(getContext()));
            crewList.setAdapter(adapter);

            return v;
        }
}


