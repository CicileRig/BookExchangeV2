package fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import activities.NavigationActivity;
import classes.Book;
import classes.Event;
import classes.User;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class Event_Detail_Fragment extends BaseFragment {

    private  TextView event_title;
    private  TextView event_date;
    private  TextView event_hour;
    private  TextView event_description;
    private  ImageView event_image;
    private Button participate_delete_btn;

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
        event_description = view.findViewById(R.id.event_description);
        event_image = view.findViewById(R.id.event_photo_d);
        participate_delete_btn = view.findViewById(R.id.participate_delete_btn);

        // get the event from the previous intent
        final Event event = (Event) getArguments().getSerializable("event");

        // display event informations
        event_title.setText(event.getEvent_name());
        event_date.setText(event.getEvent_date());
        event_hour.setText(event.getEvent_hour());
        event_description.setText(event.getEvent_description());
        event_image.setImageBitmap(imageManager.decodeBase64(event.getEvent_image_url()));

        dataBaseManager.getEventCreatorId(event, new DataBaseManager.ResultGetter<String>() {
            @Override
            public void onResult(String s) {
            // je ne suis pas le créateur, je participe à l'evenement
                if(current.getUid().equals(s)){
                    participate_delete_btn.setText("Participer");
                }
                // je suis le créateur, je supprime l'evenement
                else {
                    participate_delete_btn.setText("Annuler");
                }
            }
        });

        /************************************ Participate to event / delete  event ********************************/
        participate_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataBaseManager.getEventCreatorId(event, new DataBaseManager.ResultGetter<String>() {
                    @Override
                    public void onResult(String s) {
                        // je ne suis pas le créateur, je participe à l'evenement
                        if(current.getUid().equals(s)){
                            Log.d("comparaison", current.getUid()+" = ? "+s);
                            dataBaseManager.getUserById(current.getUid(), new DataBaseManager.ResultGetter<User>() {
                                @Override
                                public void onResult(User user) {
                                    user.setId(current.getUid());
                                    dataBaseManager.addParticipantToEvent(event, user);
                                }
                            });
                        }
                        // je suis le créateur, je supprime l'evenement
                        else {
                            // TODO : supprimer l'evenement ( annuler )
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
