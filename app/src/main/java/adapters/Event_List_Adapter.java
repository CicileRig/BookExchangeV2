package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import classes.Event;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class Event_List_Adapter extends ArrayAdapter<Event> implements View.OnClickListener{

    private ArrayList<Event> dataSet;
    Activity activity;
    Context mContext;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DataBaseManager dataBaseManager = new DataBaseManager();

    // View lookup cache
    private static class ViewHolder {
        TextView eventName;
        TextView eventDate;
        TextView eventHour;
        TextView eventPlace;
        ImageView eventImage;
        LinearLayout linearLayoutParticip;
        ImageView participationIcon;
        TextView participationText;
    }

    public Event_List_Adapter(ArrayList<Event> data, Activity activity) {
        super(activity, R.layout.row_event_item, data);
        this.dataSet = data;
        this.activity=activity;
        this.mContext=activity;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Event event =(Event) object;

        switch (v.getId())
        {
            case R.id.book_name:
                Snackbar.make(v, "Release date " + event.getEvent_name(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event event = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_event_item, parent, false);
            viewHolder.eventName =  convertView.findViewById(R.id.book_name);
            viewHolder.eventDate =  convertView.findViewById(R.id.event_date);
            viewHolder.eventHour =  convertView.findViewById(R.id.event_hour);
            viewHolder.eventImage =  convertView.findViewById(R.id.event_image_r);
            viewHolder.eventPlace = convertView.findViewById(R.id.event_place);
            viewHolder.linearLayoutParticip = convertView.findViewById(R.id.participate_layout);
            viewHolder.participationIcon= convertView.findViewById(R.id.imageViewParticip);
            viewHolder.participationText = convertView.findViewById(R.id.textViewParticip);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

       /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);*/
        lastPosition = position;

        viewHolder.eventName.setText(event.getEvent_name());
        viewHolder.eventDate.setText(event.getEvent_date());
        viewHolder.eventHour.setText("A : " +event.getEvent_hour());
        viewHolder.eventPlace.setText(event.getEvent_place());

        if(event.getEvent_image_url() != null){
            ImageManager imageManager = new ImageManager();
            viewHolder.eventImage.setImageBitmap(imageManager.decodeBase64(event.getEvent_image_url()));
        }

        final View finalConvertView = convertView;
        dataBaseManager.getEventCreatorId(event, new DataBaseManager.ResultGetter<String>() {
            @Override
            public void onResult(String s) {
                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(s)){
                    viewHolder.linearLayoutParticip.setVisibility(finalConvertView.VISIBLE);
                    viewHolder.participationText.setText("Créateur");
                    viewHolder.participationIcon.setImageResource(R.drawable.ic_creator);
                }else{
                    viewHolder.linearLayoutParticip.setVisibility(finalConvertView.GONE);
                }
            }
        });

        dataBaseManager.checkIfUserGoToEvent(event, FirebaseAuth.getInstance().getCurrentUser().getUid(), new DataBaseManager.ResultGetter<Boolean>() {
            @Override
            public void onResult(Boolean aBoolean) {
                if(aBoolean == true){
                    viewHolder.linearLayoutParticip.setVisibility(finalConvertView.VISIBLE);
                    viewHolder.participationText.setText("J y vais");
                    viewHolder.participationIcon.setImageResource(R.drawable.ic_heart);
                }else{
                    viewHolder.linearLayoutParticip.setVisibility(finalConvertView.GONE);
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
