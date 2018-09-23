package com.bignerdranch.android.iic;

import com.google.gson.JsonPrimitive;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Link {
    @GET("integers")
    Call<JsonPrimitive> getRandom(@Query("num") int num, @Query("min") int min, @Query("max") int max,
                                  @Query("col") int col, @Query("base") int base,
                                  @Query("format") String plain, @Query("rnd") String rnd);
}
