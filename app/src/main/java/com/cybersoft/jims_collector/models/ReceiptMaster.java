package com.cybersoft.jims_collector.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReceiptMaster {

    @SerializedName("CollectorID")
    private int CollectorID;
    @SerializedName("SabeelNo")
    private String SabeelNo;
    @SerializedName("MemberID")
    private String MemberID;
    @SerializedName("Payments")
    private List<ReceiptDetail> Payments;

    public int getCollectorID() {
        return CollectorID;
    }

    public void setCollectorID(int collectorID) {
        CollectorID = collectorID;
    }

    public String getSabeelNo() {
        return SabeelNo;
    }

    public void setSabeelNo(String sabeelNo) {
        SabeelNo = sabeelNo;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }

    public List<ReceiptDetail> getPayments() {
        return Payments;
    }

    public void setPayments(List<ReceiptDetail> payments) {
        Payments = payments;
    }
}
