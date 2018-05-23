package com.emwno.fq.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created on 23 May 2018.
 */
public interface FQService {

    @GET("{fuck}/{from}")
    Call<Fuck> getFuck(@Path("fuck") String fuckToGet, @Path("from") String from);

    @GET("{fuck}/{to}/{from}")
    Call<Fuck> getFuck(@Path("fuck") String fuckToGet, @Path("to") String to, @Path("from") String from);
}
