package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import activities.NavigationActivity;
import classes.Event;
import classes.User;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class Event_Detail_Fragment extends BaseFragment {

    private  TextView event_title;
    private  TextView event_date;
    private  TextView event_hour;
    private  TextView event_description;
    private  TextView event_place;
    private  ImageView event_image;
    private Button participate_btn;
    private ProgressBar progressBar;
    private LinearLayout participate_layout;
    private ImageView participationIcon;
    private TextView participationText;

    private ImageManager imageManager = new ImageManager();
    private DataBaseManager dataBaseManager = new DataBaseManager();
    final FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.event_detail_fragment_layout,container, false);

        // views :
        event_title = view.findViewById(R.id.event_title);
        event_date = view.findViewById(R.id.event_date);
        event_hour = view.findViewById(R.id.event_hour);
        event_place = view.findViewById(R.id.event_place);
        event_description = view.findViewById(R.id.event_description);
        event_image = view.findViewById(R.id.event_photo_d);
        participate_btn = view.findViewById(R.id.participate_delete_btn);
        progressBar =  view.findViewById(R.id.progressBar);
        participate_layout =  view.findViewById(R.id.participate_layout);
        participationIcon= view.findViewById(R.id.imageViewParticip);
        participationText = view.findViewById(R.id.textViewParticip);

        // get the event from the previous intent
        final Event event = (Event) getArguments().getSerializable("event");

        // display event informations
        event_title.setText(event.getEvent_name());
        event_date.setText(event.getEvent_date());
        event_hour.setText(event.getEvent_hour());
        event_place.setText(event.getEvent_place());
        event_description.setText(event.getEvent_description());
        event_image.setImageBitmap(imageManager.decodeBase64(event.getEvent_image_url()));
        participate_btn.setText("Participer");
        progressBar.setVisibility(View.VISIBLE);

        dataBaseManager.checkIfUserGoToEvent(event, FirebaseAuth.getInstance().getCurrentUser().getUid(), new DataBaseManager.ResultGetter<Boolean>() {
            @Override
            public void onResult(Boolean aBoolean) {
                // Si je participe deja
                if(aBoolean == true){
                    participate_btn.setVisibility(View.GONE);
                    participate_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }else{
                    participate_btn.setVisibility(View.VISIBLE);
                    participate_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        dataBaseManager.getEventCreatorId(event, new DataBaseManager.ResultGetter<String>() {
            @Override
            public void onResult(String s) {
            // je sui le créateur
                if(current.getUid().equals(s)){
                    participate_btn.setVisibility(View.GONE);
                    participate_layout.setVisibility(View.VISIBLE);
                    participationText.setText("Créateur");
                   participationIcon.setImageResource(R.drawable.ic_creator);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        /************************************ Participate to event ********************************/
        participate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseManager.getEventCreatorId(event, new DataBaseManager.ResultGetter<String>() {
                    @Override
                    public void onResult(String s) {
                        if(!current.getUid().equals(s)){
                            User user = new User();
                            user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            user.setMailAdress(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            dataBaseManager.addParticipantToEvent(event, user);
                            participate_btn.setVisibility(View.GONE);
                            participate_layout.setVisibility(View.VISIBLE);
                        }

                    }
                });


            }
        });


        return view;
    }


    /**
     * Back pressed send from activity.
     *
     * @return if event is consumed, it will return true.
     */
    @Override
    public boolean onBackPressed() {

        Intent intent = new Intent(getActivity(), NavigationActivity.class);
        intent.putExtra("item_id",R.id.nav_event);
        startActivity(intent);
        return true;
    }

}
