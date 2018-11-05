package fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bcs.bookexchangev2.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import activities.NavigationActivity;
import classes.Book;
import classes.Event;
import controllers.DataBaseManager;
import controllers.ImageManager;

public class Event_Detail_Fragment extends BaseFragment {

    private  TextView event_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.event_detail_fragment_layout,container, false);

        // views :
        event_title = view.findViewById(R.id.event_title_d);

        final Event event = (Event) getArguments().getSerializable("event");
        event_title.setText(event.getEvent_name());

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
