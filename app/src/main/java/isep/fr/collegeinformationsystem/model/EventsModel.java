package isep.fr.collegeinformationsystem.model;

import java.io.Serializable;

public class EventsModel implements Serializable,Comparable<EventsModel>{

    private String eventId;
    private String eventTitle;
    private String eventType;
    private String eventDateTime;
    private String eventDescription;
    private String eventImage;
    private String eventAddress;
    private String eventHall;

    public EventsModel() {

    }

    public EventsModel(String eventId, String eventTitle, String eventType, String eventDateTime, String eventDescription, String eventImage, String eventAddress, String eventHall) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
        this.eventDescription = eventDescription;
        this.eventImage = eventImage;
        this.eventAddress = eventAddress;
        this.eventHall = eventHall;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventHall() {
        return eventHall;
    }

    public void setEventHall(String eventHall) {
        this.eventHall = eventHall;
    }

    @Override
    public String toString() {
        return "EventsModel{" +
                "eventId='" + eventId + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDateTime='" + eventDateTime + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventImage='" + eventImage + '\'' +
                ", eventAddress='" + eventAddress + '\'' +
                ", eventHall='" + eventHall + '\'' +
                '}';
    }


    @Override
    public int compareTo(EventsModel eventsModel) {

        if (eventsModel.getEventId() != null && this.getEventId() != null) {

            return this.getEventId().compareToIgnoreCase(eventsModel.getEventId());
        } else {
            return 0;
        }

    }


}
