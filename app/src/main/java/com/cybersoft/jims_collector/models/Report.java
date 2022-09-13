package com.cybersoft.jims_collector.models;

import java.util.List;

public class Report {

    private List<Receipt.ReceiptData> data;
    private String message;
    private String status;

    public List<Receipt.ReceiptData> getData() {
        return data;
    }

    public void setData(List<Receipt.ReceiptData> data) {
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

}