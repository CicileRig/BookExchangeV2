package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bcs.bookexchangev2.R;

import java.util.ArrayList;

import adapters.Event_List_Adapter;
import classes.Event;

public class Events_profil_Fragment extends Fragment {

    private ArrayList<Event> events_list;
    private ListView event_listView;
    private Event_List_Adapter EventAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.events_fragment, container, false);

        events_list = new ArrayList<Event>();
        EventAdapter = new Event_List_Adapter(events_list, getActivity());
        event_listView = view.findViewById(R.id.events_list);

        events_list.add(new Event("Salon du livre", " 24 mars 2019", "De 10h à 18h30"));
        events_list.add(new Event("Fête du livre", " 13 avril 2019", "De 9h à 18h"));
        events_list.add(new Event("Livres SWAP", " 8 aout 2019", "De 14h à 19h"));

        event_listView.setAdapter(EventAdapter);

        return view;
    }

}
