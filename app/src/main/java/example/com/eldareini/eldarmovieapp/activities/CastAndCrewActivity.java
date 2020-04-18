package example.com.eldareini.eldarmovieapp.activities;

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Window;

import java.util.ArrayList;

import example.com.eldareini.eldarmovieapp.fragments.CastListFragment;
import example.com.eldareini.eldarmovieapp.fragments.CrewListFragment;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.adapters.SectionsPageAdapter;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;

import static example.com.eldareini.eldarmovieapp.Constant.CREDIT_CAST_MEMBER;
import static example.com.eldareini.eldarmovieapp.Constant.CREDIT_CREW_MEMBER;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_CAST_CREW;

public class CastAndCrewActivity extends AppCompatActivity {


    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager pager;
    private ArrayList<CreditItem> castMembers = new ArrayList<>();
    private ArrayList<CreditItem> crewMembers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cast_and_crew);

        ArrayList<CreditItem> creditItems = getIntent().getParcelableArrayListExtra(EXTRA_CAST_CREW);
        if (creditItems != null){

            for (int i = 0; i < creditItems.size(); i++){
                switch (creditItems.get(i).getPart()){
                    case CREDIT_CAST_MEMBER:

                        castMembers.add(creditItems.get(i));

                        break;

                    case CREDIT_CREW_MEMBER:

                        crewMembers.add(creditItems.get(i));

                        break;
                }
            }
        }

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.container);

        setUpViewPager(pager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);


    }

    private void setUpViewPager(ViewPager viewPager){

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Cast", castMembers);
        bundle.putParcelableArrayList("Crew", crewMembers);

        Fragment castFragment = new CastListFragment();
        castFragment.setArguments(bundle);

        Fragment crewFragment = new CrewListFragment();
        crewFragment.setArguments(bundle);

        adapter.addFragment(castFragment, "Cast");
        adapter.addFragment(crewFragment, "Crew");
        viewPager.setAdapter(adapter);


    }

}
