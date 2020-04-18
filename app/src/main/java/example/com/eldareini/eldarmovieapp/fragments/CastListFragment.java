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
public class CastListFragment extends Fragment {

    CreditsAdapter adapter;


    public CastListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cast_list, container, false);

        ArrayList<CreditItem> creditItems = getArguments().getParcelableArrayList("Cast");

        adapter = new CreditsAdapter(getContext());
        if (creditItems != null){
            adapter.addAll(creditItems);
            }

        RecyclerView castList = v.findViewById(R.id.castList);
        castList.setLayoutManager(new LinearLayoutManager(getContext()));
        castList.setAdapter(adapter);


        return v;
    }

}
