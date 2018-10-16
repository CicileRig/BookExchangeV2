package adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bcs.bookexchangev2.R;

import java.util.ArrayList;

import classes.Event;

public class Event_List_Adapter extends ArrayAdapter<Event> implements View.OnClickListener{

    private ArrayList<Event> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView eventName;
        TextView eventDate;
        TextView eventHour;
    }

    public Event_List_Adapter(ArrayList<Event> data, Context context) {
        super(context, R.layout.row_event_item, data);
        this.dataSet = data;
        this.mContext=context;

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
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_event_item, parent, false);
            viewHolder.eventName =  convertView.findViewById(R.id.book_name);
            viewHolder.eventDate =  convertView.findViewById(R.id.event_date);
            viewHolder.eventHour =  convertView.findViewById(R.id.event_hour);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.eventName.setText(event.getEvent_name());
        viewHolder.eventDate.setText(event.getEvent_date());
        viewHolder.eventHour.setText(event.getEvent_hour());

        // Return the completed view to render on screen
        return convertView;
    }
}
