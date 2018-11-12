package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.bcs.bookexchangev2.R;

import java.util.ArrayList;

import adapters.Book_List_Adapter;
import adapters.Event_List_Adapter;
import classes.Event;
import controllers.DataBaseManager;

public class Events_profil_Fragment extends Fragment {

    private ArrayList<Event> events_list;
    private ListView event_listView;
    private ProgressBar progressBar;
    private Event_List_Adapter EventAdapter;
    private DataBaseManager dataBaseManager = new DataBaseManager();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.events_fragment, container, false);

        events_list = new ArrayList<Event>();
        EventAdapter = new Event_List_Adapter(events_list, getActivity());
        event_listView = view.findViewById(R.id.events_list);
        progressBar =  view.findViewById(R.id.progressBar);

        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {

            progressBar.setVisibility(View.VISIBLE);
            event_listView.setVisibility(View.GONE);
            dataBaseManager.getEventList(new DataBaseManager.ResultGetter<ArrayList<Event>>() {
                @Override
                public void onResult(final ArrayList<Event> eventList) {
                    progressBar.setVisibility(View.GONE);
                    event_listView.setVisibility(View.VISIBLE);
                    if (getActivity()!=null){

                        Event_List_Adapter booksAdapter = new Event_List_Adapter(eventList, getActivity());
                        event_listView.setAdapter(booksAdapter);

                    }else{
                        Log.d("Log", "getActivity est nul");
                    }
                }
            });

        }
        else {
            events_list = savedInstanceState.getParcelableArrayList("key");
            EventAdapter = new Event_List_Adapter(events_list, getActivity());
            event_listView.setAdapter(EventAdapter);
        }


        event_listView.setAdapter(EventAdapter);

        /************************************ Select event from list action **********************************/
        event_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item text from ListView
                Event value = (Event) adapterView.getItemAtPosition(i);
                Event_Detail_Fragment event_detail_fragment = new Event_Detail_Fragment();

                Bundle args = new Bundle();
                args.putSerializable("event", value);
                event_detail_fragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.dynamic_fragment_frame_layout, event_detail_fragment);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", events_list);
        super.onSaveInstanceState(outState);
    }



}
