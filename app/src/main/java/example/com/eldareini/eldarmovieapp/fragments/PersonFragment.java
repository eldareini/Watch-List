package example.com.eldareini.eldarmovieapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.danlew.android.joda.JodaTimeAndroid;


import org.joda.time.DateTime;
import org.joda.time.Years;

import java.util.ArrayList;
import java.util.Date;

import example.com.eldareini.eldarmovieapp.OnChangeToSearchListener;
import example.com.eldareini.eldarmovieapp.R;
import example.com.eldareini.eldarmovieapp.activities.ImagesDialogActivity;
import example.com.eldareini.eldarmovieapp.adapters.PersonCreditsAdapter;
import example.com.eldareini.eldarmovieapp.objects.CreditItem;
import example.com.eldareini.eldarmovieapp.objects.Person;
import example.com.eldareini.eldarmovieapp.tasks.CreditsDownloadTask;

import static example.com.eldareini.eldarmovieapp.Constant.API_GET_IMAGE;
import static example.com.eldareini.eldarmovieapp.Constant.EXTRA_MEDIA;
import static example.com.eldareini.eldarmovieapp.Constant.GENDER_FEMALE;
import static example.com.eldareini.eldarmovieapp.Constant.GENDER_MALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private Person currentPerson;
    private TextView textName, textGender, textYear, textBorn, textBiography, textParts;
    private ImageView imagePoster;
    private PersonCreditsAdapter adapter;
    private OnChangeToSearchListener listener;


    public PersonFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_person, container, false);
        JodaTimeAndroid.init(getContext());

        adapter = new PersonCreditsAdapter(getContext());

        currentPerson = getArguments().getParcelable(EXTRA_MEDIA);

        textName = v.findViewById(R.id.textPersonName);
        textGender = v.findViewById(R.id.textPersonGender);
        textYear = v.findViewById(R.id.textPersonYear);
        textBorn = v.findViewById(R.id.textBorn);
        textBiography = v.findViewById(R.id.textPersonBiography);
        textParts = v.findViewById(R.id.textParts);

        imagePoster = v.findViewById(R.id.imagePoster);

        RecyclerView recyclerView = v.findViewById(R.id.creditsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        v.findViewById(R.id.btnImdb).setOnClickListener(this);
        v.findViewById(R.id.btnSimilar).setOnClickListener(this);
        v.findViewById(R.id.btnMovieShare).setOnClickListener(this);

        imagePoster.setOnClickListener(this);

        v.findViewById(R.id.btnImdb).setOnTouchListener(this);
        v.findViewById(R.id.btnSimilar).setOnTouchListener(this);
        v.findViewById(R.id.btnMovieShare).setOnTouchListener(this);
        imagePoster.setOnTouchListener(this);



        if (currentPerson != null){
            setData(currentPerson);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentPerson.getName());
        }

        return v;
    }

    private void setData(Person currentPerson) {

        textName.setText(currentPerson.getName());

        switch (currentPerson.getGender()){
            case GENDER_MALE:

                textGender.setText("Male");

                break;

            case GENDER_FEMALE:

                textGender.setText("Female");

                break;
        }


        Glide.with(getContext()).load(API_GET_IMAGE + currentPerson.getPosterUrl())
                .placeholder(R.mipmap.profile_picture).error(R.mipmap.profile_picture).into(imagePoster);

        if (currentPerson.getDeathDay() != null){
            textYear.setText(currentPerson.getYear() + "-" + currentPerson.getDeathDayYear() + " (" +
                    getAge(currentPerson.getDate(), currentPerson.getDeathDay()) + ")" );
        } else {

            textYear.setText(getAge(currentPerson.getDate(), new Date(System.currentTimeMillis())));

        }

        textBorn.append(currentPerson.getPlace_of_birth());

        textBiography.setText(currentPerson.getBiography());


        new CreditsDownloadTask(new CreditsDownloadTask.OnCastAndCrewDownloadListener() {
            @Override
            public void getData(ArrayList<CreditItem> creditItems) {
                textParts.setText(creditItems.get(0).getJob());
                creditItems.remove(0);
                adapter.addAll(creditItems);
                adapter.notifyDataSetChanged();
            }
        }).execute(currentPerson);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnImdb:

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/name/" + currentPerson.getImdb()));
                startActivity(browserIntent);

                break;

            case R.id.imagePoster:

                Intent intent = new Intent(getContext(),ImagesDialogActivity.class);
                intent.putExtra(EXTRA_MEDIA, currentPerson);
                startActivity(intent);

                break;

            case R.id.btnSimilar:

                listener.searchChangeTo("", adapter.getAll(), currentPerson.getName() + " Credits");

                break;

            case R.id.btnMovieShare:

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "You need to check this person!");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"You have to see " + currentPerson.getName() + "!");
                startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));


                break;
        }
    }

    private String getAge(Date date1, Date date2){
        DateTime start = new DateTime(date1);
        DateTime end = new DateTime(date2);

        Years years = Years.yearsBetween(start, end);

        return years.getYears() + " Years Old";

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
}
