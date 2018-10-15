package classes;

public class Event {

    private String event_name;
    private String event_date;
    private String event_hour;

    public Event(String event_name, String event_date, String event_hour) {
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_hour = event_hour;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_hour() {
        return event_hour;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public void setEvent_hour(String event_hour) {
        this.event_hour = event_hour;
    }
}
