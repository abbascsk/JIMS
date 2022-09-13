package com.cybersoft.jims_collector;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SMSAPIInterface
{
    @GET()
    Call<String> getStringResponse(@Url String url);
}
