package com.cybersoft.jims_collector.models;

import java.util.List;

public class Receipt {

    private List<ReceiptData> data;
    private String message;
    private String status;

    public List<ReceiptData> getData() {
        return data;
    }

    public void setData(List<ReceiptData> data) {
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

    public class ReceiptData {

        private String ReceiptNo;
        private String ReceiptDate;
        private String CollectorID;
        private String Collector;
        private String SabeelNo;
        private String MemberName;
        private String MemberContact;
        private String HeadID;
        private String HeadName;
        private String Amount;
        private String Dues;
        private Double Balance;

        public String getReceiptDate() {
            return ReceiptDate;
        }

        public void setReceiptDate(String receiptDate) {
            ReceiptDate = receiptDate;
        }

        public String getReceiptNo() {
            return ReceiptNo;
        }

        public void setReceiptNo(String receiptNo) {
            ReceiptNo = receiptNo;
        }

        public String getCollector() {
            return Collector;
        }

        public void setCollector(String collector) {
            Collector = collector;
        }

        public String getCollectorID() {
            return CollectorID;
        }

        public void setCollectorID(String collectorID) {
            CollectorID = collectorID;
        }

        public String getSabeelNo() {
            return SabeelNo;
        }

        public void setSabeelNo(String sabeelNo) {
            SabeelNo = sabeelNo;
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String memberName) {
            MemberName = memberName;
        }

        public String getMemberContact() {
            return MemberContact;
        }

        public void setMemberContact(String memberContact) {
            MemberContact = memberContact;
        }

        public String getHeadID() {
            return HeadID;
        }

        public void setHeadID(String headID) {
            HeadID = headID;
        }

        public String getHeadName() {
            return HeadName;
        }

        public void setHeadName(String headName) {
            HeadName = headName;
        }

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }

        public String getDues() {
            return Dues;
        }

        public void setDues(String dues) {
            Dues = dues;
        }

        public Double getBalance() {
            return Balance;
        }

        public void setBalance(Double balance) {
            Balance = balance;
        }

        @Override
        public String toString() {
            return "ClassPojo [ReceiptNo = " + ReceiptNo + ", ReceiptDate = " + ReceiptDate +
                    ", CollectorID = " + CollectorID + ", Collector = " + Collector +
                    ", SabeelNo = " + SabeelNo + ", MemberName = " + MemberName + ", MemberContact = " + MemberContact +
                    ", HeadID = " + HeadID + ", HeadName = " + HeadName +
                    ", Amount = " + Amount + ", Dues = " + Dues + ", Balance = " + Balance + "]";
        }
    }

}