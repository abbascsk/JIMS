package com.cybersoft.jims_collector;

import com.cybersoft.jims_collector.models.Events;
import com.cybersoft.jims_collector.models.General;
import com.cybersoft.jims_collector.models.Login;
import com.cybersoft.jims_collector.models.Dues;
import com.cybersoft.jims_collector.models.Receipt;
import com.cybersoft.jims_collector.models.ReceiptMaster;
import com.cybersoft.jims_collector.models.Report;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface
{
    @FormUrlEncoded
    @POST("Login")
    Call<Login> Login(@Field("Username") String Username, @Field("Password") String Password);

    @POST("Events")
    Call<Events> Events();

    @FormUrlEncoded
    @POST("Dues")
    Call<Dues> Dues(@Field("SabeelNo") String SabeelNo);

    @POST("CreateReceipt")
    Call<General> CreateReceipt(@Body ReceiptMaster Receipt);

    @FormUrlEncoded
    @POST("PrintReceipt")
    Call<Receipt> PrintReceipt(@Field("ReceiptNo") String ReceiptNo);

    @FormUrlEncoded
    @POST("Report")
    Call<Report> Report(@Field("CollectorID") String CollectorID);
}
