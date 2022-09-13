package com.cybersoft.jims_collector.models;

public class Login {

    private LoginData data;
    private String message;
    private String status;

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
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

    public class LoginData
    {
        private String c_id;
        private String name;
        private double balance;

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        @Override
        public String toString() {
            return "ClassPojo [c_id = " + c_id + ", name = " + name + ", balance = " + balance + "]";
        }
    }

}