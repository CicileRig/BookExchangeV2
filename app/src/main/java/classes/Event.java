package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Event implements Serializable, Parcelable {

    private String event_id;
    private String event_name;
    private String event_date;
    private String event_hour;
    private String event_place;
    private String event_description;
    private String event_image_url;
    private String creatorId;
    private ArrayList<User> participants;

    public Event(String event_id, String event_name, String event_date, String event_hour, String event_place, String event_description, String event_image_url, String creatorId, ArrayList<User> participants) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_hour = event_hour;
        this.event_place = event_place;
        this.event_description = event_description;
        this.event_image_url = event_image_url;
        this.creatorId = creatorId;
        this.participants = participants;
    }

    public Event(){

    }

    public Event(Parcel in){
        event_name = in.readString();
        event_date = in.readString();
        event_hour = in.readString();
        event_place = in.readString();
        event_description = in.readString();
        event_image_url = in.readString();
        creatorId = in.readString();

    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_hour() {
        return event_hour;
    }

    public void setEvent_hour(String event_hour) {
        this.event_hour = event_hour;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_image_url() {
        return event_image_url;
    }

    public void setEvent_image_url(String event_image_url) {
        this.event_image_url = event_image_url;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_place() {
        return event_place;
    }

    public void setEvent_place(String event_place) {
        this.event_place = event_place;
    }



    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", event_name);
        result.put("date", event_date);
        result.put("hour", event_hour);
        result.put("place", event_place);
        result.put("description", event_description);
        result.put("imageUrl", event_image_url);
        result.put("creatorId", creatorId);
        return result;
    }

    @Override
    public String toString() {
        return event_name  ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {

        out.writeString(event_name);
        out.writeString(event_date);
        out.writeString(event_hour);
        out.writeString(event_place);
        out.writeString(event_description);
        out.writeString(event_image_url);
        out.writeString(creatorId);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
