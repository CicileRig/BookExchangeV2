package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bcs.bookexchangev2.R;

import java.util.ArrayList;
import java.util.Iterator;

import adapters.Book_List_Adapter;
import adapters.Event_List_Adapter;
import classes.Book;
import classes.Event;
import controllers.BooksAPIManager;
import controllers.DataBaseManager;

public class Events_profil_Fragment extends Fragment {

    private ArrayList<Event> events_list;
    private ListView event_listView;
    private Event_List_Adapter EventAdapter;
    private DataBaseManager dataBaseManager = new DataBaseManager();


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

        dataBaseManager.getEventList(new DataBaseManager.ResultGetter<ArrayList<Event>>() {
            @Override
            public void onResult(final ArrayList<Event> eventList) {
                Event_List_Adapter booksAdapter = new Event_List_Adapter(eventList, getActivity());
                event_listView.setAdapter(booksAdapter);
            }
        });

        event_listView.setAdapter(EventAdapter);

        return view;
    }

}
