package com.emwno.fq.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created on 23 May 2018.
 */
public interface FQService {

    @GET("{url}")
    Call<FQ> getFuck(@Path(value = "url", encoded = true) String url);

    @GET("operations")
    Call<List<Fuck>> getOperations();

}
