package com.cybersoft.jims_collector.models;

import java.util.List;

public class Events {

    private List<EventsData> data;
    private String message;
    private String status;

    public List<EventsData> getData() {
        return data;
    }

    public void setData(List<EventsData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [data = " + data + ", message = " + message + ", status = " + status + "]";
    }

    public class EventsData
    {
        private String EventID;
        private String EventName;

        public String getEventID() {
            return EventID;
        }

        public void setEventID(String eventID) {
            EventID = eventID;
        }

        public String getEventName() {
            return EventName;
        }

        public void setEventName(String eventName) {
            EventName = eventName;
        }

        @Override
        public String toString() {
            return "ClassPojo [EventID = " + EventID + ", EventName = " + EventName + "]";
        }
    }

}