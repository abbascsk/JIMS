package com.cybersoft.jims_collector.models;

import java.util.List;

public class Dues {

    private DuesData data;
    private String message;
    private String status;

    public DuesData getData() {
        return data;
    }

    public void setData(DuesData data) {
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

    public class DuesData {

        private Member Member;
        private List<DuesHead> Dues;

        public com.cybersoft.jims_collector.models.Dues.Member getMember() {
            return Member;
        }

        public void setMember(com.cybersoft.jims_collector.models.Dues.Member member) {
            Member = member;
        }

        public List<DuesHead> getDues() {
            return Dues;
        }

        public void setDues(List<DuesHead> dues) {
            Dues = dues;
        }

        @Override
        public String toString() {
            return "ClassPojo [Member = " + Member + ", Dues = " + Dues + "]";
        }
    }

    public class Member {

        private String MemberID;
        private String MemberName;
        private String MemberContact;
        private int SabeelNo;

        public String getMemberID() {
            return MemberID;
        }

        public void setMemberID(String memberID) {
            MemberID = memberID;
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

        public int getSabeelNo() {
            return SabeelNo;
        }

        public void setSabeelNo(int sabeelNo) {
            SabeelNo = sabeelNo;
        }

        @Override
        public String toString() {
            return "ClassPojo [MemberID = " + MemberID + ", MemberName = " + MemberName +
                    ", MemberContact = " + MemberContact +", SabeelNo = " + SabeelNo + "]";
        }

    }

    public class DuesHead {

        private int HeadID;
        private String HeadName;
        private double Amount;
        private double AmountToBePaid;
        private String LastPaid;

        public int getHeadID() {
            return HeadID;
        }

        public void setHeadID(int headID) {
            HeadID = headID;
        }

        public String getHeadName() {
            return HeadName;
        }

        public void setHeadName(String headName) {
            HeadName = headName;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double amount) {
            Amount = amount;
        }

        public String getLastPaid() {
            return LastPaid;
        }

        public void setLastPaid(String lastPaid) {
            LastPaid = lastPaid;
        }

        public double getAmountToBePaid() {
            return AmountToBePaid;
        }

        public void setAmountToBePaid(double amountToBePaid) {
            AmountToBePaid = amountToBePaid;
        }

        @Override
        public String toString() {
            return "ClassPojo [HeadID = " + HeadID +
                    ", HeadName = " + HeadName + ", Amount = " + Amount +
                    ", LastPaid = " + LastPaid + "]";
        }
    }

}